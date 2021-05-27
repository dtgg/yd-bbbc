package com.ydqp.vspoker;

import com.cfq.boostStrap.IServer;
import com.cfq.boostStrap.Server;
import com.cfq.dispatch.DispatchMessage;
import com.cfq.net.websocket.ServerWebSocketChannelInitializer;
import com.ydqp.vspoker.room.RoomManager;
import com.ydqp.vspoker.room.VsPokerRoom;
import com.ydqp.vspoker.room.play.PlayVsPokerManager;

import java.util.Timer;

public class VspokerMain {

    public static void main(String[] args){
        //启动定时器，进行room的监听 1s 一次
        //start heartBeat thread
        new Timer().schedule(new BattleTask() , 1000, 1000);
        new Timer().schedule(new GeneratorRaceTask() , 1000, 1000);

        PlayVsPokerManager.getInstance().loadFastRaceConfig();

        DispatchMessage serverDispatchMessage = new DispatchMessage();
        IServer iServer = new Server();
        ServerWebSocketChannelInitializer serverWebSocketChannelInitializer = new ServerWebSocketChannelInitializer(serverDispatchMessage);
        iServer.start(serverWebSocketChannelInitializer);
    }

}
