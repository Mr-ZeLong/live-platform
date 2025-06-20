package com.logilong.live.bank.provider.service;

import com.logilong.live.bank.provider.dao.po.PayTopicPO;

public interface IPayTopicService {
    PayTopicPO getByCode(Integer code);
}
