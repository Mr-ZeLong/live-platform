package com.logilong.live.sku.interfaces;

import com.logilong.live.sku.dto.ShopCarReqDTO;
import com.logilong.live.sku.dto.ShopCarRespDTO;

public interface IShopCarRPC {
    /**
     * 查看购物车信息
     */
    ShopCarRespDTO getShopCarInfo(ShopCarReqDTO reqDTO);

    /**
     * 添加商品到购物车中
     */
    boolean addShopCar(ShopCarReqDTO reqDTO);

    /**
     * 从购物车中，删除商品
     */
    boolean removeFromShopCar(ShopCarReqDTO reqDTO);

    /**
     * 清理购物车
     */
    boolean clearShopCar(ShopCarReqDTO reqDTO);

    /**
     * 修改购物车中某个商品的数量
     */
    boolean addShopCarItemNum(ShopCarReqDTO reqDTO);
}
