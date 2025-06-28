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
    public ShopCarRespDTO getCarInfo(ShopCarReqDTO reqDTO) {
        return shopCarService.getCarInfo(reqDTO);
    }

    @Override
    public boolean addCar(ShopCarReqDTO reqDTO) {
        return shopCarService.addCar(reqDTO);
    }

    @Override
    public boolean removeFromCar(ShopCarReqDTO reqDTO) {
        return shopCarService.removeFromCar(reqDTO);
    }

    @Override
    public boolean clearCar(ShopCarReqDTO reqDTO) {
        return shopCarService.clearShopCar(reqDTO);
    }

    @Override
    public boolean addCarItemNum(ShopCarReqDTO reqDTO) {
        return shopCarService.addCarItemNum(reqDTO);
    }

}
