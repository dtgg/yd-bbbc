package com.ydqp.lobby;

import com.cfq.boostStrap.IServer;
import com.cfq.boostStrap.Server;
import com.cfq.dispatch.DispatchMessage;
import com.cfq.net.websocket.ServerWebSocketChannelInitializer;
import com.ydqp.lobby.timer.PaymentTimer;

import java.util.Timer;

public class LobbyMain {

    public static void main(String[] args) {
        //payment task
        new Timer().schedule(new PaymentTimer() , 1000, 5000);
        //start server for app
        DispatchMessage serverDispatchMessage = new DispatchMessage();
        IServer iServer = new Server();
        ServerWebSocketChannelInitializer serverWebSocketChannelInitializer = new ServerWebSocketChannelInitializer(serverDispatchMessage);
        iServer.start(serverWebSocketChannelInitializer);
    }
}
