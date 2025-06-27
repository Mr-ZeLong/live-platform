package com.logilong.live.common.interfaces.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum RedPacketStatusEum {

    IS_PREPARE(1,"待准备"),
    PREPARED(2,"已准备"),
    HAD_SEND(3,"已发送");

    private final int code;
    private final String desc;
}
