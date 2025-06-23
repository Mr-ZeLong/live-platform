package com.logilong.live.api.vo.req;

import lombok.Data;

@Data
public class GiftReqVO {

    private int giftId;
    private Integer roomId;
    private Long senderUserId;
    private Long receiverId;
    private int type;
}
