package com.logilong.live.msg.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import com.logilong.live.msg.dto.MsgCheckDTO;
import com.logilong.live.msg.enums.MsgSendResultEnum;
import com.logilong.live.msg.interfaces.ISmsRPC;
import com.logilong.live.msg.provider.service.ISmsService;


@DubboService
public class SmsRPCImpl implements ISmsRPC {

    @Resource
    private ISmsService smsService;

    @Override
    public MsgSendResultEnum sendLoginCode(String phone) {
        return smsService.sendLoginCode(phone);
    }

    @Override
    public MsgCheckDTO checkLoginCode(String phone, Integer code) {
        return smsService.checkLoginCode(phone,code);
    }

}