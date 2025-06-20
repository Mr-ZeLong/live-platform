package com.logilong.live.web.starter.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum BizBaseErrorEnum implements LiveBaseError {

    PARAM_ERROR(100001,"参数异常"),
    TOKEN_ERROR(100002,"用户token异常");

    private final int errorCode;
    private final String errorMsg;
}
