package com.logilong.live.sku.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


@Data
public class SkuOrderInfoRespDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -8080218092477992411L;

    private Long id;

    private String skuIdList;

    private Long userId;

    private Integer roomId;

    private Long status;

    private String extra;

    Date createTime;

    Date updateTime;

}
