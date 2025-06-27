package com.logilong.live.im.interfaces;

/**
 * 判断用户是否在线rpc
 */
public interface ImOnlineRPC {

    /**
     * 判断用户是否在线
     */
    boolean isOnline(long userId,int appId);
}
