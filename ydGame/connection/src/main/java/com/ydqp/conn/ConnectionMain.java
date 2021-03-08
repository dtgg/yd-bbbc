package com.ydqp.conn;

import com.cfq.boostStrap.IServer;
import com.cfq.boostStrap.Server;
import com.cfq.clientBoostStrap.ClientStart;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.net.websocket.ServerWebSocketChannelInitializer;
import com.ydqp.conn.dispatch.ConnDispatchMessage;
import com.ydqp.conn.dispatch.ServerDispatchMessage;
import java.util.Timer;

public class ConnectionMain {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionMain.class);

    public static void main(String[] args) {

        try {
            //connect bs
            ConnDispatchMessage connDispatchMessage = new ConnDispatchMessage();
            ClientStart clientStart = new ClientStart();
            clientStart.connect(connDispatchMessage);
        } catch (Exception e) {
            logger.error("connect to bs error!");
        }

        //start heartBeat thread
        new Timer().schedule( new HeartBeat(), 5000, 1000);

        //start server for app
        ServerDispatchMessage serverDispatchMessage = new ServerDispatchMessage();
        IServer iServer = new Server();
        ServerWebSocketChannelInitializer serverWebSocketChannelInitializer = new ServerWebSocketChannelInitializer(serverDispatchMessage);
        iServer.start(serverWebSocketChannelInitializer);


    }

}
