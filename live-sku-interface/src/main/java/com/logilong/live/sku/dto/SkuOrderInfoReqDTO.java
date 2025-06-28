package com.logilong.live.sku.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;


@Data
public class SkuOrderInfoReqDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -8947142795315398651L;

    private Long id;
    private Long orderId;
    private Long userId;
    private Integer roomId;
    private Integer status;
    private List<Long> skuIdList;
}
