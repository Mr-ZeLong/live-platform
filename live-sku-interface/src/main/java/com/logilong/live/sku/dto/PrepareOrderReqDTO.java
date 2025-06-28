package com.logilong.live.sku.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


@Data
public class PrepareOrderReqDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -8532781733701185470L;

    private Long userId;
    private Integer roomId;
}
