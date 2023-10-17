package com.im.flashcomms.common.common.Thread;

import lombok.extern.slf4j.Slf4j;

/**
 * 线程异常捕获其器
 */
@Slf4j
public class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.error("Exception in thread",e);
    }
}
