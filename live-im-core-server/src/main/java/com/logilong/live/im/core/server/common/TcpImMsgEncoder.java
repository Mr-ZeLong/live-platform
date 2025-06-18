package com.logilong.live.im.core.server.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 处理消息的编码过程
 */
public class TcpImMsgEncoder extends MessageToByteEncoder<ImMsg> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ImMsg msg, ByteBuf out) throws Exception {
        out.writeShort(msg.getMagic());
        out.writeInt(msg.getCode());
        out.writeInt(msg.getLen());
        out.writeBytes(msg.getBody());
    }
}
