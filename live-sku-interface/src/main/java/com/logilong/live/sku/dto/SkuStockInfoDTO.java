package com.logilong.live.sku.dto;

import lombok.Data;


@Data
public class SkuStockInfoDTO {
    private Long id;

    private Long skuId;

    private Long stockNum;

    private Byte status;

    private Long version;
}
