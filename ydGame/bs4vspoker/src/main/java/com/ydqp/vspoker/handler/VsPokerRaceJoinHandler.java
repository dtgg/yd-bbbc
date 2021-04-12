package com.ydqp.vspoker.handler;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.dao.PlayerDao;
import com.ydqp.common.dao.lottery.PlayerPromoteDao;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.entity.*;
import com.ydqp.common.receiveProtoMsg.vspoker.VsPokerRaceJoin;
import com.ydqp.common.sendProtoMsg.CoinPointSuccess;
import com.ydqp.common.sendProtoMsg.vspoker.SVsPlayerRace;
import com.ydqp.common.sendProtoMsg.vspoker.SVsPokerRaceJoin;
import com.ydqp.common.service.PlayerService;
import com.ydqp.common.utils.CommonUtils;
import com.ydqp.vspoker.ThreadManager;
import com.ydqp.vspoker.dao.PlayerPromoteRaceDao;
import com.ydqp.vspoker.dao.VsPlayerRaceDao;
import com.ydqp.vspoker.dao.VsPokerDao;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@ServerHandler(command = 7000014, module = "vsPoker")
public class VsPokerRaceJoinHandler implements IServerHandler {
    private static final Logger logger = LoggerFactory.getLogger(VsPokerRaceJoinHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        VsPokerRaceJoin vsPokerRaceJoin = (VsPokerRaceJoin) abstartParaseMessage;

        PlayerData playerData = PlayerCache.getInstance().getPlayer(vsPokerRaceJoin.getConnId());
        if (playerData == null) {
            logger.error("player is not true");
            return;
        }

        SVsPokerRaceJoin sVsPokerRaceJoin = new SVsPokerRaceJoin();

        VsRace race = VsPokerDao.getInstance().getRaceById(vsPokerRaceJoin.getRaceId());
        if (race == null) {
            logger.error("The event does not exist, playerId:{}, raceId:{}", vsPokerRaceJoin.getPlayerId(), vsPokerRaceJoin.getRaceId());
            sVsPokerRaceJoin.setSuccess(false);
            sVsPokerRaceJoin.setMessage("The event does not exist");
            iSession.sendMessageByID(sVsPokerRaceJoin, vsPokerRaceJoin.getConnId());
            return;
        }

        if (race.getRaceType() == 1) {
            List<VsRace> vsRaces = VsPokerDao.getInstance().getVsRaces(2, 1);
            if (CollectionUtils.isNotEmpty(vsRaces)) {
                List<Integer> raceIds = vsRaces.stream().map(VsRace::getId).collect(Collectors.toList());
                List<VsPlayerRace> playerRaceList = VsPlayerRaceDao.getInstance().getPlayerRaceRunning(playerData.getPlayerId(), CommonUtils.inString(raceIds));
                if (CollectionUtils.isNotEmpty(playerRaceList)) {
                    logger.error("Join free entry can only be continued after the previous game is over, playerId:{}, raceId:{}", vsPokerRaceJoin.getPlayerId(), vsPokerRaceJoin.getRaceId());
                    sVsPokerRaceJoin.setSuccess(false);
                    sVsPokerRaceJoin.setMessage("Join free entry can only be continued after the previous game is over");
                    iSession.sendMessageByID(sVsPokerRaceJoin, vsPokerRaceJoin.getConnId());
                    return;
                }
            }
        }

        if (race.getCurPlayerNum() >= race.getMaxPlayerNum()) {
            logger.error("The number of joined has reached the upper limit, playerId:{}, raceId:{}", vsPokerRaceJoin.getPlayerId(), vsPokerRaceJoin.getRaceId());
            sVsPokerRaceJoin.setSuccess(false);
            sVsPokerRaceJoin.setMessage("The number of joined has reached the upper limit");
            iSession.sendMessageByID(sVsPokerRaceJoin, vsPokerRaceJoin.getConnId());
            return;
        }

        int nowTime = new Long(System.currentTimeMillis() / 1000L).intValue();
        if (race.getBeginTime() < nowTime) {
            logger.error("Join time has ended, playerId:{}, raceId:{}", vsPokerRaceJoin.getPlayerId(), vsPokerRaceJoin.getRaceId());
            sVsPokerRaceJoin.setSuccess(false);
            sVsPokerRaceJoin.setMessage("Join time has ended");
            iSession.sendMessageByID(sVsPokerRaceJoin, vsPokerRaceJoin.getConnId());
            return;
        }

        VsPlayerRace playerRaces = VsPlayerRaceDao.getInstance().getPlayerRaceByPlayerIdAndRaceId(
                vsPokerRaceJoin.getPlayerId(), vsPokerRaceJoin.getRaceId());
        if (playerRaces != null) {
            logger.error("You has been joined, playerId:{}, raceId:{}", vsPokerRaceJoin.getPlayerId(), vsPokerRaceJoin.getRaceId());
            sVsPokerRaceJoin.setSuccess(false);
            sVsPokerRaceJoin.setMessage("You has been joined");
            iSession.sendMessageByID(sVsPokerRaceJoin, vsPokerRaceJoin.getConnId());
            return;
        }

        Player player = PlayerService.getInstance().queryByPlayerId(playerData.getPlayerId());
        if (player.getZjPoint() < race.getBasePoint()) {
            logger.error("Insufficient balance, playerId:{}, raceId:{}", vsPokerRaceJoin.getPlayerId(), vsPokerRaceJoin.getRaceId());
            sVsPokerRaceJoin.setSuccess(false);
            sVsPokerRaceJoin.setMessage("Not enough money");
            iSession.sendMessageByID(sVsPokerRaceJoin, vsPokerRaceJoin.getConnId());
            return;
        }
        if (race.getBasePoint() > 0) {
            PlayerDao.getInstance().updatePlayerZjPoint(-race.getBasePoint(), playerData.getPlayerId());
            playerData.setZjPoint(playerData.getZjPoint() - race.getBasePoint());
            PlayerCache.getInstance().addPlayer(vsPokerRaceJoin.getConnId(), playerData);

            CoinPointSuccess coinPointSuccess = new CoinPointSuccess();
            coinPointSuccess.setPlayerId(playerData.getPlayerId());
            coinPointSuccess.setCoinPoint(playerData.getZjPoint());
            iSession.sendMessageByID(coinPointSuccess, vsPokerRaceJoin.getConnId());
        }

        VsPlayerRace vsPlayerRace = new VsPlayerRace();
        vsPlayerRace.setPlayerId(vsPokerRaceJoin.getPlayerId());
        vsPlayerRace.setRaceId(vsPokerRaceJoin.getRaceId());
        vsPlayerRace.setRaceType(race.getRaceType());
        vsPlayerRace.setBasePoint(race.getBasePoint());
        vsPlayerRace.setRank(0);
        vsPlayerRace.setCreateTime(nowTime);
        vsPlayerRace.setAppId(player.getAppId());
        vsPlayerRace.setKfId(player.getKfId());
        vsPlayerRace.setIsVir(player.getIsVir());
        VsPlayerRaceDao.getInstance().insert(vsPlayerRace.getParameterMap());

        VsPokerDao.getInstance().updateCurPlayerNum(vsPokerRaceJoin.getRaceId());

        SVsPlayerRace sVsPlayerRace = new SVsPlayerRace();
        sVsPlayerRace.setPlayerId(vsPokerRaceJoin.getPlayerId());
        sVsPlayerRace.setRaceId(vsPokerRaceJoin.getRaceId());
        sVsPlayerRace.setCurPlayerNum(race.getCurPlayerNum() + 1);

        sVsPokerRaceJoin.setSuccess(true);
        sVsPokerRaceJoin.setSVsPlayerRace(sVsPlayerRace);
        iSession.sendMessageByID(sVsPokerRaceJoin, vsPokerRaceJoin.getConnId());

        ThreadManager.getInstance().getPromoteExecutor().execute(() -> playerPromoteRaceNum(playerData.getPlayerId(), race.getId()));
    }

    private void playerPromoteRaceNum(long playerId, int raceId) {
        //tuiguang
        PlayerPromote playerPromote = PlayerPromoteDao.getInstance().findByPlayerId(playerId);
        if (playerPromote.getSuperiorId() != null && playerPromote.getSuperiorId() != 0) {
            PlayerPromoteDao.getInstance().updateRaceNum(playerPromote.getSuperiorId());
        }

//        PlayerPromoteRace playerPromoteRace = PlayerPromoteRaceDao.getInstance().findByPlayerIdAndRaceId(playerId, raceId);
//        if (playerPromoteRace == null) {
//            PlayerPromoteRace promoteRace = new PlayerPromoteRace();
//            promoteRace.setPlayerId(playerId);
//            promoteRace.setRaceId(raceId);
//            promoteRace.setRaceNum(1);
//            PlayerPromoteRaceDao.getInstance().insert(promoteRace.getParameterMap());
//        } else {
//            PlayerPromoteRaceDao.getInstance().updateRaceNum(playerId, raceId);
//        }
    }
}
