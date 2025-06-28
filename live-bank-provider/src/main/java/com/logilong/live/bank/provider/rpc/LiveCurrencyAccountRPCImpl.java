package com.logilong.live.bank.provider.rpc;

import com.logilong.live.bank.dto.LiveCurrencyAccountDTO;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import com.logilong.live.bank.dto.AccountTradeReqDTO;
import com.logilong.live.bank.dto.AccountTradeRespDTO;
import com.logilong.live.bank.interfaces.ILiveCurrencyAccountRPC;
import com.logilong.live.bank.provider.service.ILiveCurrencyAccountService;

@DubboService
public class LiveCurrencyAccountRPCImpl implements ILiveCurrencyAccountRPC {
    
    @Resource
    private ILiveCurrencyAccountService liveCurrencyAccountService;

    @Override
    public boolean insertOne(Long userId) {
        return liveCurrencyAccountService.insertOne(userId);
    }

    @Override
    public void incr(Long userId, int num) {
        liveCurrencyAccountService.incr(userId, num);
    }

    @Override
    public void decr(Long userId, int num) {
        liveCurrencyAccountService.decr(userId, num);
    }

    @Override
    public boolean decrV2(long userId, int num) {
        Integer balance = getBalance(userId);
        if (balance - num < 0) {
            return false;
        }
        return liveCurrencyAccountService.decr(userId, num);
    }

    @Override
    public LiveCurrencyAccountDTO getByUserId(Long userId) {
        return liveCurrencyAccountService.getByUserId(userId);
    }

    @Override
    public Integer getBalance(Long userId) {
        return liveCurrencyAccountService.getBalance(userId);
    }

    @Override
    public AccountTradeRespDTO consumeForSendGift(AccountTradeReqDTO accountTradeReqDTO) {
        return liveCurrencyAccountService.consumeForSendGift(accountTradeReqDTO);
    }

    @Override
    public AccountTradeRespDTO consume(AccountTradeReqDTO accountTradeReqDTO) {
        return liveCurrencyAccountService.consume(accountTradeReqDTO);
    }
}
