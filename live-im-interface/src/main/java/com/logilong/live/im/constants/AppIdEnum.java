package com.logilong.live.im.constants;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum AppIdEnum {

    LIVE_BIZ(10001,"直播业务");

    private final int code;
    private final String desc;

}
