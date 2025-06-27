package com.logilong.live.user.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import com.logilong.live.user.dto.UserLoginDTO;
import com.logilong.live.user.dto.UserPhoneDTO;
import com.logilong.live.user.interfaces.IUserPhoneRPC;
import com.logilong.live.user.provider.service.IUserPhoneService;

import java.util.List;


@DubboService
public class UserPhoneRPCImpl implements IUserPhoneRPC {

    @Resource
    private IUserPhoneService userPhoneService;

    @Override
    public UserLoginDTO login(String phone) {
        return userPhoneService.login(phone);
    }

    @Override
    public UserPhoneDTO queryByPhone(String phone) {
        return userPhoneService.queryByPhone(phone);
    }

    @Override
    public List<UserPhoneDTO> queryByUserId(Long userId) {
        return userPhoneService.queryByUserId(userId);
    }

    @Override
    public boolean insertUserPhone(String phone) {
        userPhoneService.insertUserPhone(phone, null);
        return true;
    }
}
