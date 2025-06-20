package com.logilong.live.msg.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@AllArgsConstructor
@Data
public class MsgCheckDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 3394248744287019717L;
    private boolean checkStatus;
    private String desc;
}