package com.ydqp.lottery.timer;


import com.ydqp.lottery.task.LotteryGenerateTask;

import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 定时开启新一期彩票
 */
public class LotteryGenerateTimer extends TimerTask {

    private static ExecutorService executor;

    public LotteryGenerateTimer() {
        executor = Executors.newFixedThreadPool(1);
    }

    @Override
    public void run() {
        executor.execute(new LotteryGenerateTask());
    }
}
