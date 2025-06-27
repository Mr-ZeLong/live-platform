package com.logilong.live.im.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import com.logilong.live.im.interfaces.ImTokenRPC;
import com.logilong.live.im.provider.service.ImTokenService;

/**
 * 用户登录token rpc
 */
@DubboService
public class ImTokenRPCImpl implements ImTokenRPC {

    @Resource
    private ImTokenService imTokenService;

    @Override
    public String createImLoginToken(long userId, int appId) {
        return imTokenService.createImLoginToken(userId,appId);
    }

    @Override
    public Long getUserIdByToken(String token) {
        return imTokenService.getUserIdByToken(token);
    }
}
