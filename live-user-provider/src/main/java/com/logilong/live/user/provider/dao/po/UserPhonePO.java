package com.logilong.live.user.provider.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;


@TableName("t_user_phone")
@Data
public class UserPhonePO {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String phone;
    private Integer status;
    private Date createTime;
    private Date updateTime;
}
