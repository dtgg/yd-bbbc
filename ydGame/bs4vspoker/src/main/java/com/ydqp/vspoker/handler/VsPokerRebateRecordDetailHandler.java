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
import com.ydqp.common.entity.VsRacePromote;
import com.ydqp.common.receiveProtoMsg.vspoker.VsPokerRebateRecordDetail;
import com.ydqp.common.sendProtoMsg.vspoker.SVsPokerRebateRecordData;
import com.ydqp.common.sendProtoMsg.vspoker.SVsPokerRebateRecordDetail;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@ServerHandler(module = "vsPoker", command = 7000024)
public class VsPokerRebateRecordDetailHandler implements IServerHandler {

    private static final Logger logger = LoggerFactory.getLogger(VsPokerRebateRecordDetailHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        VsPokerRebateRecordDetail recordDetail = (VsPokerRebateRecordDetail) abstartParaseMessage;

        SVsPokerRebateRecordDetail sVsPokerRebateRecord = new SVsPokerRebateRecordDetail();

        PlayerData playerData = PlayerCache.getInstance().getPlayer(recordDetail.getConnId());
        if (playerData == null) {
            logger.error("player is not true");
            return;
        }

        List<VsRacePromote> playerPromoteRaces = VsRacePromoteDao.getInstance().findByPlayerIdAndRaceId(recordDetail.getPlayerId(), recordDetail.getRaceId());
        if (CollectionUtils.isEmpty(playerPromoteRaces)) {
            logger.info("no rebate records, playerId:{}", recordDetail.getPlayerId());
        } else {
            List<SVsPokerRebateRecordData> rebateRecordData = new ArrayList<>();
            for (VsRacePromote playerPromoteRace : playerPromoteRaces) {
                SVsPokerRebateRecordData recordData = new SVsPokerRebateRecordData();
                recordData.setPlayerName(playerPromoteRace.getPlayerName());
                recordData.setCreateTime(playerPromoteRace.getCreateTime());
                recordData.setOrderId(playerPromoteRace.getOrderId());
                recordData.setRebate(playerPromoteRace.getFee());
                recordData.setRaceId(playerPromoteRace.getRaceId());
                rebateRecordData.add(recordData);
            }
            sVsPokerRebateRecord.setRecords(rebateRecordData);
            iSession.sendMessageByID(sVsPokerRebateRecord, recordDetail.getConnId());
        }
    }
}
