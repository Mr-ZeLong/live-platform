package com.logilong.live.im.router.constants;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public enum ImMsgBizCodeEnum {

    LIVING_ROOM_IM_CHAT_MSG_BIZ(5555,"直播间im聊天消息"),
    LIVING_ROOM_SEND_GIFT_SUCCESS(5556,"送礼成功"),
    LIVING_ROOM_SEND_GIFT_FAIL(5557,"送礼失败"),
    LIVING_ROOM_PK_SEND_GIFT_SUCCESS(5558,"pk送礼成功"),
    LIVING_ROOM_PK_ONLINE(5559,"pk连线"),
    RED_PACKET_CONFIG(5560, "开启红包雨活动");

    private final int code;
    private final String desc;
}
