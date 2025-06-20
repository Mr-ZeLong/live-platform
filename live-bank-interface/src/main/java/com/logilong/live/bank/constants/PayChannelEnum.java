package com.logilong.live.bank.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 支付渠道 0支付宝 1微信 2银联 3收银台
 */
@AllArgsConstructor
@Getter
@ToString
public enum PayChannelEnum {

    ZHI_FU_BAO(0,"支付宝"),
    WEI_XIN(1,"微信"),
    YIN_LIAN(2,"银联"),
    SHOU_YIN_TAI(3,"收银台");

    private final Integer code;
    private final String msg;
}
