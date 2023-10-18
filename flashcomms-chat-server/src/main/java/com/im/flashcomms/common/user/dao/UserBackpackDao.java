package com.im.flashcomms.common.user.dao;

import com.im.flashcomms.common.user.domain.entity.UserBackpack;
import com.im.flashcomms.common.user.mapper.UserBackpackMapper;
import com.im.flashcomms.common.user.service.IUserBackpackService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户背包表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/zongzibinbin">abin</a>
 * @since 2023-10-18
 */
@Service
public class UserBackpackDao extends ServiceImpl<UserBackpackMapper, UserBackpack> implements IUserBackpackService {

}
