package com.ydqp.common;

import com.cfq.threadpool.support.ThreadPoolConfig;
import com.cfq.threadpool.support.fixed.FixedThreadPool;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.Executor;

public class ThreadManager {

    @Getter
    @Setter
    private Executor executor;

    @Getter
    @Setter
    private Executor rankExecutor;

    @Getter
    @Setter
    private Executor gradeExecutor;

    private ThreadManager() {
        ThreadPoolConfig threadPoolConfig = new ThreadPoolConfig();
        threadPoolConfig.setMaxThreads(1);
        threadPoolConfig.setQueueSize(2000);
        threadPoolConfig.setThreadName("playerTask");

        FixedThreadPool fixedThreadPool = new FixedThreadPool();
        executor = fixedThreadPool.getExecutor(threadPoolConfig);

        ThreadPoolConfig threadPoolConfig2 = new ThreadPoolConfig();
        threadPoolConfig2.setMaxThreads(1);
        threadPoolConfig2.setQueueSize(2000);
        threadPoolConfig2.setThreadName("rankTask");

        FixedThreadPool fixedThreadPool2 = new FixedThreadPool();
        rankExecutor = fixedThreadPool2.getExecutor(threadPoolConfig2);

        ThreadPoolConfig threadPoolConfig3 = new ThreadPoolConfig();
        threadPoolConfig3.setMaxThreads(1);
        threadPoolConfig3.setQueueSize(2000);
        threadPoolConfig3.setThreadName("gradeTask");

        FixedThreadPool fixedThreadPool3 = new FixedThreadPool();
        gradeExecutor = fixedThreadPool3.getExecutor(threadPoolConfig3);

    }

    private static final ThreadManager instance = new ThreadManager();

    public static ThreadManager getInstance() {
        return instance;
    }



}
