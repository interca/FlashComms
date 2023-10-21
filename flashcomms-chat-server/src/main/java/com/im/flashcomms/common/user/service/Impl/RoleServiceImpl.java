package com.im.flashcomms.common.user.service.Impl;

import com.im.flashcomms.common.user.cache.UserCache;
import com.im.flashcomms.common.user.domain.enums.RoleEnum;
import com.im.flashcomms.common.user.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RoleServiceImpl implements IRoleService {
    @Autowired
    private UserCache userCache;

    @Override
    public boolean hasPower(Long uid, RoleEnum roleEnum) {
        Set<Long> roleSet = userCache.getRoleSet(uid);
        return isAdmin(roleSet) || roleSet.contains(roleEnum.getType());
    }

    private boolean isAdmin(Set<Long>roleSet){
        return roleSet.contains(RoleEnum.ADMIN.getType());
    }
}
