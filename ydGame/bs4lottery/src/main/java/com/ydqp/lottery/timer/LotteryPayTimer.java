package com.ydqp.lottery.timer;


import com.ydqp.lottery.task.LotteryPayTask;

import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 赔付
 */
public class LotteryPayTimer extends TimerTask {
    private static ExecutorService executor;

    public LotteryPayTimer() {
        executor = Executors.newFixedThreadPool(1);
    }

    @Override
    public void run() {
        executor.execute(new LotteryPayTask());
    }
}
