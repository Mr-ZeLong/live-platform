package com.logilong.live.gift.interfaces;

import com.logilong.live.gift.dto.GiftConfigDTO;
import com.logilong.live.gift.dto.GiftRecordDTO;

import java.util.List;

public interface IGiftRecordRpc {

    /**
     * 插入一条送礼记录
     */
    void insertOne(GiftRecordDTO giftRecordDTO);
}
