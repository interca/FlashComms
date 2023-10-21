package com.im.flashcomms.common.user.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description: 角色枚举
 * Author: <a href="https://github.com/zongzibinbin">abin</a>
 * Date: 2023-03-19
 */
@AllArgsConstructor
@Getter
public enum RoleEnum {

    ADMIN(1L,"超级管理员"),

    CHAT_MANAGER(2L,"闪讯群聊管理员");




    private final Long  type;

    private final String desc;

    private static Map<Long, RoleEnum> cache;

    static {
        cache = Arrays.stream(RoleEnum.values()).collect(Collectors.toMap(RoleEnum::getType,Function.identity()));
    }

    public static RoleEnum of(Long type) {
        return cache.get(type);
    }

}
