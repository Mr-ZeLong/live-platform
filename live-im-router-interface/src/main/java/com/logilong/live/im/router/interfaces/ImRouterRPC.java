package com.logilong.live.im.router.interfaces;

import com.logilong.live.im.dto.ImMsgBody;

import java.util.List;

public interface ImRouterRPC {

    /**
     * 发送消息
     */
    boolean sendMsg(ImMsgBody imMsgBody);


    /**
     * 批量发送消息，在直播间内
     */
    void batchSendMsg(List<ImMsgBody> imMsgBody);
}
