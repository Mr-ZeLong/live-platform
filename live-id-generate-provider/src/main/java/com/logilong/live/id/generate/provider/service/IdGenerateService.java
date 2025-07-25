package com.logilong.live.id.generate.provider.service;

public interface IdGenerateService {

    /**
     * 获取有序id
     */
    Long getSeqId(Integer id);

    /**
     * 获取无序id
     */
    Long getUnSeqId(Integer id);
}
