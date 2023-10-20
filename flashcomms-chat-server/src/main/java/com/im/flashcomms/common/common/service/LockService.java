package com.im.flashcomms.common.common.service;

import com.im.flashcomms.common.common.exception.BusinessException;
import com.im.flashcomms.common.common.exception.CommonErrorEnum;
import lombok.SneakyThrows;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 分布式锁
 */
@Component
public class LockService{

    @Autowired
    private RedissonClient redissonClient;

    @SneakyThrows
    public <T> T executeWithLock(String key, int waitTime, TimeUnit timeUnit, Supplier<T> supplier) {
        RLock lock = redissonClient.getLock(key);
        boolean success = lock.tryLock(waitTime, timeUnit);
        if(!success){
           throw  new BusinessException(CommonErrorEnum.LOCK_LIMIT.getCode(), CommonErrorEnum.LOCK_LIMIT.getMsg());
        }
        try {
            return supplier.get();
        }finally {
            lock.unlock();
        }
    }

    @SneakyThrows
    public <T> T executeWithLock(String key,  Runnable runnable) throws InterruptedException {
        return executeWithLock(key, -1, TimeUnit.MILLISECONDS, () ->{
            runnable.run();
            return null;
        });
    }
}
