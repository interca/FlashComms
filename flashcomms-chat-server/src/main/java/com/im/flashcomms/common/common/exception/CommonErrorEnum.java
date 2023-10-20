package com.im.flashcomms.common.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CommonErrorEnum {

    BUSINESS_ERROR(0,"业务错误"),

    SYSTEM_ERROR(-1,"系统出小差了"),
    PARAM_INVALID(-2,"参数校验失败"),

    LOCK_LIMIT(-3,"请求太频繁");


    private final Integer code;

    private final  String msg;
}
