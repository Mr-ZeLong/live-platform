package com.logilong.live.api.vo.resp;

import lombok.Data;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class ShopCarRespVO implements Serializable {
    @Serial
    private static final long serialVersionUID = -3351853678580654635L;

    private Long userId;
    private Integer roomId;
    private Long totalPrice;
    private List<ShopCarItemRespVO> shopCarItemRespVOList;
}
