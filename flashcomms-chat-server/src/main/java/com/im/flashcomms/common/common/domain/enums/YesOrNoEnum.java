package com.im.flashcomms.common.common.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 全局枚举
 */
@AllArgsConstructor
@Getter
public enum YesOrNoEnum {

    NO(0,"否"),
    YES(1,"是");

    private final Integer status;

    private final String desc;
}
