package com.logilong.live.id.generate.interfaces;


public interface IdGenerateRPC {

    /**
     * 获取有序id
     */
    Long getSeqId(Integer id);

    /**
     * 获取无序id
     */
    Long getUnSeqId(Integer id);
}
