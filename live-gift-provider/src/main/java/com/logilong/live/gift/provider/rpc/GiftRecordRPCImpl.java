package com.logilong.live.gift.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import com.logilong.live.gift.dto.GiftRecordDTO;
import com.logilong.live.gift.interfaces.IGiftRecordRPC;
import com.logilong.live.gift.provider.service.IGiftRecordService;


@DubboService
public class GiftRecordRPCImpl implements IGiftRecordRPC {

    @Resource
    private IGiftRecordService giftRecordService;

    @Override
    public void insertOne(GiftRecordDTO giftRecordDTO) {
        giftRecordService.insertOne(giftRecordDTO);
    }
}
