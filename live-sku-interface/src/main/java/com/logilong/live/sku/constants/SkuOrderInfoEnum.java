package com.logilong.live.sku.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum SkuOrderInfoEnum {
    PREPARE_PAY(0, "待支付状态"),
    PAYED(1, "已支付状态"),
    END(2,"订单已关闭");

    private final int code;
    private final String desc;
}
