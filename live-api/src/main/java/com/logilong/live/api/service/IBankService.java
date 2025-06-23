package com.logilong.live.api.service;

import com.logilong.live.api.vo.req.PayProductReqVO;
import com.logilong.live.api.vo.resp.PayProductRespVO;
import com.logilong.live.api.vo.resp.PayProductVO;

public interface IBankService {

    /**
     * 查询相关的产品列表信息
     *
     * @param type
     * @return
     */
    PayProductVO products(Integer type);

    /**
     * 发起支付
     *
     * @param payProductReqVO
     * @return
     */
    PayProductRespVO payProduct(PayProductReqVO payProductReqVO);
}
