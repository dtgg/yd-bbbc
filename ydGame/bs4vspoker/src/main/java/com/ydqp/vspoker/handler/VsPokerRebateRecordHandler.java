package com.ydqp.vspoker.handler;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.dao.VsRacePromoteDao;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.data.PlayerRebateRecord;
import com.ydqp.common.receiveProtoMsg.vspoker.VsPokerRebateRecord;
import com.ydqp.common.sendProtoMsg.vspoker.SVsPokerRebateRecord;
import com.ydqp.common.sendProtoMsg.vspoker.SVsPokerRebateRecordData;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@ServerHandler(module = "vsPoker", command = 7000022)
public class VsPokerRebateRecordHandler implements IServerHandler {

    private static final Logger logger = LoggerFactory.getLogger(VsPokerRebateRecordHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        VsPokerRebateRecord rebateRecord = (VsPokerRebateRecord) abstartParaseMessage;

        SVsPokerRebateRecord sVsPokerRebateRecord = new SVsPokerRebateRecord();

        PlayerData playerData = PlayerCache.getInstance().getPlayer(rebateRecord.getConnId());
        if (playerData == null) {
            logger.error("player is not true");
            return;
        }

//        List<VsRacePromote> playerPromoteRaces = VsRacePromoteDao.getInstance().findByPlayerId(rebateRecord.getPlayerId());
//        if (CollectionUtils.isEmpty(playerPromoteRaces)) {
//            logger.info("no rebate records, playerId:{}", rebateRecord.getPlayerId());
//        } else {
//            List<SVsPokerRebateRecordData> rebateRecordData = new ArrayList<>();
//            for (VsRacePromote playerPromoteRace : playerPromoteRaces) {
//                SVsPokerRebateRecordData recordData = new SVsPokerRebateRecordData();
//                recordData.setPlayerName(playerPromoteRace.getPlayerName());
//                recordData.setCreateTime(playerPromoteRace.getCreateTime());
//                recordData.setOrderId(playerPromoteRace.getOrderId());
//                recordData.setRebate(playerPromoteRace.getFee());
//                rebateRecordData.add(recordData);
//            }
//            sVsPokerRebateRecord.setRecords(rebateRecordData);
//            iSession.sendMessageByID(sVsPokerRebateRecord, rebateRecord.getConnId());
//        }

        List<PlayerRebateRecord> playerPromoteRaces = VsRacePromoteDao.getInstance().findByPlayerId(rebateRecord.getPlayerId());
        if (CollectionUtils.isEmpty(playerPromoteRaces)) {
            logger.info("no rebate records, playerId:{}", rebateRecord.getPlayerId());
        } else {
            List<SVsPokerRebateRecordData> rebateRecordData = new ArrayList<>();
            for (PlayerRebateRecord playerPromoteRace : playerPromoteRaces) {
                SVsPokerRebateRecordData recordData = new SVsPokerRebateRecordData();
                recordData.setRaceId(playerPromoteRace.getRaceId());
                recordData.setCreateTime(playerPromoteRace.getBeginTime());
                recordData.setRebate(playerPromoteRace.getRebate());
                rebateRecordData.add(recordData);
            }
            sVsPokerRebateRecord.setRecords(rebateRecordData);
            iSession.sendMessageByID(sVsPokerRebateRecord, rebateRecord.getConnId());
        }
    }
}
