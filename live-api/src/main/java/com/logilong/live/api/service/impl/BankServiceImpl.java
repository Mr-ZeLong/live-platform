package com.logilong.live.api.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import com.logilong.live.api.service.IBankService;
import com.logilong.live.api.vo.req.PayProductReqVO;
import com.logilong.live.api.vo.resp.PayProductItemVO;
import com.logilong.live.api.vo.resp.PayProductRespVO;
import com.logilong.live.api.vo.resp.PayProductVO;
import com.logilong.live.bank.constants.OrderStatusEnum;
import com.logilong.live.bank.dto.PayOrderDTO;
import com.logilong.live.bank.dto.PayProductDTO;
import com.logilong.live.bank.interfaces.IPayOrderRPC;
import com.logilong.live.bank.interfaces.IPayProductRPC;
import com.logilong.live.bank.interfaces.ILiveCurrencyAccountRPC;
import com.logilong.live.bank.constants.PaySourceEnum;
import com.logilong.live.web.starter.context.LiveRequestContext;
import com.logilong.live.web.starter.error.BizBaseErrorEnum;
import com.logilong.live.web.starter.error.ErrorAssert;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class BankServiceImpl implements IBankService {

    @DubboReference
    private IPayProductRPC payProductRPC;
    @DubboReference
    private ILiveCurrencyAccountRPC liveCurrencyAccountRPC;
    @DubboReference
    private IPayOrderRPC payOrderRPC;
    @Resource
    private RestTemplate restTemplate;
    @Value("${live.wxNotify}")
    private String payNotifyUrl;

    @Override
    public PayProductVO products(Integer type) {
        List<PayProductDTO> payProductDTOS = payProductRPC.products(type);
        List<PayProductItemVO> itemList = new ArrayList<>();
        for (PayProductDTO payProductDTO : payProductDTOS) {
            PayProductItemVO itemVO = new PayProductItemVO();
            itemVO.setName(payProductDTO.getName());
            itemVO.setId(payProductDTO.getId());
            itemVO.setCoinNum(JSON.parseObject(payProductDTO.getExtra()).getInteger("coin"));
            itemList.add(itemVO);
        }
        PayProductVO payProductVO = new PayProductVO();
        payProductVO.setPayProductItemVOList(itemList);
        payProductVO.setCurrentBalance(Optional.ofNullable(liveCurrencyAccountRPC.getBalance(LiveRequestContext.getUserId())).orElse(0));
        return payProductVO;
    }

    @Override
    public PayProductRespVO payProduct(PayProductReqVO payProductReqVO) {
        //参数校验
        ErrorAssert.isTure(payProductReqVO != null && payProductReqVO.getProductId() != null && payProductReqVO.getPaySource() != null, BizBaseErrorEnum.PARAM_ERROR);
        ErrorAssert.isNotNull(PaySourceEnum.find(payProductReqVO.getPaySource()), BizBaseErrorEnum.PARAM_ERROR);
        PayProductDTO payProductDTO = payProductRPC.getByProductId(payProductReqVO.getProductId());
        ErrorAssert.isNotNull(payProductDTO, BizBaseErrorEnum.PARAM_ERROR);

        //插入一条订单，待支付状态
        PayOrderDTO payOrderDTO = new PayOrderDTO();
        payOrderDTO.setProductId(payProductReqVO.getProductId());
        payOrderDTO.setUserId(LiveRequestContext.getUserId());
        payOrderDTO.setSource(payProductReqVO.getPaySource());
        payOrderDTO.setPayChannel(payProductReqVO.getPayChannel());
        String orderId = payOrderRPC.insertOne(payOrderDTO);

        //更新订单为支付中状态
        payOrderRPC.updateOrderStatus(orderId, OrderStatusEnum.PAYING.getCode());
        PayProductRespVO payProductRespVO = new PayProductRespVO();
        payProductRespVO.setOrderId(orderId);

        //todo 远程http请求 resttemplate -> 支付回调接口
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("orderId", orderId);
        jsonObject.put("userId", LiveRequestContext.getUserId());
        jsonObject.put("bizCode", 10001);
        HashMap<String,String> paramMap = new HashMap<>();
        paramMap.put("param",jsonObject.toJSONString());
        ResponseEntity<String> resultEntity = restTemplate.postForEntity( payNotifyUrl + "?param={param}", null, String.class,paramMap);
        System.out.println(resultEntity.getBody());
        return payProductRespVO;
    }
}
