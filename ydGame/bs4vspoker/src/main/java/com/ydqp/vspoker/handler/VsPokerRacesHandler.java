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
import com.ydqp.common.entity.VsRace;
import com.ydqp.common.receiveProtoMsg.vspoker.VsPokerRaces;
import com.ydqp.common.sendProtoMsg.vspoker.SVsPokerRaces;
import com.ydqp.common.sendProtoMsg.vspoker.SVsRace;
import com.ydqp.common.utils.CommonUtils;
import com.ydqp.vspoker.dao.VsPlayerRaceDao;
import com.ydqp.vspoker.dao.VsPokerDao;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@ServerHandler(module = "vsPoker", command = 7000011)
public class VsPokerRacesHandler implements IServerHandler {
    private static final Logger logger = LoggerFactory.getLogger(VsPokerRacesHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        VsPokerRaces vsPokerRaces = (VsPokerRaces) abstartParaseMessage;

        PlayerData playerData = PlayerCache.getInstance().getPlayer(vsPokerRaces.getConnId());
        if (playerData == null) {
            logger.error("player is not true");
            return;
        }

        List<VsRace> vsRaces = VsPokerDao.getInstance().getVsRaces(2, vsPokerRaces.getRoomType());
        List<SVsRace> sVsRaces = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(vsRaces)) {
            List<Integer> raceIds = new ArrayList<>();
            for (VsRace vsRace : vsRaces) {
                SVsRace sVsRace = new SVsRace(vsRace);
                sVsRaces.add(sVsRace);
                raceIds.add(sVsRace.getId());
            }

            List<VsPlayerRace> playerRaces = VsPlayerRaceDao.getInstance().getPlayerRaceByPlayerIdAndRaceIds(
                    vsPokerRaces.getPlayerId(), CommonUtils.inString(raceIds));
            if (CollectionUtils.isNotEmpty(playerRaces)) {
                List<Integer> raceIdList = new ArrayList<>();
                for (VsPlayerRace playerRace : playerRaces) {
                    raceIdList.add(playerRace.getRaceId());
                }

                for (SVsRace sVsRace : sVsRaces) {
                    sVsRace.setJoinStatus(raceIdList.contains(sVsRace.getId()) ? 1 : 0);
                }
            }
        }

        SVsPokerRaces sVsPokerRaces = new SVsPokerRaces();
        sVsPokerRaces.setSVsRaces(sVsRaces);
        iSession.sendMessageByID(sVsPokerRaces, abstartParaseMessage.getConnId());
    }
}
