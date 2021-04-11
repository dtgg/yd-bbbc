package com.ydqp.vspoker.handler;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.entity.Player;
import com.ydqp.common.entity.VsPlayerRace;
import com.ydqp.common.receiveProtoMsg.vspoker.VsPokerRanking;
import com.ydqp.common.sendProtoMsg.vspoker.SVsPlayerRankData;
import com.ydqp.common.sendProtoMsg.vspoker.SVsPokerRaking;
import com.ydqp.common.service.PlayerService;
import com.ydqp.common.utils.CommonUtils;
import com.ydqp.vspoker.dao.VsPlayerRaceDao;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ServerHandler(command = 7000013, module = "vsPoker")
public class VsPokerRankingHandler implements IServerHandler {
    private static final Logger logger = LoggerFactory.getLogger(VsPokerRankingHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        VsPokerRanking rank = (VsPokerRanking) abstartParaseMessage;

        PlayerData playerData = PlayerCache.getInstance().getPlayer(rank.getConnId());
        if (playerData == null) {
            logger.error("player is not true");
            return;
        }

        List<VsPlayerRace> vsPlayerRaces = VsPlayerRaceDao.getInstance().getPlayerRaceOrderByRank(rank.getRaceId());
        List<SVsPlayerRankData> list = new ArrayList<>();
        SVsPlayerRankData playerRankData = new SVsPlayerRankData();

        if (CollectionUtils.isNotEmpty(vsPlayerRaces)) {
            List<Long> playerIds = new ArrayList<>();
            for (VsPlayerRace vsPlayerRace : vsPlayerRaces) {
                playerIds.add(vsPlayerRace.getPlayerId());
            }

            List<Player> players = PlayerService.getInstance().getPlayerByPlayerIds(CommonUtils.longString(playerIds));
            Map<Long, Player> playerMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(players)) {
                for (Player player : players) {
                    playerMap.put(player.getId(), player);
                }
            }

            for (VsPlayerRace vsPlayerRace : vsPlayerRaces) {
                Player player = playerMap.get(vsPlayerRace.getPlayerId());
                SVsPlayerRankData sVsPlayerRankData = new SVsPlayerRankData(vsPlayerRace, player == null ? "" : player.getPlayerName());

                StringBuilder buffer = new StringBuilder(sVsPlayerRankData.getPlayerName());
                buffer.replace(2, 8, "******");
                sVsPlayerRankData.setPlayerName(buffer.toString());

                list.add(sVsPlayerRankData);

                if (vsPlayerRace.getPlayerId() == playerData.getPlayerId()) {
                    playerRankData = sVsPlayerRankData;
                }
            }

            if (playerRankData.getPlayerId() == 0) {
                VsPlayerRace playerRace = VsPlayerRaceDao.getInstance().getPlayerRaceByPlayerIdAndRaceId(playerData.getPlayerId(), rank.getRaceId());
                if (playerRace != null) {
                    Player player = playerMap.get(playerRace.getPlayerId());
                    playerRankData = new SVsPlayerRankData(playerRace, player == null ? "" : player.getPlayerName());
                }
            }
        }

        SVsPokerRaking sVsPokerRaking = new SVsPokerRaking();
        sVsPokerRaking.setSPlayerRankData(list);
        sVsPokerRaking.setPlayerRankData(playerRankData);
        iSession.sendMessageByID(sVsPokerRaking, rank.getConnId());
    }
}
