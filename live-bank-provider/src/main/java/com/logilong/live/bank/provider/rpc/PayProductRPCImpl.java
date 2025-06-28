package com.logilong.live.bank.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import com.logilong.live.bank.dto.PayProductDTO;
import com.logilong.live.bank.interfaces.IPayProductRPC;
import com.logilong.live.bank.provider.service.IPayProductService;

import java.util.List;

@DubboService
public class PayProductRPCImpl implements IPayProductRPC {
    
    @Resource
    private IPayProductService payProductService;

    @Override
    public List<PayProductDTO> products(Integer type) {
        return payProductService.products(type);
    }

    @Override
    public PayProductDTO getByProductId(Long productId) {
        return payProductService.getByProductId(productId);
    }
}
