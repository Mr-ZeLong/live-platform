package com.logilong.live.bank.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import com.logilong.live.bank.provider.dao.mapper.IPayTopicMapper;
import com.logilong.live.bank.provider.dao.po.PayTopicPO;
import com.logilong.live.bank.provider.service.IPayTopicService;
import com.logilong.live.common.interfaces.enums.CommonStatusEnum;
import org.springframework.stereotype.Service;

@Service
public class PayTopicServiceImpl implements IPayTopicService {
    
    @Resource
    private IPayTopicMapper IPayTopicMapper;

    @Override
    public PayTopicPO getByCode(Integer code) {
        LambdaQueryWrapper<PayTopicPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PayTopicPO::getBizCode, code);
        queryWrapper.eq(PayTopicPO::getStatus, CommonStatusEnum.VALID_STATUS.getCode());
        queryWrapper.last("limit 1");
        return IPayTopicMapper.selectOne(queryWrapper);
    }
}
