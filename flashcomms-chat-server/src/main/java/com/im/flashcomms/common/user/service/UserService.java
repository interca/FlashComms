package com.im.flashcomms.common.user.service;

import com.im.flashcomms.common.user.domain.entity.User;
import com.im.flashcomms.common.user.domain.vo.req.user.BlackReq;
import com.im.flashcomms.common.user.domain.vo.resp.user.BadgeResp;
import com.im.flashcomms.common.user.domain.vo.resp.user.UserInfoResp;

import java.util.List;

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


    /**
     * 用户改名
     * @param uid
     * @param name
     */
    void modifyName(Long uid, String name);


    /**
     * 返回用户徽章
     * @param uid
     * @return
     */
    List<BadgeResp> badges(Long uid);

    /**
     * 佩戴徽章
     * @param uid
     * @param itemId
     * @return
     */
    void wearingBadge(Long uid, Long itemId);

    /**
     * 拉黑用户
     * @param req
     */
    void black(BlackReq req);
}
