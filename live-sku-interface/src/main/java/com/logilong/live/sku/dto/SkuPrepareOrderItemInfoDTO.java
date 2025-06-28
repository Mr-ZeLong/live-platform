package com.logilong.live.sku.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkuPrepareOrderItemInfoDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 124965709610826174L;
    
    private Integer count;
    private SkuInfoDTO skuInfoDTO;

}
