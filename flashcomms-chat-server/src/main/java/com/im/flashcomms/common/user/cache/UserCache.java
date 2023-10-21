package com.im.flashcomms.common.user.cache;

import com.im.flashcomms.common.user.dao.ItemConfigDao;
import com.im.flashcomms.common.user.dao.UserRoleDao;
import com.im.flashcomms.common.user.domain.entity.ItemConfig;
import com.im.flashcomms.common.user.domain.entity.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 权限缓存
 */
@Component
public class UserCache {



    @Autowired
    private UserRoleDao userRoleDao;

    @Cacheable(cacheNames =  "user",key = "'roles:'+#uid")
    public Set<Long> getRoleSet(Long uid) {
        List<UserRole> userRoles = userRoleDao.listByUid(uid);
        Set<Long> collect = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toSet());
        return collect;
    }
}
