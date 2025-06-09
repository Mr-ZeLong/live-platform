package com.logilong.live.user.provider.service.impl;

import com.logilong.live.common.interfaces.dto.UserDTO;
import com.logilong.live.common.interfaces.utils.ConvertBeanUtils;
import com.logilong.live.user.provider.dao.mapper.IUserMapper;
import com.logilong.live.user.provider.dao.po.UserPO;
import com.logilong.live.user.provider.service.IUserService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private IUserMapper userMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public UserDTO getUserById(Long userId) {
        if(userId == null) return null;

        String key = "userInfo:" + userId;
        UserDTO userDTO = (UserDTO) redisTemplate.opsForValue().get(key);
        if(userDTO != null) return userDTO;
        userDTO = ConvertBeanUtils.convert(userMapper.selectById(userId), UserDTO.class);
        if(userDTO != null) redisTemplate.opsForValue().set(key, userDTO);

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
}
