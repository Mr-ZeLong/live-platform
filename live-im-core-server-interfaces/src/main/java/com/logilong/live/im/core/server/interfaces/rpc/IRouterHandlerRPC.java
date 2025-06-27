package com.logilong.live.im.core.server.interfaces.rpc;

import com.logilong.live.im.dto.ImMsgBody;

import java.util.List;


/**
 * 专门给router层的服务进行调用的接口
 */
public interface IRouterHandlerRPC {

    /**
     * 按照用户id进行消息的发送
     */
    void sendMsg(ImMsgBody imMsgBody);

    /**
     * 支持批量发送消息
     */
    void batchSendMsg(List<ImMsgBody> imMsgBodyList);
}
