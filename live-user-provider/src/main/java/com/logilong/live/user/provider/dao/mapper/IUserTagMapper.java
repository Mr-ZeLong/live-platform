package com.logilong.live.user.provider.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import com.logilong.live.user.provider.dao.po.UserTagPO;


@Mapper
public interface IUserTagMapper extends BaseMapper<UserTagPO> {

    /**
     * 使用或的思路来设置标签，只能允许第一次设置成功
     */
    @Update("update t_user_tag set ${fieldName}=${fieldName} | #{tag} where user_id=#{userId} and ${fieldName} & #{tag}=0")
    int setTag(Long userId, String fieldName, long tag);

    /**
     * 使用先取反再按位与的思路来取消标签，只能允许第一次删除成功
     * 注意：不推荐使用异或运算符来取消标签，因为异或运算依赖原有标签的状态，
     *      如果原有的标签位为0，不仅删除失败，还会将对应的标签位设置为1
     */
    @Update("update t_user_tag set ${fieldName}=${fieldName} &~ #{tag} where user_id=#{userId} and ${fieldName} & #{tag} = #{tag}")
    int cancelTag(Long userId, String fieldName, long tag);
}
