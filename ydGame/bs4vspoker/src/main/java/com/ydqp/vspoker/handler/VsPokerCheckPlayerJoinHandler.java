package com.ydqp.vspoker.handler;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.dao.PlayerDao;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.receiveProtoMsg.vspoker.VsPokerCheckPlayerJoin;
import com.ydqp.common.sendProtoMsg.vspoker.SVsPokerCheckPlayerJoin;
import com.ydqp.vspoker.room.play.PlayVsPokerManager;
import com.ydqp.vspoker.room.play.VsPokerBasePlay;

@ServerHandler(module = "vsPoker", command = 7000025)
public class VsPokerCheckPlayerJoinHandler implements IServerHandler {
    private static final Logger logger = LoggerFactory.getLogger(VsPokerCheckPlayerJoinHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        VsPokerCheckPlayerJoin checkPlayerJoin = (VsPokerCheckPlayerJoin) abstartParaseMessage;

        PlayerData playerData = PlayerCache.getInstance().getPlayer(abstartParaseMessage.getConnId());
        if (playerData == null) {
            logger.error("player is not true");
            return;
        }

        VsPokerBasePlay vsPokerBasePlay = PlayVsPokerManager.getInstance().getPlayObject(2, checkPlayerJoin.getBasePoint(), 0);

        boolean joinEnabled = vsPokerBasePlay.checkRoomId(playerData, playerData.getPlayerId());

        SVsPokerCheckPlayerJoin pokerCheckPlayerJoin = new SVsPokerCheckPlayerJoin();
        pokerCheckPlayerJoin.setJoinEnabled(joinEnabled);
        iSession.sendMessageByID(pokerCheckPlayerJoin, checkPlayerJoin.getConnId());
    }
}
