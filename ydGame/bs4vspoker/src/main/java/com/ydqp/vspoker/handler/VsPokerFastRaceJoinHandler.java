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
import com.ydqp.common.entity.Player;
import com.ydqp.common.lottery.player.ManageLottery;
import com.ydqp.common.receiveProtoMsg.vspoker.VsPokerFastRaceJoin;
import com.ydqp.common.sendProtoMsg.CoinPointSuccess;
import com.ydqp.common.sendProtoMsg.SRemainTickets;
import com.ydqp.common.sendProtoMsg.vspoker.SVsPokerFastRaceJoin;
import com.ydqp.vspoker.room.play.PlayVsPokerManager;
import com.ydqp.vspoker.room.play.VsPokerBasePlay;

@ServerHandler(module = "vsPoker", command = 7000026)
public class VsPokerFastRaceJoinHandler implements IServerHandler {
    private static final Logger logger = LoggerFactory.getLogger(VsPokerFastRaceJoinHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        VsPokerFastRaceJoin fastRaceJoin = (VsPokerFastRaceJoin) abstartParaseMessage;

        SVsPokerFastRaceJoin pokerFastRaceJoin = new SVsPokerFastRaceJoin();
        if (ManageLottery.getInstance().getCloseServer() == 1) {
            pokerFastRaceJoin.setEnterRoomSuccess(false);
            pokerFastRaceJoin.setMessage("The server is under maintenance");
            iSession.sendMessageByID(pokerFastRaceJoin, fastRaceJoin.getConnId());
            return;
        }

        PlayerData player = PlayerCache.getInstance().getPlayer(abstartParaseMessage.getConnId());
        if (player == null) {
            logger.error("player is not true");
            return;
        }
        Player playerData = PlayerDao.getInstance().queryById(player.getPlayerId());

        if (fastRaceJoin.getBasePoint() == 500 || fastRaceJoin.getBasePoint() == 1000) {
            logger.error("not open basePoint:{}, playerId:{}", fastRaceJoin.getBasePoint(), playerData.getZjPoint());
            pokerFastRaceJoin.setEnterRoomSuccess(false);
            pokerFastRaceJoin.setMessage("This session is not yet open, please be patient");
            iSession.sendMessageByID(pokerFastRaceJoin, fastRaceJoin.getConnId());
            return;
        }

        if (fastRaceJoin.getBasePoint() <= 0) {
            logger.error("not exist basePoint:{}, playerId:{}", fastRaceJoin.getBasePoint(), playerData.getZjPoint());
            pokerFastRaceJoin.setEnterRoomSuccess(false);
            pokerFastRaceJoin.setMessage("Insufficient balance");
            iSession.sendMessageByID(pokerFastRaceJoin, fastRaceJoin.getConnId());
            return;
        }

        if (fastRaceJoin.getBasePoint() == 100 && playerData.getTickets() > 0) {
            PlayerDao.getInstance().updatePlayerTickets(playerData.getId());

            SRemainTickets sRemainTickets = new SRemainTickets();
            sRemainTickets.setPlayerId(playerData.getId());
            sRemainTickets.setTickets(playerData.getTickets());
            iSession.sendMessageByID(sRemainTickets, abstartParaseMessage.getConnId());
        } else {
            if (playerData.getZjPoint() < fastRaceJoin.getBasePoint()) {
                logger.error("Insufficient balance");
                pokerFastRaceJoin.setEnterRoomSuccess(false);
                pokerFastRaceJoin.setMessage("Insufficient balance");
                iSession.sendMessageByID(pokerFastRaceJoin, fastRaceJoin.getConnId());
                return;
            }

            PlayerDao.getInstance().updatePlayerZjPoint(-fastRaceJoin.getBasePoint(), playerData.getId());

            CoinPointSuccess coinPointSuccess = new CoinPointSuccess();
            coinPointSuccess.setPlayerId(playerData.getId());
            coinPointSuccess.setCoinPoint(playerData.getZjPoint() - fastRaceJoin.getBasePoint());
            iSession.sendMessageByID(coinPointSuccess, fastRaceJoin.getConnId());
        }
//        PlayerCache.getInstance().addPlayer(abstartParaseMessage.getConnId(), playerData);

        VsPokerBasePlay vsPokerBasePlay = PlayVsPokerManager.getInstance().getPlayObject(2, fastRaceJoin.getBasePoint(), 0);
        vsPokerBasePlay.enterRoom(player, iSession, playerData.getRoomId());
        vsPokerBasePlay.putPlayerMap(playerData.getId(), player.getRoomId());

        pokerFastRaceJoin.setEnterRoomSuccess(true);
        iSession.sendMessageByID(pokerFastRaceJoin, fastRaceJoin.getConnId());
    }
}
