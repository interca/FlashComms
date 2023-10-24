package com.im.flashcomms.common.user.dao;

import com.im.flashcomms.common.user.domain.entity.UserApply;
import com.im.flashcomms.common.user.mapper.UserApplyMapper;
import com.im.flashcomms.common.user.service.IUserApplyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户申请表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/zongzibinbin">abin</a>
 * @since 2023-10-24
 */
@Service
public class UserApplyDao extends ServiceImpl<UserApplyMapper, UserApply> implements IUserApplyService {

}
