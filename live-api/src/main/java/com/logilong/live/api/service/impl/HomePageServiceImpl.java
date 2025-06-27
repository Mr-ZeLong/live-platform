package com.logilong.live.api.service.impl;

import org.apache.dubbo.config.annotation.DubboReference;
import com.logilong.live.api.service.IHomePageService;
import com.logilong.live.api.vo.HomePageVO;
import com.logilong.live.user.constants.UserTagsEnum;
import com.logilong.live.user.dto.UserDTO;
import com.logilong.live.user.interfaces.IUserRPC;
import com.logilong.live.user.interfaces.IUserTagRPC;
import org.springframework.stereotype.Service;


@Service
public class HomePageServiceImpl implements IHomePageService {

    @DubboReference
    private IUserRPC userRpc;
    @DubboReference
    private IUserTagRPC userTagRpc;

    @Override
    public HomePageVO initPage(Long userId) {
        UserDTO userDTO = userRpc.getByUserId(userId);
        HomePageVO homePageVO = new HomePageVO();
        if (userDTO != null) {
            homePageVO.setAvatar(userDTO.getAvatar());
            homePageVO.setUserId(userId);
            homePageVO.setNickName(userDTO.getNickName());
            //vip用户有权利开播
            homePageVO.setShowStartLivingBtn(userTagRpc.containTag(userId, UserTagsEnum.IS_VIP));
        }
        return homePageVO;
    }
}
