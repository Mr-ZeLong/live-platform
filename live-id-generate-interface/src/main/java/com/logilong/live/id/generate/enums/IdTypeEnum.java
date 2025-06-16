package com.logilong.live.id.generate.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum IdTypeEnum {

    USER_ID(1,"用户id生成策略");

    private final int code;
    private final String desc;
}
