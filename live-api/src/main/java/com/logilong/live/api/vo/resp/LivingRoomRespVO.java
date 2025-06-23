package com.logilong.live.api.vo.resp;

import lombok.Data;

@Data
public class LivingRoomRespVO {

    private Integer id;
    private String roomName;
    private Long anchorId;
    private Integer watchNum;
    private Integer goodNum;
    private Integer type;
    private String covertImg;
}
