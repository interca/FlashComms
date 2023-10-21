package com.im.flashcomms.common.user.dao;

import com.im.flashcomms.common.user.domain.entity.Black;
import com.im.flashcomms.common.user.mapper.BlackMapper;
import com.im.flashcomms.common.user.service.IBlackService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 黑名单 服务实现类
 * </p>
 */
@Service
public class BlackDao extends ServiceImpl<BlackMapper, Black>  {

}
