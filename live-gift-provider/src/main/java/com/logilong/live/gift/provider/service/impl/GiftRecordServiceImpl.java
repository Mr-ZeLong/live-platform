package com.logilong.live.gift.provider.service.impl;

import jakarta.annotation.Resource;
import com.logilong.live.common.interfaces.utils.ConvertBeanUtils;
import com.logilong.live.gift.dto.GiftRecordDTO;
import com.logilong.live.gift.provider.dao.mapper.GiftRecordMapper;
import com.logilong.live.gift.provider.dao.po.GiftRecordPO;
import com.logilong.live.gift.provider.service.IGiftRecordService;
import org.springframework.stereotype.Service;


@Service
public class GiftRecordServiceImpl implements IGiftRecordService {

    @Resource
    private GiftRecordMapper giftRecordMapper;

    @Override
    public void insertOne(GiftRecordDTO giftRecordDTO) {
        GiftRecordPO giftRecordPO = ConvertBeanUtils.convert(giftRecordDTO,GiftRecordPO.class);
        giftRecordMapper.insert(giftRecordPO);
    }
}
