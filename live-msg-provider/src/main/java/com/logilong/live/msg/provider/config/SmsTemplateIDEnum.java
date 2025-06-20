package com.logilong.live.msg.provider.config;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum SmsTemplateIDEnum {

    SMS_LOGIN_CODE_TEMPLATE("1","登录验证码短信模版");

    final String templateId;
    final String desc;
}
