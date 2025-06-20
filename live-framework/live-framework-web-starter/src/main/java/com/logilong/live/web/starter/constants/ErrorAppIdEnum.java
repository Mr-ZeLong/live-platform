package com.logilong.live.web.starter.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum ErrorAppIdEnum {

    LIVE_API_ERROR(101,"live-api");

    final int code;
    final String msg;

}
