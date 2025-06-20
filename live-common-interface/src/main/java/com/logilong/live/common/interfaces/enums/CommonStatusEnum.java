package com.logilong.live.common.interfaces.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum CommonStatusEnum {

    INVALID_STATUS(0,"无效"),
    VALID_STATUS(1,"有效");

    final int code;
    final String desc;
}