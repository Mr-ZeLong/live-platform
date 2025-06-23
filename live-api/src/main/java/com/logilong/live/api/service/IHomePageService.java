package com.logilong.live.api.service;

import com.logilong.live.api.vo.HomePageVO;


public interface IHomePageService {


    /**
     * 初始化页面获取的信息
     *
     * @param userId
     * @return
     */
    HomePageVO initPage(Long userId);


}
