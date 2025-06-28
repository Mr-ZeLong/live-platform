package com.logilong.live.sku.interfaces;


import com.logilong.live.sku.dto.SkuDetailInfoDTO;
import com.logilong.live.sku.dto.SkuInfoDTO;

import java.util.List;


public interface ISkuInfoRPC {

    /**
     * 通过id查询Sku信息
     */
    List<SkuInfoDTO> queryByAnchorId(Long anchorId);

    /**
     * 通过skuId查询商品详情
     */
    SkuDetailInfoDTO queryBySkuId(Long skuId);
}
