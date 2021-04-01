package com.ydqp.vspoker;


import com.ydqp.common.poker.room.Room;
import com.ydqp.vspoker.room.RoomManager;

import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BattleTask extends TimerTask {

    private static ExecutorService executor;
    private static ExecutorService delRoomExe;

    public BattleTask () {
        executor = Executors.newFixedThreadPool(3);
        delRoomExe = Executors.newFixedThreadPool(1);
    }


    @Override
    public void run() {

        for(Map.Entry<Integer, Room> entry : RoomManager.getInstance().getVsPokerRoomMapMap().entrySet()) {
            Room room = entry.getValue();

            //@TODO 判断下比赛结束后，把房间从列表删除
            executor.execute(new MonitorTask(room));

        }

    }
}
