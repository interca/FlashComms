package com.im.flashcomms.common.common.domain.dto;

import lombok.Data;

/**
 * 存请求中的用户信息
 */
@Data
public class RequestInfo {
    private Long uid;

    private String ip;
}
