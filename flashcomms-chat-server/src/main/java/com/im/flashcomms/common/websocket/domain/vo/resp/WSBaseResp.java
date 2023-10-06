package com.im.flashcomms.common.websocket.domain.vo.resp;

import lombok.Data;

@Data
public class WSBaseResp<T> {
    Integer type;

    T data;
}
