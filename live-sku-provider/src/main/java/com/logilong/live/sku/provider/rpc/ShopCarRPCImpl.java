package com.logilong.live.sku.provider.rpc;

import com.logilong.live.sku.dto.ShopCarReqDTO;
import com.logilong.live.sku.dto.ShopCarRespDTO;
import com.logilong.live.sku.interfaces.IShopCarRPC;
import com.logilong.live.sku.provider.service.IShopCarService;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
public class ShopCarRPCImpl implements IShopCarRPC {
    
    @Resource
    private IShopCarService shopCarService;

    @Override
    public ShopCarRespDTO getShopCarInfo(ShopCarReqDTO reqDTO) {
        return shopCarService.getShopCarInfo(reqDTO);
    }

    @Override
    public boolean addShopCar(ShopCarReqDTO reqDTO) {
        return shopCarService.addShopCar(reqDTO);
    }

    @Override
    public boolean removeFromShopCar(ShopCarReqDTO reqDTO) {
        return shopCarService.removeFromShopCar(reqDTO);
    }

    @Override
    public boolean clearShopCar(ShopCarReqDTO reqDTO) {
        return shopCarService.clearShopCar(reqDTO);
    }

    @Override
    public boolean addShopCarItemNum(ShopCarReqDTO reqDTO) {
        return shopCarService.addShopCarItemNum(reqDTO);
    }

}
