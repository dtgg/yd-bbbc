package com.ydqp.vspoker.handler;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.entity.VsPlayerRace;
import com.ydqp.common.sendProtoMsg.vspoker.SVsPokerFastRaceHistory;
import com.ydqp.common.sendProtoMsg.vspoker.SVsPokerFastRaceInfo;
import com.ydqp.vspoker.dao.VsPlayerRaceDao;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@ServerHandler(module = "vsPoker", command = 7000030)
public class VsPokerFastRaceHistoryHandler implements IServerHandler {
    private static final Logger logger = LoggerFactory.getLogger(VsPokerFastRaceHistoryHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        PlayerData playerData = PlayerCache.getInstance().getPlayer(abstartParaseMessage.getConnId());
        if (playerData == null) {
            logger.error("player is not true");
            return;
        }

        List<VsPlayerRace> playerRaces = VsPlayerRaceDao.getInstance().getPlayerRaceByPlayerId(playerData.getPlayerId());

        List<SVsPokerFastRaceInfo> fastRaceInfos = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(playerRaces)) {
            for (VsPlayerRace playerRace : playerRaces) {
                SVsPokerFastRaceInfo fastRaceInfo = new SVsPokerFastRaceInfo();
                fastRaceInfo.setCreateTime(playerRace.getCreateTime());
                fastRaceInfo.setBasePoint(playerRace.getBasePoint());
                fastRaceInfo.setRanking(playerRace.getRank());
                fastRaceInfo.setBonus(playerRace.getBonus());
                fastRaceInfos.add(fastRaceInfo);
            }
        }

        SVsPokerFastRaceHistory sVsPokerFastRaceHistory = new SVsPokerFastRaceHistory();
        sVsPokerFastRaceHistory.setPlayerId(playerData.getPlayerId());
        sVsPokerFastRaceHistory.setFastRaceInfoList(fastRaceInfos);
        iSession.sendMessageByID(sVsPokerFastRaceHistory, abstartParaseMessage.getConnId());
    }
}
