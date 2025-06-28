package com.logilong.live.bank.interfaces;


import com.logilong.live.bank.dto.PayOrderDTO;


public interface IPayOrderRPC {

    /**
     * 插入订单
     */
    String insertOne(PayOrderDTO payOrderDTO);


    /**
     * 根据主键id做更新
     */
    boolean updateOrderStatus(Long id,Integer status);

    /**
     * 根据订单id做更新
     */
    boolean updateOrderStatus(String orderId,Integer status);


    /**
     * 支付回调需要请求该接口
     */
    boolean payNotify(PayOrderDTO payOrderDTO);
}
