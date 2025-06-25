package com.logilong.live.api.vo.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.logilong.live.gift.dto.SkuInfoDTO;

import java.io.Serial;
import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopCarItemRespVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 4922676783085721925L;

    private Integer count;
    private SkuInfoDTO skuInfoDTO;
}
