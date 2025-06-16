package com.logilong.live.im.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class ImMsgBody implements Serializable {

    @Serial
    private static final long serialVersionUID = -7657602083071950966L;
    /**
     * 接入im服务的各个业务线id
     */
    private int appId;
    /**
     * 用户id
     */
    private long userId;
    /**
     * 从业务服务中获取，用于在im服务建立连接的时候使用
     */
    private String token;

    /**
     * 业务标识
     */
    private int bizCode;

    /**
     * 唯一的消息id
     */
    private String msgId;

    /**
     * 和业务服务进行消息传递
     */
    private String data;
}
