package com.logilong.live.user.provider.rpc;

import com.logilong.interfaces.IUserRpc;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
public class IUserPpcImpl implements IUserRpc {
    @Override
    public String test() {
        System.out.println("测试");
        return "SUCCESS";
    }
}
