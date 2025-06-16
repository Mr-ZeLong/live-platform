package com.logilong.live.im.core.server.interfaces.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class ImOfflineDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 7435114723872010599L;
    private Long userId;
    private Integer appId;
    private Integer roomId;
    private Long loginTime;
}
