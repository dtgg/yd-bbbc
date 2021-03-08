package com.ydqp.lobby.timer;

import com.ydqp.lobby.task.PaymentTask;

import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PaymentTimer extends TimerTask {

    private static ExecutorService executor;

    public PaymentTimer() {
        executor = Executors.newFixedThreadPool(1);
    }

    @Override
    public void run() {
        executor.execute(new PaymentTask());
    }
}
