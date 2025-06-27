package com.logilong.live.im.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import com.logilong.live.im.interfaces.ImOnlineRPC;
import com.logilong.live.im.provider.service.ImOnlineService;


@DubboService
public class ImOnlineRPCImpl implements ImOnlineRPC {

    @Resource
    private ImOnlineService imOnlineService;

    @Override
    public boolean isOnline(long userId, int appId) {
        return imOnlineService.isOnline(userId,appId);
    }
}
