package com.logilong.live.bank.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 订单状态（0待支付,1支付中,2已支付,3撤销,4无效）
 */
@AllArgsConstructor
@Getter
@ToString
public enum OrderStatusEnum {

    WAITING_PAY(0,"待支付"),
    PAYING(1,"支付中"),
    PAYED(2,"已支付"),
    PAY_BACK(3,"撤销"),
    IN_VALID(4,"无效");

    private final Integer code;
    private final String msg;
}
