package com.logilong.live.user.provider.service.impl;

import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.common.message.Message;
import com.logilong.live.framework.redis.starter.key.UserProviderCacheKeyBuilder;
import com.logilong.live.common.interfaces.topic.UserProviderTopicNames;
import com.logilong.live.common.interfaces.utils.ConvertBeanUtils;
import com.logilong.live.user.constants.CacheAsyncDeleteCode;
import com.logilong.live.user.constants.UserTagFieldNameConstants;
import com.logilong.live.user.constants.UserTagsEnum;
import com.logilong.live.user.dto.UserCacheAsyncDeleteDTO;
import com.logilong.live.user.dto.UserTagDTO;
import com.logilong.live.user.provider.dao.mapper.IUserTagMapper;
import com.logilong.live.user.provider.dao.po.UserTagPO;
import com.logilong.live.user.provider.service.IUserTagService;
import com.logilong.live.user.utils.TagInfoUtils;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
public class UserTagServiceImpl implements IUserTagService {

    @Resource
    private IUserTagMapper userTagMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private UserProviderCacheKeyBuilder cacheKeyBuilder;
    @Resource
    private MQProducer mqProducer;

    @Override
    public boolean setTag(Long userId, UserTagsEnum userTagsEnum) {
        boolean updateStatus = userTagMapper.setTag(userId, userTagsEnum.getFieldName(), userTagsEnum.getTag()) > 0;
        // 如果标签设置成功，则删除redis中对应的缓存对象
        if (updateStatus) {
            deleteUserTagDTOFromRedis(userId);
            return true;
        }
        /*
        没有设置成功存在两种情况
        ①不存在对应的用户（用户表里没有这个用户），
        ②用户标签数据没有初始化（标签表里还没这个用户的标签数据，但是用户表里有这个用户）
         */
        String setNxKey = cacheKeyBuilder.buildTagLockKey(userId);
        // 分布式并发场景下，使用原子命令setNX指令来保证并发场景下的数据一致性，
        String setNxResult = redisTemplate.execute((RedisCallback<String>) connection -> {
            RedisSerializer keySerializer = redisTemplate.getKeySerializer();
            RedisSerializer valueSerializer = redisTemplate.getValueSerializer();
            return (String) connection.execute("set", keySerializer.serialize(setNxKey),
                    // value 没有什么特殊意义，随便设置一个即可
                    valueSerializer.serialize("-1"),
                    "NX".getBytes(StandardCharsets.UTF_8),
                    "EX".getBytes(StandardCharsets.UTF_8),
                    "3".getBytes(StandardCharsets.UTF_8));
        });
        // 分布式场景下，只能有一个线程获取锁，对用户标签数据进行初始化然后插入到mysql中，
        // 其他线程直接返回false，避免大量的无效请求访问mysql数据库，降低数据库的访问压力
        if (!"OK".equals(setNxResult)) {
            return false;
        }
        // 用户标签已经设置过，不再重复设置，直接返回false
        UserTagPO userTagPO = userTagMapper.selectById(userId);
        if (userTagPO != null) {
            return false;
        }
        // 查询不到用户的标签数据，即没有初始化过，则创建并插入用户标签初始数据到MySQL中
        userTagPO = new UserTagPO();
        userTagPO.setUserId(userId);
        userTagMapper.insert(userTagPO);
        updateStatus = userTagMapper.setTag(userId, userTagsEnum.getFieldName(), userTagsEnum.getTag()) > 0;
        // 释放分布式锁
        redisTemplate.delete(setNxKey);
        return updateStatus;
    }

    @Override
    public boolean cancelTag(Long userId, UserTagsEnum userTagsEnum) {
        boolean cancelStatus = userTagMapper.cancelTag(userId, userTagsEnum.getFieldName(), userTagsEnum.getTag()) > 0;
        // 要么不存在这个用户标签，要么用户标签已经取消过一次了
        if (!cancelStatus) {
            return false;
        }
        // 如果本次用户标签取消成功，则从redis中删除对应的缓存对象
        deleteUserTagDTOFromRedis(userId);
        return true;
    }

    @Override
    public boolean containTag(Long userId, UserTagsEnum userTagsEnum) {
        UserTagDTO userTagDTO = this.queryByUserIdFromRedis(userId);
        if (userTagDTO == null || userTagDTO.getUserId() == null) {
            return false;
        }
        String queryFieldName = userTagsEnum.getFieldName();
        //需要根据标签枚举中的fieldName来识别需要匹配MySQL表中哪个字段的标签值
        if (UserTagFieldNameConstants.TAG_INFO_01.equals(queryFieldName)) {
            return TagInfoUtils.isContain(userTagDTO.getTagInfo01(), userTagsEnum.getTag());
        } else if (UserTagFieldNameConstants.TAG_INFO_02.equals(queryFieldName)) {
            return TagInfoUtils.isContain(userTagDTO.getTagInfo02(), userTagsEnum.getTag());
        } else if (UserTagFieldNameConstants.TAG_INFO_03.equals(queryFieldName)) {
            return TagInfoUtils.isContain(userTagDTO.getTagInfo03(), userTagsEnum.getTag());
        }
        // 匹配不到对应的字段名称，则返回false
        return false;
    }

    /**
     * 从redis中删除用户标签对象
     */
    private void deleteUserTagDTOFromRedis(Long userId) {
        String redisKey = cacheKeyBuilder.buildTagKey(userId);
        redisTemplate.delete(redisKey);

        /*
       场景分析：在高并发场景下，第一个更新请求先到 MySQL Master 节点更新完数据后，然后再去 redis 删除缓存，
                然后第二个请求过来之后，发现 redis 缓存中没有数据，就去 MySQL Slave 读取数据，
                此时如果 MySQL Slave 节点还没有同步数据，就会读取到旧数据，这个旧数据会被返回且放到缓存中。
       解决方案：采用延迟双删的模式，延迟一秒进行缓存的二次删除，保证数据一致性
         */
        UserCacheAsyncDeleteDTO userCacheAsyncDeleteDTO = new UserCacheAsyncDeleteDTO();
        userCacheAsyncDeleteDTO.setCode(CacheAsyncDeleteCode.USER_TAG_DELETE.getCode());
        Map<String,Object> jsonParam = new HashMap<>();
        jsonParam.put("userId",userId);
        userCacheAsyncDeleteDTO.setJson(JSON.toJSONString(jsonParam));

        Message message = new Message();
        message.setTopic(UserProviderTopicNames.CACHE_ASYNC_DELETE_TOPIC);
        message.setBody(JSON.toJSONString(userCacheAsyncDeleteDTO).getBytes());
        //延迟一秒进行缓存的二次删除
        message.setDelayTimeLevel(1);
        try {
            mqProducer.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 从redis中查询用户标签对象
     */
    private UserTagDTO queryByUserIdFromRedis(Long userId) {
        String redisKey = cacheKeyBuilder.buildTagKey(userId);
        // 先从redis中查询用户标签对象
        UserTagDTO userTagDTO = (UserTagDTO)redisTemplate.opsForValue().get(redisKey);
        if (userTagDTO != null) {
            return userTagDTO;
        }
        
        // 使用分布式锁解决缓存击穿问题
        String lockKey = cacheKeyBuilder.buildTagLockKey(userId);
        // UUID用于锁持有者校验，避免锁被其他线程误删
        String lockValue = UUID.randomUUID().toString();
        try {
            // 尝试获取分布式锁，3秒过期时间
            // 为锁设置合理过期时间（如业务执行时间+缓冲值），防止因异常未释放锁，导致死锁。
            Boolean lockSuccess = redisTemplate.opsForValue().setIfAbsent(lockKey, lockValue, 3L, TimeUnit.SECONDS);
            
            // 获取锁成功，查询数据库
            if (Boolean.TRUE.equals(lockSuccess)) {
                // 双重检查，确保没有其他线程已经加载了数据
                userTagDTO = (UserTagDTO) redisTemplate.opsForValue().get(redisKey);
                if (userTagDTO != null) {
                    return userTagDTO;
                }
                
                // 如果redis中没有数据，则从mysql中查询数据
                UserTagPO userTagPO = userTagMapper.selectById(userId);
                if (userTagPO == null) {
                    // 解决缓存穿透问题，对于不存在的数据也进行缓存，设置较短的过期时间
                    redisTemplate.opsForValue().set(redisKey, new UserTagDTO(), 1, TimeUnit.MINUTES);
                    return null;
                }
                // 如果从mysql中查询到数据，则先将数据缓存到redis中, 再返回
                userTagDTO = ConvertBeanUtils.convert(userTagPO, UserTagDTO.class);
                redisTemplate.opsForValue().set(redisKey, userTagDTO, 30, TimeUnit.MINUTES);
                return userTagDTO;
            } else {
                // 获取锁失败，短暂等待后重试查询缓存
                Thread.sleep(100);
                return (UserTagDTO)redisTemplate.opsForValue().get(redisKey);
            }
        } catch (Exception e) {
            log.error("查询用户标签对象异常，userId:{}", userId, e);
        } finally {
            // Lua脚本确保“检查锁持有者”和“删除键”操作的原子性
            String script = "if redis.call('get', KEYS) == ARGV then return redis.call('del', KEYS) else return 0 end";
            redisTemplate.execute(new DefaultRedisScript<>(script, Boolean.class),
                    Collections.singletonList(lockKey), lockValue);
            log.info("释放分布式锁，lockKey:{}, lockValue:{}", lockKey, lockValue);
        }
        return null;
    }
}
