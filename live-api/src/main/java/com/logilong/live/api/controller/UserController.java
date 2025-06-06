package com.logilong.live.api.controller;

import com.logilong.interfaces.IUserRpc;
import com.logilong.live.common.interfaces.dto.UserDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @DubboReference
    private IUserRpc userRpc;

    @GetMapping("/getUserInfo")
    public UserDTO getUserInfo(Long userId) {
        return userRpc.getUserById(userId);
    }
}
