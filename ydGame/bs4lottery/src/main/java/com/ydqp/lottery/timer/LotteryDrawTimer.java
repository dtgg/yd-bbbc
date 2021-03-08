package com.ydqp.lottery.timer;


import com.ydqp.lottery.task.LotteryDrawTask;

import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 根据下注情况开奖
 */
public class LotteryDrawTimer extends TimerTask {

    private static ExecutorService executor;

    public LotteryDrawTimer() {
        executor = Executors.newFixedThreadPool(1);
    }

    @Override
    public void run() {
        executor.execute(new LotteryDrawTask());
    }
}
