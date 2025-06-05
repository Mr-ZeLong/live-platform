package com.logilong.live.api.controller;

import com.logilong.interfaces.IUserRpc;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @DubboReference
    private IUserRpc iuserRpc;

    @RequestMapping("/dubbo")
    public String dubbo()
    {
        return iuserRpc.test();
    }
}
