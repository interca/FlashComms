package com.im.flashcomms.common.user.service;

import com.im.flashcomms.common.user.domain.entity.UserBackpack;
import com.baomidou.mybatisplus.extension.service.IService;
import com.im.flashcomms.common.user.domain.enums.IdempotentEnum;

/**
 * <p>
 * 用户背包表 服务类
 * </p>
 *
 * @author <a href="https://github.com/zongzibinbin">abin</a>
 * @since 2023-10-18
 */
public interface IUserBackpackService  {

    /**
     * 给用户发放物体
     * @param uid 用户Id
     * @param itemId 物品id
     * @param idempotentEnum  幂等类型
     * @param businessId  用户唯一标识
     */
    void acquireItem(Long uid, Long itemId, IdempotentEnum idempotentEnum,String businessId );
}
