package com.logilong.live.common.interfaces.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 网关服务传递给下游的header枚举
 */
@AllArgsConstructor
@Getter
@ToString
public enum GatewayHeaderEnum {

    USER_LOGIN_ID("用户id","gh_user_id");

    private final String desc;
    private final String name;
}
