package com.logilong.live.gift.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum SkuOrderInfoEnum {
    PREPARE_PAY(0, "待支付状态"),
    HAS_PAY(1, "已支付状态"),
    CANCEL(2, "取消订单状态");

    final int code;
    final String desc;
}
