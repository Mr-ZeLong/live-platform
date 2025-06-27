package com.logilong.live.user.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum UserTagsEnum {

    IS_RICH((long) Math.pow(2, 0), "是否是有钱用户", "tag_info_01"),
    IS_VIP((long) Math.pow(2, 1), "是否是VIP用户", "tag_info_01"),
    IS_OLD_USER((long) Math.pow(2, 2), "是否是老用户", "tag_info_01");

    private final long tag;
    private final String desc;
    private final String fieldName;
}