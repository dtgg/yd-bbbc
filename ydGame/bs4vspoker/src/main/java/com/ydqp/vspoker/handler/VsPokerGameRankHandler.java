package com.ydqp.vspoker.handler;

import com.alibaba.fastjson.JSON;
import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.poker.room.BattleRole;
import com.ydqp.common.receiveProtoMsg.vspoker.VsPokerGameRank;
import com.ydqp.common.sendProtoMsg.vspoker.SVsPlayerRankData;
import com.ydqp.common.sendProtoMsg.vspoker.SVsPokerGameRank;
import com.ydqp.vspoker.cache.RankingCache;
import com.ydqp.vspoker.room.GameBonusManager;
import com.ydqp.vspoker.room.RoomManager;
import com.ydqp.vspoker.room.VsPokerRoom;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ServerHandler(module = "vsPoker", command = 7000015)
public class VsPokerGameRankHandler implements IServerHandler {

    private static final Logger logger = LoggerFactory.getLogger(VsPokerGameRankHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        VsPokerGameRank rank = (VsPokerGameRank) abstartParaseMessage;
        logger.info("获取游戏内排行版信息:{}", JSON.toJSONString(rank));

        PlayerData playerData = PlayerCache.getInstance().getPlayer(rank.getConnId());
        if (playerData == null) {
            logger.error("player is not true");
            return;
        }

        VsPokerRoom vsPokerRoom = RoomManager.getInstance().getRoom(rank.getRoomId());

        Set<String> rankInfo = null;
        try {
            rankInfo = RankingCache.getInstance().getRankInfo(vsPokerRoom.getRaceId(), 0, -1);
        } catch (NullPointerException e) {
            logger.error("no player join, redis data is null");
        }

        if (CollectionUtils.isEmpty(rankInfo)) {
            logger.error("no player join");
            return;
        }

        List<Long> playerIds = new ArrayList<>();
        for (String s : rankInfo) {
            playerIds.add(Long.parseLong(s));
        }

        List<SVsPlayerRankData> list = new ArrayList<>();
        SVsPlayerRankData playerRankData = new SVsPlayerRankData();
        if (vsPokerRoom.getBattleRoleMap() != null) {
            for (int i = 0; i < playerIds.size(); i++) {
                for (Map.Entry<Long, BattleRole> entry : vsPokerRoom.getBattleRoleMap().entrySet()) {
                    if (entry.getKey().equals(playerIds.get(i))) {
                        SVsPlayerRankData sVsPlayerRankData = new SVsPlayerRankData();
                        sVsPlayerRankData.setPlayerId(entry.getKey());

                        StringBuilder buffer = new StringBuilder(entry.getValue().getPlayerName());
                        buffer.replace(2, 8, "******");
                        sVsPlayerRankData.setPlayerName(buffer.toString());

                        int ranks = i + 1;
                        Double bonus = GameBonusManager.getInstance().getBonus(vsPokerRoom, ranks);
                        if (bonus == null) bonus = 0D;

                        sVsPlayerRankData.setRank(ranks);
                        sVsPlayerRankData.setPoint(entry.getValue().getRankZJ());
                        sVsPlayerRankData.setBonus(bonus);
                        if (i < 100) {
                            list.add(sVsPlayerRankData);
                        }

                        if (entry.getKey() == playerData.getPlayerId()) {
                            playerRankData = sVsPlayerRankData;
                        }
                    }
                }
            }
        }

        SVsPokerGameRank sVsPokerGameRank = new SVsPokerGameRank();
        sVsPokerGameRank.setSPlayerRankData(list);
        sVsPokerGameRank.setPlayerRankData(playerRankData);
        iSession.sendMessageByID(sVsPokerGameRank, rank.getConnId());
    }
}
