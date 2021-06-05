package com.ydqp.vspoker.handler;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.receiveProtoMsg.vspoker.VsPokerTrend;
import com.ydqp.common.sendProtoMsg.vspoker.SVsPokerTrend;
import com.ydqp.vspoker.room.RoomManager;
import com.ydqp.vspoker.room.VsPokerRoom;

import java.util.ArrayList;
import java.util.List;

@ServerHandler(command = 7000014, module = "vsPoker")
public class VsPokerTrendHandler implements IServerHandler {

    private static final Logger logger = LoggerFactory.getLogger(VsPokerTrendHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        VsPokerTrend vsPokerTrend = (VsPokerTrend) abstartParaseMessage;

        PlayerData playerData = PlayerCache.getInstance().getPlayer(vsPokerTrend.getConnId());
        if (playerData == null) {
            logger.error("player is not true");
            return;
        }

        VsPokerRoom vsPokerRoom = RoomManager.getInstance().getRoom(playerData.getRoomId());
        List<Boolean> list = new ArrayList<>();
        if (vsPokerRoom != null && vsPokerRoom.getTrendMap().get(vsPokerTrend.getPlayType()) != null) {
            list = vsPokerRoom.getTrendMap().get(vsPokerTrend.getPlayType());
        }

        SVsPokerTrend sVsPokerTrend = new SVsPokerTrend();
        sVsPokerTrend.setTrendList(list);
        iSession.sendMessageByID(sVsPokerTrend, vsPokerTrend.getConnId());
    }
}
