package com.logilong.live.im.core.server.handler;

import io.netty.channel.ChannelHandlerContext;
import com.logilong.live.im.core.server.common.ImMsg;


public interface SimplyHandler {

    /**
     * 消息处理函数
     */
    void handler(ChannelHandlerContext ctx, ImMsg imMsg);
}
