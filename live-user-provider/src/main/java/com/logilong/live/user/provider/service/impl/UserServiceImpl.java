package com.logilong.live.user.provider.service.impl;

import com.logilong.live.common.interfaces.dto.UserDTO;
import com.logilong.live.common.interfaces.utils.ConvertBeanUtils;
import com.logilong.live.framework.redis.starter.key.UserProviderCacheKeyBuilder;
import com.logilong.live.user.provider.dao.mapper.IUserMapper;
import com.logilong.live.user.provider.dao.po.UserPO;
import com.logilong.live.user.provider.service.IUserService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private IUserMapper userMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    UserProviderCacheKeyBuilder builder;

    @Override
    public UserDTO getUserById(Long userId) {
        if(userId == null) return null;

        String key = builder.buildUserInfoKey(userId);

        UserDTO userDTO = (UserDTO) redisTemplate.opsForValue().get(key);
        if(userDTO != null) return userDTO;
        userDTO = ConvertBeanUtils.convert(userMapper.selectById(userId), UserDTO.class);
        if(userDTO != null) redisTemplate.opsForValue().set(key, userDTO, createRandomExpireTime(1800, 600), TimeUnit.SECONDS);

        return userDTO;
    }

    @Override
    public boolean updateUserInfo(UserDTO userDTO) {
        if(userDTO == null || userDTO.getUserId() == null) return false;
        userMapper.updateById(ConvertBeanUtils.convert(userDTO, UserPO.class));
        return true;
    }

    @Override
    public boolean insertOne(UserDTO userDTO) {
        if(userDTO == null || userDTO.getUserId() == null) return false;
        userMapper.insert(ConvertBeanUtils.convert(userDTO, UserPO.class));
        return true;
    }

    @Override
    public Map<Long, UserDTO> batchQueryUserInfo(List<Long> userIdList) {
        return Map.of();
    }


    /**
     * @param aroundTime 缓存大致过期时间
     * @param aroundTimeRange 缓存过期时间波动范围
     * @return 在波动范围内生成具体的缓存时间
     */
    private int createRandomExpireTime(int aroundTime,  int aroundTimeRange){
        int seconds = ThreadLocalRandom.current().nextInt(aroundTimeRange);
        return aroundTime + seconds;
    }
}
