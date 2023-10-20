package com.im.flashcomms.common.user.service.Impl;

import com.im.flashcomms.common.common.domain.enums.YesOrNoEnum;
import com.im.flashcomms.common.common.exception.BusinessException;
import com.im.flashcomms.common.common.exception.CommonErrorEnum;
import com.im.flashcomms.common.common.service.LockService;
import com.im.flashcomms.common.user.dao.UserBackpackDao;
import com.im.flashcomms.common.user.domain.entity.UserBackpack;
import com.im.flashcomms.common.user.domain.enums.IdempotentEnum;
import com.im.flashcomms.common.user.service.IUserBackpackService;
import lombok.SneakyThrows;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserBackpackServiceImpl implements IUserBackpackService {

    @Autowired
    private UserBackpackDao userBackpackDao;

    @Autowired
    private LockService lockService;

    @SneakyThrows
    @Override
    public void acquireItem(Long uid, Long itemId, IdempotentEnum idempotentEnum, String businessId) {
        String idempotent = getIdempotent(itemId,idempotentEnum,businessId);
        lockService.executeWithLock("acquireItem" + idempotent,()->{
            //判断幂等是否存在
            UserBackpack userBackpack = userBackpackDao.getByIdempotent(idempotent);
            if(Objects.nonNull(userBackpack)){
                return;
            }
            //业务检查
            //发放物品
            userBackpack = userBackpack.builder()
                    .uid(uid)
                    .itemId(itemId)
                    .status(YesOrNoEnum.NO.getStatus())
                    .idempotent(idempotent)
                    .build();
            userBackpackDao.save(userBackpack);
        });

    }


    /**
     * 获取幂等号
     * @param itemId
     * @param idempotentEnum
     * @param businessId
     * @return
     */
    private String getIdempotent(Long itemId, IdempotentEnum idempotentEnum, String businessId) {
        return String.format("%d_%d_%s",itemId,idempotentEnum.getType(),businessId);
    }
}
