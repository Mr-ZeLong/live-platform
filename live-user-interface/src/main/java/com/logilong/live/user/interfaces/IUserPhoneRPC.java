package com.logilong.live.user.interfaces;

import com.logilong.live.user.dto.UserLoginDTO;
import com.logilong.live.user.dto.UserPhoneDTO;

import java.util.List;

/**
 * 用户手机相关RPC
 */
public interface IUserPhoneRPC {

    /**
     * 用户登录（底层会进行手机号的注册）
     */
    UserLoginDTO login(String phone);

    /**
     * 根据手机信息查询相关用户信息
     */
    UserPhoneDTO queryByPhone(String phone);

    /**
     * 根据用户id查询手机相关信息
     */
    List<UserPhoneDTO> queryByUserId(Long userId);

    boolean insertUserPhone(String phone);
}
