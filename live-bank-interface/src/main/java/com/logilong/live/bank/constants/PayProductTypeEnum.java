package com.logilong.live.bank.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum PayProductTypeEnum {

    LIVE_COIN(0,"直播间充值-虚拟币产品");

    final Integer code;
    final String desc;
}
