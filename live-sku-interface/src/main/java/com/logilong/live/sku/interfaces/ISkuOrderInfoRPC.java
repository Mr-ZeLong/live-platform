package com.logilong.live.sku.interfaces;

import com.logilong.live.sku.dto.*;

public interface ISkuOrderInfoRPC {

    /**
     * 多直播间用户订单信息查询
     */
    SkuOrderInfoRespDTO queryByUserIdAndRoomId(Long userId, Integer roomId);

    /**
     * 新增订单
     */
    boolean insertOne(SkuOrderInfoReqDTO reqDTO);

    /**
     * 修改订单状态
     */
    boolean updateOrderStatus(SkuOrderInfoReqDTO skuOrderInfoReqDTO);
    /**
     * 预下单接口
     */
    SkuPrepareOrderInfoDTO prepareOrder(PrepareOrderReqDTO reqDTO);

    /**
     * 支付
     */
    boolean payNow(PayNowReqDTO convert);

}
