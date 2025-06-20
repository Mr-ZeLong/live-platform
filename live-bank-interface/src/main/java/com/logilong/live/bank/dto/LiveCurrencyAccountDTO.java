package com.logilong.live.bank.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 虚拟币账户dto
 */
@Data
public class LiveCurrencyAccountDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -1270392385831310569L;

    private Long userId;
    private int currentBalance;
    private int totalCharged;
    private Integer status;
    private Date createTime;
    private Date updateTime;
}
