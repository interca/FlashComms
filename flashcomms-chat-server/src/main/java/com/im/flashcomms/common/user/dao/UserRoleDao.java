package com.im.flashcomms.common.user.dao;

import com.im.flashcomms.common.user.domain.entity.UserRole;
import com.im.flashcomms.common.user.mapper.UserRoleMapper;
import com.im.flashcomms.common.user.service.IUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户角色关系表 服务实现类
 * </p>
 */
@Service
public class UserRoleDao extends ServiceImpl<UserRoleMapper, UserRole>  {

    public List<UserRole> listByUid(Long uid) {
        return lambdaQuery()
                .eq(UserRole::getUid, uid)
                .list();
    }
}
