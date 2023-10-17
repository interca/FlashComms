package com.im.flashcomms.common.common.Thread;

import lombok.AllArgsConstructor;

import java.util.concurrent.ThreadFactory;

/**
 * 自定义线程工厂
 */
@AllArgsConstructor
public class MyThreadFactory implements ThreadFactory {

    private static  final  MyUncaughtExceptionHandler MY_UNCAUGHT_EXCEPTION_HANDLER = new MyUncaughtExceptionHandler();

    private ThreadFactory original;

    @Override
    public Thread newThread(Runnable r) {
        //执行spring的线程自己的创建逻辑
        Thread thread = original.newThread(r);
        //额外装饰的逻辑
        thread.setUncaughtExceptionHandler(MY_UNCAUGHT_EXCEPTION_HANDLER);//异常捕获
        return thread;
    }
}