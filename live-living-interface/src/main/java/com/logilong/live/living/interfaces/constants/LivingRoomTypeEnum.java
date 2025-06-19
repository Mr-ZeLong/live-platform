package com.logilong.live.living.interfaces.constants;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum LivingRoomTypeEnum {

    DEFAULT_LIVING_ROOM(1,"普通直播间"),
    PK_LIVING_ROOM(2,"pk直播间");

    LivingRoomTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    final Integer code;
    final String desc;
}
