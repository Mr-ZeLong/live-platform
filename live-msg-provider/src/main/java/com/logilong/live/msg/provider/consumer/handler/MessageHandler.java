package com.logilong.live.msg.provider.consumer.handler;

import com.logilong.live.im.dto.ImMsgBody;

public interface MessageHandler {

    /**
     * 处理im服务投递过来的消息内容
     *
     * @param imMsgBody
     */
    void onMsgReceive(ImMsgBody imMsgBody);
}
