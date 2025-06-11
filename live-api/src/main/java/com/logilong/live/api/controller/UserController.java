package com.logilong.live.api.controller;

import com.logilong.live.user.interfaces.IUserRpc;
import com.logilong.live.common.interfaces.dto.UserDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @DubboReference
    private IUserRpc userRpc;

    @GetMapping("/getUserInfo")
    public UserDTO getUserInfo(Long userId) {
        return userRpc.getUserById(userId);
    }

    @GetMapping("/updateUserInfo")
    public boolean updateUserInfo(Long userId, String nickName) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userId);
        userDTO.setNickName(nickName);
        return userRpc.updateUserInfo(userDTO);
    }

    @GetMapping("/insertOne")
    public boolean insertOne(Long userId, String nickName) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userId);
        userDTO.setNickName(nickName);
        return userRpc.insertOne(userDTO);
    }

    @GetMapping("/batchQueryUserInfo")
    public Map<Long, UserDTO> batchQueryUserInfo(Long... userIds) {
        return userRpc.batchQueryUserInfo(Arrays.asList(userIds));
    }

}
