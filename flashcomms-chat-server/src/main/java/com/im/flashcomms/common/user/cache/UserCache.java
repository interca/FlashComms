package com.im.flashcomms.common.user.cache;

import com.im.flashcomms.common.user.dao.BlackDao;
import com.im.flashcomms.common.user.dao.ItemConfigDao;
import com.im.flashcomms.common.user.dao.UserDao;
import com.im.flashcomms.common.user.dao.UserRoleDao;
import com.im.flashcomms.common.user.domain.entity.Black;
import com.im.flashcomms.common.user.domain.entity.ItemConfig;
import com.im.flashcomms.common.user.domain.entity.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 权限缓存
 */
@Component
public class UserCache {



    @Autowired
    private UserRoleDao userRoleDao;

    @Autowired
    private BlackDao blackDao;

    /**
     * 获取用户角色
     * @param uid
     * @return
     */
    @Cacheable(cacheNames =  "user",key = "'roles:'+#uid")
    public Set<Long> getRoleSet(Long uid) {
        List<UserRole> userRoles = userRoleDao.listByUid(uid);
        Set<Long> collect = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toSet());
        return collect;
    }



    /**
     * 获取拉黑的列表
     */
    @Cacheable(cacheNames =  "user",key = "'blackList'")
    public Map<Integer,Set<String>> getBlackMap() {
        Map<Integer, List<Black>> collect = blackDao.list().stream().collect(Collectors.groupingBy(Black::getType));
        Map<Integer,Set<String>>result = new HashMap<>();
        collect.forEach((type,list) ->{
            result.put(type,list.stream().map(Black::getTarget).collect(Collectors.toSet()));
        });
        return result;
    }


    /**
     * 清空缓存
     * @return
     */
    @CacheEvict(cacheNames =  "user",key = "'blackList'")
    public Map<Integer,Set<String>> evictBlackMap() {
        return null;
    }
}
