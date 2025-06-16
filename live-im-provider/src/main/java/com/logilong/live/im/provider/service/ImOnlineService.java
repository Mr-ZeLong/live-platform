package com.logilong.live.im.provider.service;

/**
 * 判断用户是否在线service
 */
public interface ImOnlineService {

    /**
     * 判断用户是否在线
     *
     * @param userId
     * @param appId
     * @return
     */
    boolean isOnline(long userId,int appId);
}
