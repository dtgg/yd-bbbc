package com.ydqp.vspoker;

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
    private Executor promoteExecutor;

    @Getter
    @Setter
    private Executor gradeExecutor;

    private ThreadManager() {
        ThreadPoolConfig threadPoolConfig = new ThreadPoolConfig();
        threadPoolConfig.setMaxThreads(1);
        threadPoolConfig.setQueueSize(2000);
        threadPoolConfig.setThreadName("rankTask");

        FixedThreadPool fixedThreadPool = new FixedThreadPool();
        executor = fixedThreadPool.getExecutor(threadPoolConfig);

        ThreadPoolConfig threadPoolConfig2 = new ThreadPoolConfig();
        threadPoolConfig2.setMaxThreads(1);
        threadPoolConfig2.setQueueSize(2000);
        threadPoolConfig2.setThreadName("promoteTask");

        FixedThreadPool fixedThreadPool2 = new FixedThreadPool();
        promoteExecutor = fixedThreadPool2.getExecutor(threadPoolConfig2);
    }

    private static final ThreadManager instance = new ThreadManager();

    public static ThreadManager getInstance() {
        return instance;
    }



}
