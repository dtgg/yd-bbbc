package com.ydqp.vspoker.handler;

import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.dao.PlayerDao;
import com.ydqp.common.data.PlayerData;
import com.ydqp.vspoker.room.play.PlayVsPokerManager;
import com.ydqp.vspoker.room.play.VsPokerBasePlay;

public class VsPokerFastRaceJoinHandler implements IServerHandler {
    private static final Logger logger = LoggerFactory.getLogger(VsPokerFastRaceJoinHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        int basePoint = 10;
        // 1、客户端传入 basePoint, playerId
        // 2、根据playerId 获取player， player中保存 raceId
        PlayerData playerData = PlayerCache.getInstance().getPlayer(abstartParaseMessage.getConnId());
        if (playerData == null) {
            logger.error("player is not true");
            return;
        }

        if (playerData.getZjPoint() < basePoint) {
            logger.error("Insufficient balance");
            return;
        }

        playerData.setZjPoint(playerData.getZjPoint() - basePoint);
        PlayerCache.getInstance().addPlayer(abstartParaseMessage.getConnId(), playerData);

//        PlayerDao.getInstance().updatePlayerZjPoint();



        VsPokerBasePlay vsPokerBasePlay = PlayVsPokerManager.getInstance().getPlayObject(2, basePoint, 0);

        vsPokerBasePlay.enterRoom(playerData, iSession, playerData.getRoomId());

    }
}
