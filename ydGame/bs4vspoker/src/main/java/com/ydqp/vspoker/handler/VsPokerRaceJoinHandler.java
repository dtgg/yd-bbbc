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
import com.ydqp.common.dao.VsRacePromoteDao;
import com.ydqp.vspoker.cache.RankingCache;
import com.ydqp.vspoker.dao.VsPlayerRaceDao;
import com.ydqp.vspoker.dao.VsPokerDao;
import org.apache.commons.collections.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
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

            if (race.getIsPermission() == 1) {
                //需要参加报名赛才可以包名的免费赛
               if (!RankingCache.getInstance().exitRaceJoin(playerData.getPlayerId())) {
                   sVsPokerRaceJoin.setSuccess(false);
                   sVsPokerRaceJoin.setMessage("You need to participate in today’s registration match before you have the right to participate in the free race");
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

        ThreadManager.getInstance().getPromoteExecutor().execute(() -> playerPromoteRaceNum(playerData, race, nowTime));

        if (race.getRaceType() == 2) {
            RankingCache.getInstance().setRaceJoin(playerData.getPlayerId(), race.getBeginTime());
        }
    }

    private void playerPromoteRaceNum(PlayerData playerData, VsRace vsRace, int nowTime) {
        //tuiguang
        PlayerPromote playerPromote = PlayerPromoteDao.getInstance().findByPlayerId(playerData.getPlayerId());
        if (playerPromote == null) {
            logger.error("无法获取用户的推广信息，playerId = {}", playerData.getPlayerId());
            return;
        }
        if (playerPromote.getSuperiorId() != null && playerPromote.getSuperiorId() != 0) {
            PlayerPromoteDao.getInstance().updateRaceNum(playerPromote.getSuperiorId());

            Player player = PlayerService.getInstance().queryByPlayerId(playerPromote.getSuperiorId());
            if (player.getIsRebate() == 1 && vsRace.getRaceType() == 2) {
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                String time = format.format(new Date(nowTime * 1000L));

                float rebate = vsRace.getBasePoint() * 0.01F;

                logger.info("用户可以获得返利: playerId:{}, raceId:{}, rebate:{}", player.getId(), vsRace.getId(), rebate);

                VsRacePromote promoteRace = new VsRacePromote();
                promoteRace.setPlayerId(playerPromote.getSuperiorId());
                promoteRace.setSubId(playerData.getPlayerId());
                promoteRace.setPlayerName(playerData.getPlayerName());
                promoteRace.setNickname(playerData.getNickName());
                promoteRace.setRaceId(vsRace.getId());
                promoteRace.setOrderId(time);
                promoteRace.setSignFee(vsRace.getBasePoint());
                promoteRace.setFee(rebate);
                promoteRace.setStatus(0);
                promoteRace.setCreateTime(new Long(System.currentTimeMillis() / 1000).intValue());
                promoteRace.setBeginTime(vsRace.getBeginTime());
                promoteRace.setAppId(playerData.getAppId());
                promoteRace.setKfId(playerData.getKfId());
                VsRacePromoteDao.getInstance().insert(promoteRace.getParameterMap());
            }
        }
    }
}
