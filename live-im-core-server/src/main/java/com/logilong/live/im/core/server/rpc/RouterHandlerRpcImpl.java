package com.logilong.live.im.core.server.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import com.logilong.live.im.core.server.interfaces.rpc.IRouterHandlerRpc;
import com.logilong.live.im.core.server.service.IRouterHandlerService;
import com.logilong.live.im.dto.ImMsgBody;

import java.util.List;

@DubboService
public class RouterHandlerRpcImpl implements IRouterHandlerRpc {

    @Resource
    private IRouterHandlerService routerHandlerService;

    @Override
    public void sendMsg(ImMsgBody imMsgBody) {
        routerHandlerService.onReceive(imMsgBody);
    }

    @Override
    public void batchSendMsg(List<ImMsgBody> imMsgBodyList) {
        imMsgBodyList.forEach(imMsgBody -> {
            routerHandlerService.onReceive(imMsgBody);
        });
    }
}
