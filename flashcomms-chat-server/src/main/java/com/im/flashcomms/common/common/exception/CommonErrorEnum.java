package com.im.flashcomms.common.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CommonErrorEnum {

    PARAM_INVALID(-2,"参数校验失败"),

    SYSTEM_ERROR(-1,"系统出小差了");


    private final Integer code;

    private final  String msg;
}
