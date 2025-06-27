package com.logilong.live.im.core.server.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import com.logilong.live.im.core.server.interfaces.rpc.IRouterHandlerRPC;
import com.logilong.live.im.core.server.service.IRouterHandlerService;
import com.logilong.live.im.dto.ImMsgBody;

import java.util.List;

@DubboService
public class RouterHandlerRPCImpl implements IRouterHandlerRPC {

    @Resource
    private IRouterHandlerService routerHandlerService;

    @Override
    public void sendMsg(ImMsgBody imMsgBody) {
        routerHandlerService.onReceive(imMsgBody, 1);
    }

    @Override
    public void batchSendMsg(List<ImMsgBody> imMsgBodyList) {
        imMsgBodyList.forEach(imMsgBody -> {
            routerHandlerService.onReceive(imMsgBody, 1);
        });
    }
}
