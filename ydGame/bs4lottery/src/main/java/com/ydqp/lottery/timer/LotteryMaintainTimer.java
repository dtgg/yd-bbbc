package com.ydqp.lottery.timer;

import com.ydqp.lottery.task.LotteryMaintainTask;

import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LotteryMaintainTimer extends TimerTask {

    private static ExecutorService executor;

    public LotteryMaintainTimer() {
        executor = Executors.newFixedThreadPool(1);
    }

    @Override
    public void run() {
        executor.execute(new LotteryMaintainTask());
    }
}
