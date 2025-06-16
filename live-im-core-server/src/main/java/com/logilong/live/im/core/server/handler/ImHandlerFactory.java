package com.logilong.live.im.core.server.handler;

import io.netty.channel.ChannelHandlerContext;
import com.logilong.live.im.core.server.common.ImMsg;


public interface ImHandlerFactory {

    /**
     * 按照immsg的code去筛选
     *
     * @param channelHandlerContext
     * @param imMsg
     */
    void doMsgHandler(ChannelHandlerContext channelHandlerContext, ImMsg imMsg);
}
