package com.ydqp.vspoker.handler;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.dao.PlayerDao;
import com.ydqp.common.dao.VsRacePromoteDao;
import com.ydqp.common.dao.lottery.PlayerPromoteDao;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.entity.*;
import com.ydqp.common.lottery.player.ManageLottery;
import com.ydqp.common.receiveProtoMsg.vspoker.VsPokerRaceJoin;
import com.ydqp.common.receiveProtoMsg.vspoker.VsPokerZjRaceJoin;
import com.ydqp.common.sendProtoMsg.CoinPointSuccess;
import com.ydqp.common.sendProtoMsg.vspoker.SVsPlayerRace;
import com.ydqp.common.sendProtoMsg.vspoker.SVsPokerFastRaceJoin;
import com.ydqp.common.sendProtoMsg.vspoker.SVsPokerRaceJoin;
import com.ydqp.common.sendProtoMsg.vspoker.SVsPokerZjRaceJoin;
import com.ydqp.common.service.PlayerService;
import com.ydqp.common.utils.CommonUtils;
import com.ydqp.vspoker.ThreadManager;
import com.ydqp.vspoker.cache.RankingCache;
import com.ydqp.vspoker.dao.VsPlayerRaceDao;
import com.ydqp.vspoker.dao.VsPokerDao;
import com.ydqp.vspoker.room.play.PlayVsPokerManager;
import com.ydqp.vspoker.room.play.VsPokerBasePlay;
import org.apache.commons.collections.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ServerHandler(command = 7000031, module = "vsPoker")
public class VsPokerZjRaceJoinHandler implements IServerHandler {
    private static final Logger logger = LoggerFactory.getLogger(VsPokerZjRaceJoinHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        VsPokerZjRaceJoin vsPokerZjRaceJoin = (VsPokerZjRaceJoin) abstartParaseMessage;

        SVsPokerZjRaceJoin pokerZjRaceJoin = new SVsPokerZjRaceJoin();
        if (ManageLottery.getInstance().getCloseServer() == 1) {
            pokerZjRaceJoin.setEnterRoomSuccess(false);
            pokerZjRaceJoin.setMessage("The server is under maintenance");
            iSession.sendMessageByID(pokerZjRaceJoin, vsPokerZjRaceJoin.getConnId());
            return;
        }

        PlayerData playerData = PlayerCache.getInstance().getPlayer(vsPokerZjRaceJoin.getConnId());
        if (playerData == null) {
            logger.error("player is not true");
            return;
        }
        Player player = PlayerDao.getInstance().queryById(playerData.getPlayerId());
        if (player.getZjPoint() < 10) {
            logger.error("you have not enough balance");
            pokerZjRaceJoin.setEnterRoomSuccess(false);
            pokerZjRaceJoin.setMessage("you have not enough balance");
            iSession.sendMessageByID(pokerZjRaceJoin, vsPokerZjRaceJoin.getConnId());
            return;
        }

        VsPokerBasePlay vsPokerBasePlay = PlayVsPokerManager.getInstance().getPlayObject(3, 0, 0);
        vsPokerBasePlay.enterRoom(playerData, iSession, playerData.getRoomId());
        vsPokerBasePlay.putPlayerMap(player.getId(), player.getRoomId());

        pokerZjRaceJoin.setEnterRoomSuccess(true);
        iSession.sendMessageByID(pokerZjRaceJoin, vsPokerZjRaceJoin.getConnId());
    }
}
