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
import com.ydqp.common.receiveProtoMsg.vspoker.VsPokerFastRaceJoin;
import com.ydqp.common.sendProtoMsg.vspoker.SVsPokerFastRaceJoin;
import com.ydqp.vspoker.room.play.PlayVsPokerManager;
import com.ydqp.vspoker.room.play.VsPokerBasePlay;

@ServerHandler(module = "vsPoker", command = 7000026)
public class VsPokerFastRaceJoinHandler implements IServerHandler {
    private static final Logger logger = LoggerFactory.getLogger(VsPokerFastRaceJoinHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        VsPokerFastRaceJoin fastRaceJoin = (VsPokerFastRaceJoin) abstartParaseMessage;
        // 1、客户端传入 basePoint, playerId
        // 2、根据playerId 获取player， player中保存 raceId
        PlayerData playerData = PlayerCache.getInstance().getPlayer(abstartParaseMessage.getConnId());
        if (playerData == null) {
            logger.error("player is not true");
            return;
        }

        SVsPokerFastRaceJoin pokerFastRaceJoin = new SVsPokerFastRaceJoin();

        if (playerData.getZjPoint() < fastRaceJoin.getBasePoint()) {
            logger.error("Insufficient balance");
            pokerFastRaceJoin.setEnterRoomSuccess(false);
            pokerFastRaceJoin.setMessage("Insufficient balance");
            iSession.sendMessageByID(pokerFastRaceJoin, fastRaceJoin.getConnId());
            return;
        }

        playerData.setZjPoint(playerData.getZjPoint() - fastRaceJoin.getBasePoint());
        PlayerCache.getInstance().addPlayer(abstartParaseMessage.getConnId(), playerData);
        PlayerDao.getInstance().updatePlayerZjPoint(-fastRaceJoin.getBasePoint(), playerData.getPlayerId());

        VsPokerBasePlay vsPokerBasePlay = PlayVsPokerManager.getInstance().getPlayObject(2, fastRaceJoin.getBasePoint(), 0);
        vsPokerBasePlay.enterRoom(playerData, iSession, playerData.getRoomId());

        pokerFastRaceJoin.setEnterRoomSuccess(true);
        iSession.sendMessageByID(pokerFastRaceJoin, fastRaceJoin.getConnId());
    }
}
