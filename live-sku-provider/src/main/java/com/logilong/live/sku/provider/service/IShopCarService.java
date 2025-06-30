package com.logilong.live.sku.provider.service;

import com.logilong.live.sku.dto.ShopCarReqDTO;
import com.logilong.live.sku.dto.ShopCarRespDTO;

public interface IShopCarService {

    /**
     * 添加商品到购物车中
     */
    Boolean addShopCar(ShopCarReqDTO shopCarReqDTO);

    /**
     * 移除购物车
     */
    Boolean removeFromShopCar(ShopCarReqDTO shopCarReqDTO);

    /**
     * 清空购物车
     */
    Boolean clearShopCar(ShopCarReqDTO shopCarReqDTO);

    /**
     * 修改购物车中某个商品的数量
     */
    Boolean addShopCarItemNum(ShopCarReqDTO shopCarReqDTO);

    /**
     * 查看购物车信息
     */
    ShopCarRespDTO getShopCarInfo(ShopCarReqDTO shopCarReqDTO);
}
