package com.logilong.live.sku.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
public class SkuInfoDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 7111302331962061092L;

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

    private Date createTime;

    private Date updateTime;
}
