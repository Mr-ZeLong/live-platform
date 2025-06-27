package com.logilong.live.im.core.server.service;

import com.logilong.live.im.dto.ImMsgBody;


public interface IRouterHandlerService {

    /**
     * 当收到业务服务的请求，进行处理
     */
    void onReceive(ImMsgBody imMsgBody, int times);


    /**
     * 发送消息给客户端
     */
    boolean sendMsgToClient(ImMsgBody imMsgBody);
}
