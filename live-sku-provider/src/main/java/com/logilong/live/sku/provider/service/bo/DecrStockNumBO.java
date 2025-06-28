package com.logilong.live.sku.provider.service.bo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 */
@Data
public class DecrStockNumBO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1829802235999323708L;
    private boolean isSuccess;
    private boolean isEmptyStock;
}
