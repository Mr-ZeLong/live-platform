package com.logilong.live.sku.interfaces;

import com.logilong.live.sku.dto.ShopCarReqDTO;
import com.logilong.live.sku.dto.ShopCarRespDTO;

public interface IShopCarRPC {
    /**
     * 查看购物车信息
     */
    ShopCarRespDTO getCarInfo(ShopCarReqDTO reqDTO);

    /**
     * 添加商品到购物车中
     */
    boolean addCar(ShopCarReqDTO reqDTO);

    /**
     * 从购物车中，删除商品
     */
    boolean removeFromCar(ShopCarReqDTO reqDTO);

    /**
     * 清理购物车
     */
    boolean clearCar(ShopCarReqDTO reqDTO);

    /**
     * 修改购物车中某个商品的数量
     */
    boolean addCarItemNum(ShopCarReqDTO reqDTO);
}
