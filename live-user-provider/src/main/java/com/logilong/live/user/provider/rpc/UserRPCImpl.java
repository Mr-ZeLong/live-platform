package com.logilong.live.user.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import com.logilong.live.user.dto.UserDTO;
import com.logilong.live.user.interfaces.IUserRPC;
import com.logilong.live.user.provider.service.IUserService;

import java.util.List;
import java.util.Map;


@DubboService
public class UserRPCImpl implements IUserRPC {

    @Resource
    private IUserService userService;

    @Override
    public UserDTO getByUserId(Long userId) {
        return userService.getByUserId(userId);
    }

    @Override
    public boolean updateUserInfo(UserDTO userDTO) {
        return userService.updateUserInfo(userDTO);
    }

    @Override
    public boolean insertOne(UserDTO userDTO) {
        return userService.insertOne(userDTO);
    }

    @Override
    public Map<Long, UserDTO> batchQueryUserInfo(List<Long> userIdList) {
        return userService.batchQueryUserInfo(userIdList);
    }

}
