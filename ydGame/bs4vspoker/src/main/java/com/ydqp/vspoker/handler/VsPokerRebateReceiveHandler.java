package com.ydqp.vspoker.handler;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.dao.VsRacePromoteDao;
import com.ydqp.common.dao.VsRebateRecordDao;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.entity.VsRacePromote;
import com.ydqp.common.entity.VsRebateRecord;
import com.ydqp.common.receiveProtoMsg.vspoker.VsPokerRebateReceive;
import com.ydqp.common.sendProtoMsg.CoinPointSuccess;
import com.ydqp.common.sendProtoMsg.vspoker.SVsPokerRebateReceive;
import com.ydqp.common.service.PlayerService;
import com.ydqp.common.utils.CommonUtils;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@ServerHandler(module = "vsPoker", command = 7000021)
public class VsPokerRebateReceiveHandler implements IServerHandler {

    private static final Logger logger = LoggerFactory.getLogger(VsPokerRebateRecordHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        VsPokerRebateReceive rebateReceive = (VsPokerRebateReceive) abstartParaseMessage;

        List<VsRacePromote> vsRacePromotes = VsRacePromoteDao.getInstance().findById(rebateReceive.getPlayerId());

        SVsPokerRebateReceive sVsPokerRebateReceive = new SVsPokerRebateReceive();

        PlayerData playerData = PlayerCache.getInstance().getPlayer(rebateReceive.getConnId());
        if (playerData == null) {
            logger.error("player is not true");
            return;
        }

        if (CollectionUtils.isEmpty(vsRacePromotes)) {
            logger.info("No rewards to claim, playerId:{}", rebateReceive.getPlayerId());
            sVsPokerRebateReceive.setSuccess(false);
            sVsPokerRebateReceive.setMessage("No rewards to claim");
            iSession.sendMessageByID(sVsPokerRebateReceive, rebateReceive.getConnId());
            return;
        }

        float rebate = 0;
        List<Integer> ids = new ArrayList<>();
        for (VsRacePromote vsRacePromote : vsRacePromotes) {
            rebate += vsRacePromote.getFee();
            ids.add(vsRacePromote.getId());
        }
        logger.info("领取赛事返利, playerId:{}, rebate:{}, orginZj:{}", rebateReceive.getPlayerId(), rebate, playerData.getZjPoint());

        VsRacePromoteDao.getInstance().updateStatus(CommonUtils.inString(ids));

        playerData.setZjPoint(playerData.getZjPoint() + rebate);
        PlayerCache.getInstance().addPlayer(rebateReceive.getConnId(), playerData);
        PlayerService.getInstance().updatePlayerZjPoint(rebate, rebateReceive.getPlayerId());

        sVsPokerRebateReceive.setSuccess(true);
        sVsPokerRebateReceive.setMessage("Successful reward");
        iSession.sendMessageByID(sVsPokerRebateReceive, rebateReceive.getConnId());

        CoinPointSuccess coinPointSuccess = new CoinPointSuccess();
        coinPointSuccess.setPlayerId(rebateReceive.getPlayerId());
        coinPointSuccess.setCoinPoint(playerData.getZjPoint());
        iSession.sendMessageByID(coinPointSuccess, rebateReceive.getConnId());

        VsRebateRecord vsRebateRecord = new VsRebateRecord();
        vsRebateRecord.setPlayerId(rebateReceive.getPlayerId());
        vsRebateRecord.setPlayerName(playerData.getPlayerName());
        vsRebateRecord.setNickname(playerData.getNickName());
        vsRebateRecord.setRebate(rebate);
        vsRebateRecord.setCreateTime(new Long(System.currentTimeMillis() / 1000).intValue());
        vsRebateRecord.setAppId(playerData.getAppId());
        vsRebateRecord.setKfId(playerData.getKfId());
        VsRebateRecordDao.getInstance().insert(vsRebateRecord.getParameterMap());
    }
}
