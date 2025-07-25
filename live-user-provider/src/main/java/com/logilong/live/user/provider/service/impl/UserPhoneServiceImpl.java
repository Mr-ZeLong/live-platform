package com.logilong.live.user.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import com.logilong.live.framework.redis.starter.key.UserProviderCacheKeyBuilder;
import com.logilong.live.common.interfaces.enums.CommonStatusEnum;
import com.logilong.live.common.interfaces.utils.ConvertBeanUtils;
import com.logilong.live.common.interfaces.utils.DESUtils;
import com.logilong.live.id.generate.enums.IdTypeEnum;
import com.logilong.live.id.generate.interfaces.IdGenerateRPC;
import com.logilong.live.user.dto.UserDTO;
import com.logilong.live.user.dto.UserLoginDTO;
import com.logilong.live.user.dto.UserPhoneDTO;
import com.logilong.live.user.provider.dao.mapper.IUserPhoneMapper;
import com.logilong.live.user.provider.dao.po.UserPhonePO;
import com.logilong.live.user.provider.service.IUserPhoneService;
import com.logilong.live.user.provider.service.IUserService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service
@Slf4j
public class UserPhoneServiceImpl implements IUserPhoneService {

    @Resource
    private IUserPhoneMapper userPhoneMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private UserProviderCacheKeyBuilder cacheKeyBuilder;
    @Resource
    private IUserService userService;
    @DubboReference
    private IdGenerateRPC idGenerateRpc;
    @Resource
    private UserPhoneServiceImpl userPhoneServiceImpl;

    @Override
    public UserLoginDTO login(String phone) {
        //phone不能为空
        if (StringUtils.isEmpty(phone)) {
            return null;
        }
        //是否注册过
        UserPhoneDTO userPhoneDTO = this.queryByPhone(phone);
        //如果注册过，创建token，返回userId
        if (userPhoneDTO != null) {
            //这里token创建使用新模块代替
            return UserLoginDTO.loginSuccess(userPhoneDTO.getUserId(), createAndSaveLoginToken(userPhoneDTO.getUserId()));
        }
        //如果没注册过，生成user信息，插入手机记录，绑定userId
        return userPhoneServiceImpl.registerAndLogin(phone);
    }

    /**
     * 注册 + 登录
     */
    @Transactional(rollbackFor = Exception.class)
    protected UserLoginDTO registerAndLogin(String phone) {
        try {
            // 通过分布式ID生成器获取userId
            Long userId = idGenerateRpc.getSeqId(IdTypeEnum.USER_ID.getCode());
            UserDTO userDTO = new UserDTO();
            userDTO.setNickName("直播平台用户-" + userId);
            userDTO.setUserId(userId);
            userService.insertOne(userDTO);
            insertUserPhone(phone, userId);
            // 如果有空值缓存，则删除，因为查询的时候有可能缓存了空值对象
            redisTemplate.delete(cacheKeyBuilder.buildUserPhoneObjKey(phone));
            return UserLoginDTO.loginSuccess(userId, createAndSaveLoginToken(userId));
        } catch (Exception e) {
            // 记录日志
            log.error("用户注册登录失败，手机号:{}", phone, e);
            throw e; // 重新抛出异常以触发事务回滚
        }
    }

    private String createAndSaveLoginToken(Long userId) {
        String token = UUID.randomUUID().toString();
        String redisKey = cacheKeyBuilder.buildUserLoginTokenKey(token);
        redisTemplate.opsForValue().set(redisKey, userId, 30L, TimeUnit.DAYS);
        return token;
    }

    @Override
    public boolean insertUserPhone(String phone, Long userId) {
        UserPhonePO userPhonePO = new UserPhonePO();
        userPhonePO.setUserId(userId != null ? userId : idGenerateRpc.getSeqId(IdTypeEnum.USER_ID.getCode()));
        userPhonePO.setPhone(DESUtils.encrypt(phone));
        userPhonePO.setStatus(CommonStatusEnum.VALID_STATUS.getCode());
        userPhoneMapper.insert(userPhonePO);
        return true;
    }

    @Override
    public UserPhoneDTO queryByPhone(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return null;
        }
        String redisKey = cacheKeyBuilder.buildUserPhoneObjKey(phone);
        UserPhoneDTO userPhoneDTO = (UserPhoneDTO) redisTemplate.opsForValue().get(redisKey);
        if (userPhoneDTO != null) {
            //属于空值缓存对象
            if (userPhoneDTO.getUserId() == null) {
                return null;
            }
            return userPhoneDTO;
        }
        userPhoneDTO = this.queryByPhoneFromDB(phone);
        if (userPhoneDTO != null) {
            userPhoneDTO.setPhone(DESUtils.decrypt(userPhoneDTO.getPhone()));
            redisTemplate.opsForValue().set(redisKey, userPhoneDTO, 30, TimeUnit.MINUTES);
            return userPhoneDTO;
        }
        //缓存击穿，空值缓存
        userPhoneDTO = new UserPhoneDTO();
        redisTemplate.opsForValue().set(redisKey, userPhoneDTO, 5, TimeUnit.MINUTES);
        return null;
    }

    @Override
    public List<UserPhoneDTO> queryByUserId(Long userId) {
        if (userId == null || userId < 10000) {
            return Collections.emptyList();
        }
        String redisKey = cacheKeyBuilder.buildUserPhoneListKey(userId);
        List<Object> userPhoneList = redisTemplate.opsForList().range(redisKey, 0, -1);
        if (!CollectionUtils.isEmpty(userPhoneList)) {
            //证明是空值缓存
            if (((UserPhoneDTO) userPhoneList.get(0)).getUserId() == null) {
                return Collections.emptyList();
            }
            return userPhoneList.stream().map(x -> (UserPhoneDTO) x).collect(Collectors.toList());
        }
        List<UserPhoneDTO> userPhoneDTOS = this.queryByUserIdFromDB(userId);
        if (!CollectionUtils.isEmpty(userPhoneDTOS)) {
            userPhoneDTOS.forEach(x -> x.setPhone(DESUtils.decrypt(x.getPhone())));
            redisTemplate.opsForList().leftPushAll(redisKey, userPhoneDTOS.toArray());
            redisTemplate.expire(redisKey, 30, TimeUnit.MINUTES);
            return userPhoneDTOS;
        }
        //缓存击穿，空对象缓存
        redisTemplate.opsForList().leftPush(redisKey, new UserPhoneDTO());
        redisTemplate.expire(redisKey, 5, TimeUnit.MINUTES);
        return Collections.emptyList();
    }

    /**
     * 根据用户id查询记录
     */
    private List<UserPhoneDTO> queryByUserIdFromDB(Long userId) {
        LambdaQueryWrapper<UserPhonePO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserPhonePO::getUserId, userId);
        queryWrapper.eq(UserPhonePO::getStatus, CommonStatusEnum.VALID_STATUS.getCode());
        queryWrapper.last("limit 1");
        return ConvertBeanUtils.convertList(userPhoneMapper.selectList(queryWrapper), UserPhoneDTO.class);
    }

    /**
     * 根据手机号查询记录
     */
    private UserPhoneDTO queryByPhoneFromDB(String phone) {
        LambdaQueryWrapper<UserPhonePO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserPhonePO::getPhone, DESUtils.encrypt(phone));
        queryWrapper.eq(UserPhonePO::getStatus, CommonStatusEnum.VALID_STATUS.getCode());
        queryWrapper.last("limit 1");
        return ConvertBeanUtils.convert(userPhoneMapper.selectOne(queryWrapper), UserPhoneDTO.class);
    }
}
