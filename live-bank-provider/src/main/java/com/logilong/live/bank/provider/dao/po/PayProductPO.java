package com.logilong.live.bank.provider.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 送礼物服务（用户的账户需要有一定的余额）通过一个接口，返回可以购买的产品列表, 映射我们的每个虚拟商品
 */
@Data
@TableName("t_pay_product")
public class PayProductPO {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private Integer price;
    private String extra;
    private Integer type;
    private Integer validStatus;
    private Date createTime;
    private Date updateTime;
}
