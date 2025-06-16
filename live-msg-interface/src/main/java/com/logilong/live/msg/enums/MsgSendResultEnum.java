package com.logilong.live.msg.enums;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
public enum MsgSendResultEnum {

    SEND_SUCCESS(0,"成功"),
    SEND_FAIL(1,"发送失败"),
    MSG_PARAM_ERROR(2,"消息参数异常");

    final int code;
    final String desc;

}