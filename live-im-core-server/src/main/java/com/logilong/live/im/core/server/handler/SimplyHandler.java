package com.logilong.live.im.core.server.handler;

import io.netty.channel.ChannelHandlerContext;
import com.logilong.live.im.core.server.common.ImMsg;


public interface SimplyHandler {

    /**
     * 消息处理函数
     *
     * @param ctx
     * @param imMsg
     */
    void handler(ChannelHandlerContext ctx, ImMsg imMsg);
}
