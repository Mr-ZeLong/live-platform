package com.logilong.live.sku.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;


@Data
public class ShopCarRespDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 6213928122846364022L;

    private Long userId;
    private Long roomId;
    private Long totalPrice;
    private List<ShopCarItemRespDTO> shopCarItemRespDTOList;
}
