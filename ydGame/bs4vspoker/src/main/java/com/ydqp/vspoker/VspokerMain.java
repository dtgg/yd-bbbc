package com.ydqp.vspoker;

import com.cfq.boostStrap.IServer;
import com.cfq.boostStrap.Server;
import com.cfq.dispatch.DispatchMessage;
import com.cfq.net.websocket.ServerWebSocketChannelInitializer;

public class VspokerMain {

    public static void main(String[] args){
        DispatchMessage serverDispatchMessage = new DispatchMessage();
        IServer iServer = new Server();
        ServerWebSocketChannelInitializer serverWebSocketChannelInitializer = new ServerWebSocketChannelInitializer(serverDispatchMessage);
        iServer.start(serverWebSocketChannelInitializer);
    }

}
