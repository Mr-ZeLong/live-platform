package com.logilong.live.sku.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopCarItemRespDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -5754617334189955176L;

    private Integer count;
    private SkuInfoDTO skuInfoDTO;
}
