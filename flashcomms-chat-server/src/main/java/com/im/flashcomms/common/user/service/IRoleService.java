package com.im.flashcomms.common.user.service;

import com.im.flashcomms.common.user.domain.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.im.flashcomms.common.user.domain.enums.RoleEnum;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author <a href="https://github.com/zongzibinbin">abin</a>
 * @since 2023-10-21
 */
public interface IRoleService {

    /**
     * 判断用户是否拥有某个权限
     * @param uid
     * @param roleEnum
     * @return
     */
    boolean hasPower(Long uid, RoleEnum roleEnum);
}
