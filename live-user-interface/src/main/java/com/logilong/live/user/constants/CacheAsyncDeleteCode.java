package com.logilong.live.user.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum CacheAsyncDeleteCode {

    USER_INFO_DELETE(0, "用户基础信息删除"),
    USER_TAG_DELETE(1, "用户标签删除");

    final int code;
    final String desc;

}
