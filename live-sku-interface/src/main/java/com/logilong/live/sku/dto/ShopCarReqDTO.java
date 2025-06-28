package com.logilong.live.sku.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


@Data
public class ShopCarReqDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 2735264285918350741L;
    private Long userId;
    private Integer roomId;
    private Long skuId;
}
