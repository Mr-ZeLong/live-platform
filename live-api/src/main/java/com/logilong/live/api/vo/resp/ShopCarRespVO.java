package com.logilong.live.api.vo.resp;

import lombok.Data;
import com.logilong.live.gift.dto.ShopCarItemRespDTO;

import java.util.List;

@Data
public class ShopCarRespVO {

    private Long userId;
    private Integer roomId;
    private Long totalPrice;
    private List<ShopCarItemRespDTO> shopCarItemRespDTOS;
}
