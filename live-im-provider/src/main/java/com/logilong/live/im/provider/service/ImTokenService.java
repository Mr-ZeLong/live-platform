package com.logilong.live.im.provider.service;

/**
 * 用户登录token service
 */
public interface ImTokenService {

    /**
     * 创建用户登录im服务的token
     */
    String createImLoginToken(long userId, int appId);

    /**
     * 根据token检索用户id
     */
    Long getUserIdByToken(String token);
}
