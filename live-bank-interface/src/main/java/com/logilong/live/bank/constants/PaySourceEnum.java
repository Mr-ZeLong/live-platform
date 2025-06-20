package com.logilong.live.bank.constants;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 支付渠道类型
 */
@AllArgsConstructor
@Getter
@ToString
public enum PaySourceEnum {

    LIVE_LIVING_ROOM(1,"直播间内支付"),
    LIVE_USER_CENTER(2,"用户中心");

    public static PaySourceEnum find(int code) {
        for (PaySourceEnum value : PaySourceEnum.values()) {
            if(value.getCode() == code) {
                return value;
            }
        }
        return null;
    }

    private final int code;
    private final String desc;
}
