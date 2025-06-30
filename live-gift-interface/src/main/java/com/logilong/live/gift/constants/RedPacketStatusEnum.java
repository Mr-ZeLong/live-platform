package com.logilong.live.gift.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum RedPacketStatusEnum {
    
    NOT_PREPARED(1,"待准备"),
    IS_PREPARED(2, "已准备"),
    IS_SEND(3, "已发送");

    final int code;
    final String desc;
}
