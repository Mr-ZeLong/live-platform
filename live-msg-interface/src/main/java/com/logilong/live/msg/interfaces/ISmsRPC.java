package com.logilong.live.msg.interfaces;

import com.logilong.live.msg.dto.MsgCheckDTO;
import com.logilong.live.msg.enums.MsgSendResultEnum;


public interface ISmsRPC {

    /**
     * 发送短信登录验证码接口
     */
    MsgSendResultEnum sendLoginCode(String phone);

    /**
     * 校验登录验证码
     */
    MsgCheckDTO checkLoginCode(String phone, Integer code);

}