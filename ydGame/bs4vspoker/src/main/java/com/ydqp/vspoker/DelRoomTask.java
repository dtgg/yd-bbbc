package com.ydqp.vspoker;

import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.util.StackTraceUtil;
import com.ydqp.common.ThreadManager;
import com.ydqp.common.poker.room.BattleRole;
import com.ydqp.common.poker.room.Room;
import com.ydqp.common.sendProtoMsg.vspoker.SVsBonusRank;
import com.ydqp.common.sendProtoMsg.vspoker.SVsRaceEnd;
import com.ydqp.vspoker.cache.RankingCache;
import com.ydqp.vspoker.dao.VsPlayerRaceDao;
import com.ydqp.vspoker.dao.VsPokerDao;
import com.ydqp.vspoker.room.GameAwardManager;
import com.ydqp.vspoker.room.RoomManager;
import org.apache.commons.collections.CollectionUtils;

import java.util.*;

public class DelRoomTask implements Runnable{

    private static final Logger logger = LoggerFactory.getLogger(DelRoomTask.class);


    public DelRoomTask() {
    }

    @Override
    public void run() {
        //删除房间
        try {
            for(Integer roomId : RoomManager.getInstance().getDelRoomId()) {
                Room room = RoomManager.getInstance().getRoom(roomId);
                if (room != null) {
                    logger.info("删除房间开始，roomId = {}", roomId);

                    if (room.getRoomType() == 1 ||room.getRoomType() == 2) {
                        //更新状态
                        VsPokerDao.getInstance().updateRaceStatus(room.getRaceId(), 2);
                        //@TODO 通知用户比赛结束
                        notifyRaceEnd(room);
                    }

                    ThreadManager.getInstance().getExecutor().execute(() ->
                            updateRank(room.getRaceId(), room.getBattleRoleMap()));

                    RoomManager.getInstance().getVsPokerRoomMapMap().remove(roomId);
                    NumberPool.getInstance().push(roomId);

                    RankingCache.getInstance().delRankInfo(room.getRaceId());

                    logger.info("删除房间结束，roomId = {}", roomId);
                }
            }

            RoomManager.getInstance().getDelRoomId().clear();
        } catch (Exception e) {
            logger.error("关闭房间失败异常, e = {}" , StackTraceUtil.getStackTrace(e));
        }

    }

    /**
     * 通知比赛结束
     * @param vsPokerRoom
     */
    private void notifyRaceEnd (Room vsPokerRoom) {
        Set<String> rankInfo = null;
        try {
            rankInfo = RankingCache.getInstance().getRankInfo(vsPokerRoom.getRaceId(), 0, -1);
        } catch (NullPointerException e) {
            logger.error("no player join, redis data is null");
        }
        List<Long> playerIds = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(rankInfo)) {
            for (String s : rankInfo) {
                playerIds.add(Long.parseLong(s));
            }
        }
        Map<Long, Integer> rankMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(playerIds)) {
            for (int i = 0; i < playerIds.size(); i++) {
                rankMap.put(playerIds.get(i), i + 1);
            }
        }

        Map<Integer, SVsBonusRank> sVsBonusRankMap = new HashMap<>();
        for (Map.Entry<Long, BattleRole> entry : vsPokerRoom.getBattleRoleMap().entrySet()) {
            rankMap.forEach((playerId, rank) -> {
                if (rank <= 3) {
                    if (entry.getKey().equals(playerId)) {
                        SVsBonusRank  sVsBonusRank = new SVsBonusRank();

                        Double bonus = GameAwardManager.getInstance().getGameAwardMap().get(rank);
                        if (bonus == null) bonus = 0D;
                        sVsBonusRank.setBonus(bonus);
                        sVsBonusRank.setPoints(entry.getValue().getPlayerZJ());

                        StringBuilder buffer = new StringBuilder(entry.getValue().getPlayerName());
                        buffer.replace(2, 8, "******");
                        sVsBonusRank.setPlayerName(buffer.toString());
                        sVsBonusRankMap.put(rank, sVsBonusRank);
                    }
                }
            });
        }

        for (Map.Entry<Long, BattleRole> entry : vsPokerRoom.getBattleRoleMap().entrySet()) {
            SVsRaceEnd sVsRaceEnd = new SVsRaceEnd();
            sVsRaceEnd.setRoomId(vsPokerRoom.getRoomId());
            sVsRaceEnd.setBonusRankMap(sVsBonusRankMap);
            sVsRaceEnd.setPlayerId(entry.getKey());

            for (int i = 0; i < playerIds.size(); i++) {
                if (playerIds.get(i).equals(entry.getKey())) {
                    int rank = i + 1;
                    Double bonus = GameAwardManager.getInstance().getGameAwardMap().get(rank);
                    if (bonus == null) bonus = 0D;

                    sVsRaceEnd.setBonus(bonus);
                    sVsRaceEnd.setRank(rank);
                }
            }

            if (entry.getValue().isQuite()) continue;
            vsPokerRoom.sendMessageToBattle(sVsRaceEnd, entry.getKey());
        }

    }

    private void updateRank(int raceId, Map<Long, BattleRole> battleRoleMap) {
//        Object[][] params = new Object[battleRoleMap.entrySet().size()][];
        List<Object[]> params = new ArrayList<>();
        for (Map.Entry<Long, BattleRole> entry : battleRoleMap.entrySet()) {
            RankingCache.getInstance().addRank(raceId, entry.getValue().getPlayerZJ(), entry.getKey());
        }

        Set<String> rankInfo = null;
        try {
            rankInfo = RankingCache.getInstance().getRankInfo(raceId, 0, -1);
        } catch (NullPointerException e) {
            logger.error("no player join, redis data is null");
        }
        if (CollectionUtils.isNotEmpty(rankInfo)) {
            int i = 0;
            for (String s : rankInfo) {
                long playerId = Long.parseLong(s);
                for (Map.Entry<Long, BattleRole> entry : battleRoleMap.entrySet()) {
                    if (playerId == entry.getValue().getPlayerId()) {
                        logger.info("完赛排名：raceId：{}，playerId：{}，rank：{}, zj", raceId, entry.getKey(), i + 1, entry.getValue().getPlayerZJ());

                        int rank = i + 1;
                        Double bonus = GameAwardManager.getInstance().getGameAwardMap().get(rank);
                        if (bonus == null) bonus = 0D;

                        Object[] param = new Object[]{rank, bonus, entry.getValue().getPlayerZJ(), raceId, playerId};
                        params.add(param);
                        i++;
                    }
                }
            }
            if (params.size() < battleRoleMap.entrySet().size()) {
                logger.info("用户数量异常，房间中人数：{}, 缓存中人数：{}, 排名：{}", battleRoleMap.entrySet().size(), params.size(), rankInfo);
            }
            if (CollectionUtils.isNotEmpty(params)) {
                Object[][] parameters = new Object[params.size()][];
                for (int i1 = 0; i1 < params.size(); i1++) {
                    parameters[i1] = params.get(i1);
                }
                VsPlayerRaceDao.getInstance().updatePlayerRace(parameters);
            }
        }
    }
}

