package com.logilong.live.api.service.impl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import com.logilong.live.account.interfaces.IAccountTokenRPC;
import com.logilong.live.api.error.ApiErrorEnum;
import com.logilong.live.api.service.IUserLoginService;
import com.logilong.live.api.vo.UserLoginVO;
import com.logilong.live.common.interfaces.utils.ConvertBeanUtils;
import com.logilong.live.common.interfaces.vo.WebResponseVO;
import com.logilong.live.msg.dto.MsgCheckDTO;
import com.logilong.live.msg.enums.MsgSendResultEnum;
import com.logilong.live.msg.interfaces.ISmsRPC;
import com.logilong.live.user.dto.UserLoginDTO;
import com.logilong.live.user.interfaces.IUserPhoneRPC;
import com.logilong.live.web.starter.error.ErrorAssert;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@Slf4j
public class UserLoginServiceImpl implements IUserLoginService {

    private static final String PHONE_REG = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$";

    @DubboReference
    private ISmsRPC smsRPC;
    @DubboReference
    private IUserPhoneRPC userPhoneRPC;
    @DubboReference
    private IAccountTokenRPC accountTokenRPC;
    @Value("${web.domain}")
    private String webDomain; // live.com

    @Override
    public WebResponseVO sendLoginCode(String phone) {
        ErrorAssert.isNotBlank(phone, ApiErrorEnum.PHONE_IS_EMPTY);
        ErrorAssert.isTure(Pattern.matches(PHONE_REG, phone), ApiErrorEnum.PHONE_IN_VALID);
        MsgSendResultEnum msgSendResultEnum = smsRPC.sendLoginCode(phone);
        if (msgSendResultEnum == MsgSendResultEnum.SEND_SUCCESS) {
            return WebResponseVO.success();
        }
        return WebResponseVO.sysError("短信发送太频繁，请稍后再试");
    }

    @Override
    public WebResponseVO login(String phone, Integer code, HttpServletResponse response) {
        ErrorAssert.isNotBlank(phone, ApiErrorEnum.PHONE_IS_EMPTY);
        ErrorAssert.isTure(Pattern.matches(PHONE_REG, phone), ApiErrorEnum.PHONE_IN_VALID);
        ErrorAssert.isTure(code != null && code > 1000, ApiErrorEnum.SMS_CODE_ERROR);
        MsgCheckDTO msgCheckDTO = smsRPC.checkLoginCode(phone, code);
        if (!msgCheckDTO.isCheckStatus()) {
            return WebResponseVO.bizError(msgCheckDTO.getDesc());
        }
        //验证码校验通过
        UserLoginDTO userLoginDTO = userPhoneRPC.login(phone);
        ErrorAssert.isTure(userLoginDTO.isLoginSuccess(),ApiErrorEnum.USER_LOGIN_ERROR);


        String token = accountTokenRPC.createAndSaveLoginToken(userLoginDTO.getUserId());
        Cookie cookie = new Cookie("livetk", token);
        //http://app.live.com/html/qiyu_live_list_room.html
        //http://api.live.com/live/api/userLogin/sendLoginCode
        // 用于指定Cookie的有效域名范围，控制哪些域名可以访问该Cookie
        cookie.setDomain(webDomain);
        log.info("cookie的domain:{}", cookie.getDomain());
        cookie.setPath("/");

        //cookie有效期，一般他的默认单位是秒，设置为30天
        cookie.setMaxAge(30 * 24 * 3600);

        //加上它，不然web浏览器不会将cookie自动记录下
        response.addCookie(cookie);
        log.info("用户登录成功，用户id:{}", userLoginDTO.getUserId());
        return WebResponseVO.success(ConvertBeanUtils.convert(userLoginDTO, UserLoginVO.class));
    }

}
