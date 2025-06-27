package com.logilong.live.id.generate.provider.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import com.logilong.live.id.generate.interfaces.IdGenerateRPC;
import com.logilong.live.id.generate.provider.service.IdGenerateService;


@DubboService
public class IdGenerateRPCImpl implements IdGenerateRPC {

    @Resource
    private IdGenerateService idGenerateService;

    @Override
    public Long getSeqId(Integer id) {
        return idGenerateService.getSeqId(id);
    }

    @Override
    public Long getUnSeqId(Integer id) {
        return idGenerateService.getUnSeqId(id);
    }
}
