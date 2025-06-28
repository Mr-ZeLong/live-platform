package com.logilong.live.bank.interfaces;

import com.logilong.live.bank.dto.PayProductDTO;

import java.util.List;


public interface IPayProductRPC {

    /**
     * 返回批量的商品信息
     * @param type 不同的业务场景所使用的产品
     */
    List<PayProductDTO> products(Integer type);


    /**
     * 根据产品id查询
     */
    PayProductDTO getByProductId(Long productId);
}
