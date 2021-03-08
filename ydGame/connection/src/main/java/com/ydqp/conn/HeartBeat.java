package com.ydqp.conn;


import com.cfq.clientBoostStrap.ClientStart;
import com.cfq.connection.ManagerClient;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.ydqp.conn.dispatch.ConnDispatchMessage;

import java.util.TimerTask;

public class HeartBeat extends TimerTask {

    private final static Logger logger = LoggerFactory.getLogger(HeartBeat.class);

    @Override
    public void run() {
        try {
            if (ManagerClient.getInstance().getClientChannels().size() == 0) {
                //no sever connect, do connect all
                ConnDispatchMessage connDispatchMessage = new ConnDispatchMessage();
                ClientStart clientStart = new ClientStart();
                clientStart.connect(connDispatchMessage);
                logger.info("heartbeat is start connection all server code success!");
            } else if (!ManagerClient.getInstance().isChannelNumEq()) {
                //reconnect
                ConnDispatchMessage connDispatchMessage = new ConnDispatchMessage();
                ClientStart clientStart = new ClientStart();
                clientStart.reConnect(connDispatchMessage, ManagerClient.getInstance().getDisConnectServer());
                logger.info("heartbeat is start reconnection server code success! code = {} ", ManagerClient.getInstance().getDisConnectServer().toString());
                ManagerClient.getInstance().clearDisConnectionServer();

            }
        } catch (Exception e) {
            logger.error("reconnect error msg = {}" , e.getMessage());
        }


    }
}
