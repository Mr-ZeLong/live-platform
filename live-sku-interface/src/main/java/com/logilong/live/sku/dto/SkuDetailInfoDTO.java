package com.logilong.live.sku.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


@Data
public class SkuDetailInfoDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -1858463450572581836L;
    private Long id;

    private Long skuId;

    private Long skuPrice;

    private String skuCode;

    private String name;

    private String iconUrl;

    private String originalIconUrl;

    private String remark;

    private Byte status;

    private Long categoryId;
}
