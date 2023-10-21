package com.im.flashcomms.common.user.dao;

import com.im.flashcomms.common.user.domain.entity.Role;
import com.im.flashcomms.common.user.mapper.RoleMapper;
import com.im.flashcomms.common.user.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/zongzibinbin">abin</a>
 * @since 2023-10-21
 */
@Service
public class RoleDao extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}
