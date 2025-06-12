package com.logilong.live.user.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import com.logilong.live.user.constants.UserTagsEnum;
import com.logilong.live.user.interfaces.IUserTagRpc;
import com.logilong.live.user.provider.service.IUserTagService;

/**
 * @Author idea
 * @Date: Created in 17:12 2023/5/27
 * @Description
 */
@DubboService
public class UserTagRpcImpl implements IUserTagRpc {

    @Resource
    private IUserTagService userTagService;

    @Override
    public boolean setTag(Long userId, UserTagsEnum userTagsEnum) {
        return userTagService.setTag(userId, userTagsEnum);
    }

    @Override
    public boolean cancelTag(Long userId, UserTagsEnum userTagsEnum) {
        return userTagService.cancelTag(userId, userTagsEnum);
    }

    @Override
    public boolean containTag(Long userId, UserTagsEnum userTagsEnum) {
        return userTagService.containTag(userId, userTagsEnum);
    }
}
