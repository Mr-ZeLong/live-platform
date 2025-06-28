package com.logilong.live.sku.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class RollbackStockInfoDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 7085904237045846612L;

    private Long userId;
    private Long orderId;
}
