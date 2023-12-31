package com.im.flashcomms.common.common.config;

import com.im.flashcomms.common.common.Thread.MyThreadFactory;
import com.im.flashcomms.transaction.annotation.SecureInvokeConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 自定义线程池
 */
@Configuration
@EnableAsync
public class ThreadPoolConfig implements AsyncConfigurer, SecureInvokeConfigurer {
    /**
     * 项目共用线程池
     */
    public static final String FLASHCOMMS_EXECUTOR = "flashcommsExecutor";


    /**
     * websocket通信线程池
     */
    public static final String WS_EXECUTOR = "websocketExecutor";



    @Override
    public Executor getAsyncExecutor() {
        return mallchatExecutor();
    }

    @Override
    public Executor getSecureInvokeExecutor() {
        return mallchatExecutor();
    }



    @Bean(FLASHCOMMS_EXECUTOR)
    @Primary
    public ThreadPoolTaskExecutor mallchatExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //线程池优雅停机
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(200);
        executor.setThreadNamePrefix("flashcomms-executor-");
        executor.setThreadFactory(new MyThreadFactory(executor));
        //拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());//满了调用线程执行，认为重要任务
        executor.initialize();
        return executor;
    }


    @Bean(WS_EXECUTOR)
    @Primary
    public ThreadPoolTaskExecutor websocketExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //线程池优雅停机
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setCorePoolSize(16);
        executor.setMaxPoolSize(16);
        executor.setQueueCapacity(1000);
        executor.setThreadNamePrefix("ws-executor-");
        executor.setThreadFactory(new MyThreadFactory(executor));
        //拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());//满了直接丢掉
        executor.initialize();
        return executor;
    }
}