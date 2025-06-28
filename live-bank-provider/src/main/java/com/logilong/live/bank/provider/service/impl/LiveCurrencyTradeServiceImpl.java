package com.logilong.live.bank.provider.service.impl;

import com.logilong.live.bank.provider.dao.po.LiveCurrencyTradePO;
import jakarta.annotation.Resource;
import com.logilong.live.bank.provider.dao.mapper.ILiveCurrencyTradeMapper;
import com.logilong.live.bank.provider.service.ILiveCurrencyTradeService;
import com.logilong.live.common.interfaces.enums.CommonStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LiveCurrencyTradeServiceImpl implements ILiveCurrencyTradeService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(LiveCurrencyTradeServiceImpl.class);
    
    @Resource
    private ILiveCurrencyTradeMapper liveCurrencyTradeMapper;
    
    @Override
    public boolean insertOne(Long userId, int num, int type) {
        try {
            LiveCurrencyTradePO tradePO = new LiveCurrencyTradePO();
            tradePO.setUserId(userId);
            tradePO.setNum(num);
            tradePO.setType(type);
            tradePO.setStatus(CommonStatusEnum.VALID_STATUS.getCode());
            liveCurrencyTradeMapper.insert(tradePO);
            return true;
        } catch (Exception e) {
            LOGGER.error("[LiveCurrencyTradeServiceImpl] insert error, error is:", e);
        }
        return false;
    }
}
