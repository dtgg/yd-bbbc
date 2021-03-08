package com.ydqp.lottery;

import com.cfq.threadpool.support.ThreadPoolConfig;
import com.cfq.threadpool.support.fixed.FixedThreadPool;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.Executor;

public class ThreadManager {

    @Getter
    @Setter
    private Executor executor;

    private ThreadManager() {
        ThreadPoolConfig threadPoolConfig = new ThreadPoolConfig();
        threadPoolConfig.setMaxThreads(1);
        threadPoolConfig.setQueueSize(2000);
        threadPoolConfig.setThreadName("playerTask");

        FixedThreadPool fixedThreadPool = new FixedThreadPool();
        executor = fixedThreadPool.getExecutor(threadPoolConfig);
    }

    private static final ThreadManager instance = new ThreadManager();

    public static ThreadManager getInstance() {
        return instance;
    }



}
