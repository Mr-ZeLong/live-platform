package com.logilong.live.bank.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
public class PayOrderDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -9209044050847451420L;

    private Long id;
    private String orderId;
    private Long productId;
    private Integer bizCode;
    private Long userId;
    private Integer source;
    private Integer payChannel;
    private Integer status;
    private Date payTime;
}
