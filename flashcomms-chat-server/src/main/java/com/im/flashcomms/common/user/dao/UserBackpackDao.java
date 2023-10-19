package com.im.flashcomms.common.user.dao;

import com.im.flashcomms.common.common.domain.enums.YesOrNoEnum;
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
public class UserBackpackDao extends ServiceImpl<UserBackpackMapper, UserBackpack>  {

    /**
     * 获取剩余的改名卡
     * @param uid
     * @param itemId
     */
    public Integer getCountByValidItemId(Long uid, Long itemId) {
        Integer count = lambdaQuery()
                .eq(UserBackpack::getUid, uid)
                .eq(UserBackpack::getItemId, itemId)
                .eq(UserBackpack::getStatus, YesOrNoEnum.NO.getStatus())
                .count();
        return count;
    }

    public UserBackpack getFirstValidItem(Long uid, Long itemId) {
        UserBackpack one = lambdaQuery()
                .eq(UserBackpack::getUid, uid)
                .eq(UserBackpack::getItemId, itemId)
                .eq(UserBackpack::getStatus, YesOrNoEnum.NO.getStatus())
                .orderByAsc(UserBackpack::getId)
                .one();
        return one;
    }

    public boolean userItem(UserBackpack firstValidItem) {
        return lambdaUpdate()
                .eq(UserBackpack::getId, firstValidItem.getId())
                .eq(UserBackpack::getStatus, YesOrNoEnum.NO.getStatus())
                .set(UserBackpack::getStatus, YesOrNoEnum.YES.getStatus()).update();
    }
}
