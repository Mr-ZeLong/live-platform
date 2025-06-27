package com.logilong.live.im.core.server.common;

import io.netty.channel.ChannelHandlerContext;

/**
 * 利用channelHandlerContext的attr方法去 绑定/获取 一些业务属性
 */
public class ImContextUtils {

    public static Integer getRoomId(ChannelHandlerContext ctx) {
        return ctx.channel().attr(ImContextAttr.ROOM_ID).get();
    }

    public static void setRoomId(ChannelHandlerContext ctx, int roomId) {
        ctx.channel().attr(ImContextAttr.ROOM_ID).set(roomId);
    }

    public static void setAppId(ChannelHandlerContext ctx, int appId) {
        ctx.channel().attr(ImContextAttr.APP_ID).set(appId);
    }

    public static Integer getAppId(ChannelHandlerContext ctx) {
        return ctx.channel().attr(ImContextAttr.APP_ID).get();
    }

    public static void setUserId(ChannelHandlerContext ctx, Long userId) {
        ctx.channel().attr(ImContextAttr.USER_ID).set(userId);
    }

    public static Long getUserId(ChannelHandlerContext ctx) {
        return ctx.channel().attr(ImContextAttr.USER_ID).get();
    }

    public static void removeUserId(ChannelHandlerContext ctx) {
        ctx.channel().attr(ImContextAttr.USER_ID).remove();
    }

    public static void removeAppId(ChannelHandlerContext ctx) {
        ctx.channel().attr(ImContextAttr.APP_ID).remove();
    }

}
