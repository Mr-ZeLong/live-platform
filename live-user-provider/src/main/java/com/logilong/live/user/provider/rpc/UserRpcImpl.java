package com.logilong.live.user.provider.rpc;

import com.logilong.interfaces.IUserRpc;
import com.logilong.live.common.interfaces.dto.UserDTO;
import com.logilong.live.user.provider.service.IUserService;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.List;
import java.util.Map;

@DubboService(timeout=5000)
public class UserRpcImpl implements IUserRpc {

    @Resource
    private IUserService userService;

    @Override
    public UserDTO getUserById(Long userId) {
        return userService.getUserById(userId);
    }

    @Override
    public boolean updateUserInfo(UserDTO userDTO) {
        return false;
    }

    @Override
    public boolean insertOne(UserDTO userDTO) {
        return false;
    }

    @Override
    public Map<Long, UserDTO> batchQueryUserInfo(List<Long> userIdList) {
        return Map.of();
    }
}
