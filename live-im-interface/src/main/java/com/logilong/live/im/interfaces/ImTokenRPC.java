package com.logilong.live.im.interfaces;


public interface ImTokenRPC {

    /**
     * 创建用户登录im服务的token
     */
    String createImLoginToken(long userId, int appId);

    /**
     * 根据token检索用户id
     */
    Long getUserIdByToken(String token);
}
