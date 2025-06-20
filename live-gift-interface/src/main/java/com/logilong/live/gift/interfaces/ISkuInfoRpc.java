package com.logilong.live.gift.interfaces;

import com.logilong.live.gift.dto.SkuDetailInfoDTO;
import com.logilong.live.gift.dto.SkuInfoDTO;

import java.util.List;

public interface ISkuInfoRpc {

    /**
     * 根据anchorId查询skuInfoList
     */
    List<SkuInfoDTO> queryByAnchorId(Long anchorId);

    SkuDetailInfoDTO queryBySkuId(Long skuId, Long anchorId);
}
