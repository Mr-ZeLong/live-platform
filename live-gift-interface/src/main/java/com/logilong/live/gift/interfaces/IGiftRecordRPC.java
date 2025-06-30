package com.logilong.live.gift.interfaces;

import com.logilong.live.gift.dto.GiftRecordDTO;

public interface IGiftRecordRPC {

    /**
     * 插入一条送礼记录
     */
    void insertOne(GiftRecordDTO giftRecordDTO);
}
