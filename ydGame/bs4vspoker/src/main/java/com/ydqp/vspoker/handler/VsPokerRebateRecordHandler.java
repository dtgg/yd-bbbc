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
import com.ydqp.common.receiveProtoMsg.vspoker.VsPokerRebateRecord;
import com.ydqp.common.sendProtoMsg.vspoker.SVsPokerRebateRecord;
import com.ydqp.common.sendProtoMsg.vspoker.SVsPokerRebateRecordData;
import org.apache.commons.collections.CollectionUtils;

import java.util.*;

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

        List<VsRacePromote> playerPromoteRaces = VsRacePromoteDao.getInstance().findByPlayerId(rebateRecord.getPlayerId());
        if (CollectionUtils.isEmpty(playerPromoteRaces)) {
            logger.info("no rebate records, playerId:{}", rebateRecord.getPlayerId());
        } else {
            Set<Integer> raceIdsSet = new LinkedHashSet<>();
            Map<Integer, Float> rebateMap = new HashMap<>();
            Map<Integer, Integer> beginTimeMap = new HashMap<>();
            for (VsRacePromote vsRacePromote : playerPromoteRaces) {
                raceIdsSet.add(vsRacePromote.getRaceId());

                if (rebateMap.get(vsRacePromote.getRaceId()) == null) {
                    rebateMap.put(vsRacePromote.getRaceId(), vsRacePromote.getFee());
                } else {
                    Float aFloat = rebateMap.get(vsRacePromote.getRaceId());
                    rebateMap.put(vsRacePromote.getRaceId(), aFloat + vsRacePromote.getFee());
                }

                if (beginTimeMap.get(vsRacePromote.getRaceId()) == null) {
                    beginTimeMap.put(vsRacePromote.getRaceId(), vsRacePromote.getBeginTime());
                }
            }

            List<SVsPokerRebateRecordData> rebateRecordData = new ArrayList<>();
            for (Integer raceId : raceIdsSet) {
                SVsPokerRebateRecordData recordData = new SVsPokerRebateRecordData();
                recordData.setRaceId(raceId);
                recordData.setCreateTime(beginTimeMap.get(raceId));
                recordData.setRebate(rebateMap.get(raceId));
                rebateRecordData.add(recordData);
            }

            sVsPokerRebateRecord.setRecords(rebateRecordData);
            iSession.sendMessageByID(sVsPokerRebateRecord, rebateRecord.getConnId());
        }
    }
}
