package com.logilong.live.api.service;

import com.logilong.live.api.vo.req.GiftReqVO;
import com.logilong.live.api.vo.resp.GiftConfigVO;

import java.util.List;

public interface IGiftService {

    /**
     * 展示礼物列表
     *
     * @return
     */
    List<GiftConfigVO> listGift();

    /**
     * 送礼
     *
     * @param giftReqVO
     * @return
     */
    boolean send(GiftReqVO giftReqVO);
}
