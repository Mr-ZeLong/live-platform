package com.logilong.live.api.service;

import jakarta.servlet.http.HttpServletResponse;
import com.logilong.live.common.interfaces.vo.WebResponseVO;


public interface IUserLoginService {

    /**
     * 发送登录验证码
     */
    WebResponseVO sendLoginCode(String phone);

    /**
     * 手机号+验证码登录
     */
    WebResponseVO login(String phone, Integer code, HttpServletResponse response);
}
