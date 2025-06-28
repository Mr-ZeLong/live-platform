package com.logilong.live.sku.provider.service;


import com.logilong.live.sku.dto.SkuOrderInfoReqDTO;
import com.logilong.live.sku.dto.SkuOrderInfoRespDTO;
import com.logilong.live.sku.provider.dao.po.SkuOrderInfoPO;

public interface ISkuOrderInfoService {

    /**
     * 根据userId和roomId查询订单信息
     */
    SkuOrderInfoRespDTO queryByUserIdAndRoomId(Long userId, Integer roomId);

    /**
     * 插入一条订单
     */
    SkuOrderInfoPO insertOne(SkuOrderInfoReqDTO skuOrderInfoReqDTO);

    /**
     * 更新订单状态
     */
    boolean updateOrderStatus(SkuOrderInfoReqDTO skuOrderInfoReqDTO);

    SkuOrderInfoRespDTO queryByOrderId(Long orderId);
}
