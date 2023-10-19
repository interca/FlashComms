package com.im.flashcomms.common.user.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.flashcomms.common.user.domain.entity.User;
import com.im.flashcomms.common.user.mapper.UserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/zongzibinbin">abin</a>
 * @since 2023-10-07
 */
@Service
public class UserDao extends ServiceImpl<UserMapper, User>{

    public User getByOpenId(String openId) {
        return lambdaQuery()
                .eq(User::getOpenId,openId)
                .one();
    }

    public User getByName(String name) {
        User one = lambdaQuery().eq(User::getName, name).one();
        return one;
    }

    public void modifyName(Long uid, String name) {
        lambdaUpdate()
                .eq(User::getId,uid)
                .set(User::getName,name)
                .update();
    }

    public void wearingBadge(Long uid, Long itemId) {
        lambdaUpdate()
                .eq(User::getId,uid)
                .eq(User::getItemId,itemId)
                .update();
    }
}
