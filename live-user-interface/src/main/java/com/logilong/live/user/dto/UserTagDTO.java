package com.logilong.live.user.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


@Data
public class UserTagDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -7287751480665429862L;
    private Long userId;
    private Long tagInfo01;
    private Long tagInfo02;
    private Long tagInfo03;
    private Date createTime;
    private Date updateTime;
}
