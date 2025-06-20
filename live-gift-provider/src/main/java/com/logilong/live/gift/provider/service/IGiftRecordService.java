package com.logilong.live.gift.provider.service;

import com.logilong.live.gift.dto.GiftRecordDTO;


public interface IGiftRecordService {

    /**
     * 插入单个礼物信息
     *
     * @param giftRecordDTO
     */
    void insertOne(GiftRecordDTO giftRecordDTO);

}
