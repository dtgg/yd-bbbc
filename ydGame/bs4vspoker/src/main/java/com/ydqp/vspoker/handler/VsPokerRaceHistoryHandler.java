package com.ydqp.vspoker.handler;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.entity.VsRace;
import com.ydqp.common.receiveProtoMsg.vspoker.VsPokerRaceHistory;
import com.ydqp.common.sendProtoMsg.vspoker.SVsPokerRaceHistory;
import com.ydqp.common.sendProtoMsg.vspoker.SVsPokerRaces;
import com.ydqp.common.sendProtoMsg.vspoker.SVsRace;
import com.ydqp.vspoker.dao.VsPokerDao;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@ServerHandler(module = "vsPoker", command = 7000012)
public class VsPokerRaceHistoryHandler implements IServerHandler {
    private static final Logger logger = LoggerFactory.getLogger(VsPokerRaceHistoryHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        VsPokerRaceHistory vsPokerRaceHistory = (VsPokerRaceHistory) abstartParaseMessage;
        PlayerData playerData = PlayerCache.getInstance().getPlayer(abstartParaseMessage.getConnId());
        if (playerData == null) {
            logger.error("player is not true");
            return;
        }

        List<VsRace> vsRaces = VsPokerDao.getInstance().getVsRaceHistory(2, vsPokerRaceHistory.getRoomType());
        List<SVsRace> sVsRaces = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(vsRaces)) {
            for (VsRace vsRace : vsRaces) {
                SVsRace sVsRace = new SVsRace(vsRace);
                sVsRaces.add(sVsRace);
            }
        }
        SVsPokerRaceHistory sVsPokerRaceHistory = new SVsPokerRaceHistory();
        sVsPokerRaceHistory.setSVsRaces(sVsRaces);
        iSession.sendMessageByID(sVsPokerRaceHistory, abstartParaseMessage.getConnId());
    }
}
