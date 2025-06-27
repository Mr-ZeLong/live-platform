package com.logilong.live.im.router.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import com.logilong.live.im.dto.ImMsgBody;
import com.logilong.live.im.router.interfaces.ImRouterRPC;
import com.logilong.live.im.router.provider.service.ImRouterService;

import java.util.List;


@DubboService
public class ImRouterRPCImpl implements ImRouterRPC {

    @Resource
    private ImRouterService routerService;

    @Override
    public boolean sendMsg(ImMsgBody imMsgBody) {
        return routerService.sendMsg(imMsgBody);
    }

    //假设我们有100个immsgbody，调用100次im-core-server  2ms,200ms
    @Override
    public void batchSendMsg(List<ImMsgBody> imMsgBodyList) {
        routerService.batchSendMsg(imMsgBodyList);
    }
}
