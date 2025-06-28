package com.logilong.live.sku.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkuPrepareOrderInfoDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 5101075046224519335L;

    private Integer totalPrice;
    private List<SkuPrepareOrderItemInfoDTO> skuPrepareOrderItemInfoDTOList;
}
