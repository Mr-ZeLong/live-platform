package com.logilong.live.api.vo.req;


import lombok.Data;

@Data
public class PayProductReqVO {

    /**
     * 产品id
     */
    private Long productId;

    /**
     * 支付来源 (直播间，个人中心，聊天页面，第三方宣传页面，广告弹窗引导)
     * @see com.logilong.live.bank.constants.PaySourceEnum
     */
    private Integer paySource;

    /**
     * 支付渠道
     * @see com.logilong.live.bank.constants.PayChannelEnum
     */
    private Integer payChannel;
}
