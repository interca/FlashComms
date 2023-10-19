package com.im.flashcomms.common.user.service;

import com.im.flashcomms.common.user.domain.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.im.flashcomms.common.user.domain.vo.resp.UserInfoResp;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author <a href="https://github.com/zongzibinbin">abin</a>
 * @since 2023-10-07
 */
public interface UserService {

    /**
     * 用户注册
     * @param insert
     * @return
     */
    Long  register(User insert);


    /**
     * 获取用户信息
     * @param uid
     * @return
     */
    UserInfoResp getUserInfo(Long uid);
}
