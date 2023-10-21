package com.im.flashcomms.common.user.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description:黑名单枚举
 * Date: 2023-03-19
 */
@AllArgsConstructor
@Getter
public enum BlackTypeEnum {

    UID(1,"UID"),

    IP(2,"IP");

    private final Integer type;

    private final String desc;

    private static Map<Integer, BlackTypeEnum> cache;

    static {
        cache = Arrays.stream(BlackTypeEnum.values()).collect(Collectors.toMap(BlackTypeEnum::getType,Function.identity()));
    }

    public static BlackTypeEnum of(Long type) {
        return cache.get(type);
    }

}
