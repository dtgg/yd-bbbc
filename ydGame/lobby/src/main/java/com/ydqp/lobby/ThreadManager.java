package com.ydqp.lobby;

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

    @Getter
    @Setter
    private Executor statUploadExecutor;

    private ThreadManager() {
        ThreadPoolConfig threadPoolConfig = new ThreadPoolConfig();
        threadPoolConfig.setMaxThreads(1);
        threadPoolConfig.setQueueSize(2000);
        threadPoolConfig.setThreadName("playerTask");

        FixedThreadPool fixedThreadPool = new FixedThreadPool();
        executor = fixedThreadPool.getExecutor(threadPoolConfig);

        ThreadPoolConfig threadPoolConfig4 = new ThreadPoolConfig();
        threadPoolConfig4.setMaxThreads(2);
        threadPoolConfig4.setQueueSize(2000);
        threadPoolConfig4.setThreadName("statUpload");

        FixedThreadPool fixedThreadPool4 = new FixedThreadPool();
        statUploadExecutor = fixedThreadPool4.getExecutor(threadPoolConfig4);
    }

    private static final ThreadManager instance = new ThreadManager();

    public static ThreadManager getInstance() {
        return instance;
    }



}
