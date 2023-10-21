package com.im.flashcomms.common.user.dao;

import com.im.flashcomms.common.user.domain.entity.UserRole;
import com.im.flashcomms.common.user.mapper.UserRoleMapper;
import com.im.flashcomms.common.user.service.IUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户角色关系表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/zongzibinbin">abin</a>
 * @since 2023-10-21
 */
@Service
public class UserRoleDao extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

}
