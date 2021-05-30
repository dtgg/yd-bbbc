package com.ydqp.vspoker;


import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VsPokerMaintainTimer extends TimerTask {

    private static ExecutorService executor;

    public VsPokerMaintainTimer() {
        executor = Executors.newFixedThreadPool(1);
    }

    @Override
    public void run() {
        executor.execute(new VsPokerMaintainTask());
    }
}
