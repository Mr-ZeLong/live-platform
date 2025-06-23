package com.logilong.live.api.controller;

import jakarta.annotation.Resource;
import com.logilong.live.api.service.IHomePageService;
import com.logilong.live.api.vo.HomePageVO;
import com.logilong.live.common.interfaces.vo.WebResponseVO;
import com.logilong.live.web.starter.context.LiveRequestContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/home")
public class HomePageController {

    @Resource
    private IHomePageService homePageService;

    @PostMapping("/initPage")
    public WebResponseVO initPage() {
        Long userId = LiveRequestContext.getUserId();
        HomePageVO homePageVO = new HomePageVO();
        homePageVO.setLoginStatus(false);
        if (userId != null) {
            homePageVO = homePageService.initPage(userId);
            homePageVO.setLoginStatus(true);
        }
        return WebResponseVO.success(homePageVO);
    }
}
