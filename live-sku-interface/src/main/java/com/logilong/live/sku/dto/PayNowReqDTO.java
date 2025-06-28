package com.logilong.live.sku.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayNowReqDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -5673920967915546016L;

    private Long userId;
    private Integer roomId;

}
