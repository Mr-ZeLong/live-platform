package com.logilong.live.api.vo.resp;

import lombok.Data;

/**
 * 返回给客户端使用的im地址和认证token
 */
@Data
public class ImConfigVO {

    private String token;
    private String wsImServerAddress;
    private String tcpImServerAddress;

}
