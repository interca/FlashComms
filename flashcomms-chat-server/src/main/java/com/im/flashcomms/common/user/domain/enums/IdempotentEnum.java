package com.im.flashcomms.common.user.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 幂等枚举
 */
@AllArgsConstructor
@Getter
public enum IdempotentEnum {

    UID(1,"uid"),

    MSG_ID(2,"消息id");


    private final Integer type;

    private final String desc;
}
