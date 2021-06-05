package com.ydqp.vspoker.room;

import com.alibaba.fastjson.JSONObject;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.ydqp.common.ThreadManager;
import com.ydqp.common.poker.room.BattleRole;
import com.ydqp.common.sendProtoMsg.vspoker.SVsPlayerWin;
import com.ydqp.common.sendProtoMsg.vspoker.SVsPokerPerRanking;
import com.ydqp.common.sendProtoMsg.vspoker.SVsTaoTai;
import com.ydqp.common.service.PlayerService;
import com.ydqp.vspoker.cache.RankingCache;
import com.ydqp.vspoker.room.play.PlayVsPokerManager;
import com.ydqp.vspoker.room.play.VsPokerBasePlay;
import org.apache.commons.collections.CollectionUtils;

import java.util.*;

public class VsPokerSettlementHandler implements IRoomStatusHandler{

    private static final Logger logger = LoggerFactory.getLogger(VsPokerSettlementHandler.class);
    @Override
    public void doHandler(VsPokerRoom vsPokerRoom) {
        if (vsPokerRoom.getRoomType() == 3) {
            zjPaiFu(vsPokerRoom);
            return;
        }
        paiFu(vsPokerRoom);
        //重置数据，房间状态
        vsPokerRoom.setRound(vsPokerRoom.getRound() + 1);
        vsPokerRoom.setRankPlayerIds(new ArrayList<>());
        vsPokerRoom.setVirBet(false);

        for (int i = 1; i <= 4; i++) {
            PlayerObject playerObject = vsPokerRoom.getPlayerObjectMap().get(i);
            playerObject.setWin(1);
            playerObject.getBetBattleRoleId().clear();
            playerObject.setBetPool(0);
        }
        vsPokerRoom.getPokerMap().clear();
        vsPokerRoom.setStatus(0);

        if(vsPokerRoom.getRoomType() == 1 || vsPokerRoom.getRoomType() == 2) {
            //淘汰用户
            int otherPlayer = playerOut(vsPokerRoom);
//            if (otherPlayer <= 2) {
//                vsPokerRoom.setStatus(-1);
//                return;
//            }
            if (vsPokerRoom.getRound() >=  vsPokerRoom.getTotalRounds()) {
                vsPokerRoom.setStatus(-1);
            }
        }

        ThreadManager.getInstance().getExecutor().execute(() ->
                updateRank(vsPokerRoom));

        logger.info("VsPokerSettlementHandler end");
    }

    /**
     * 对赢的玩家进行赔付
     * @param vsPokerRoom
     */
    private void paiFu (VsPokerRoom vsPokerRoom) {
        Map<Long, PlayerWin> playerWinMap = new HashMap<>();
        for (int i = 1; i <= 4; i++){
            PlayerObject playerObject = vsPokerRoom.getPlayerObjectMap().get(i);
            if (playerObject.getWin() == 1) {
                Map<Long, Double> betBattleRoleId = playerObject.getBetBattleRoleId();
                if (betBattleRoleId == null) {
                    continue;
                }

                for (Map.Entry<Long, Double> entry : betBattleRoleId.entrySet()) {
                    BattleRole battleRole = vsPokerRoom.getBattleRoleMap().get(entry.getKey());
                    if (battleRole != null) {
                        PlayerWin playerWin = playerWinMap.get(battleRole.getPlayerId());
                        if (playerWin == null) {
                            playerWin = new PlayerWin();

                        }
                        playerWin.getWinTypes().add(i);
                        playerWin.setWinMoney(playerWin.getWinMoney() + entry.getValue());
                        playerWinMap.put(battleRole.getPlayerId(), playerWin);
                    }
                }
            }
        }

        for (Map.Entry<Long, PlayerWin> entry : playerWinMap.entrySet()) {
            BattleRole battleRole = vsPokerRoom.getBattleRoleMap().get(entry.getKey());
            if (battleRole == null) {
                logger.error("结算时进行赔付，结果没找到下注的battleRole,playerId = {} money = {}", entry.getKey(),
                        JSONObject.toJSONString(entry.getValue()));
                continue;
            }
            //赔付
            SVsPlayerWin sVsPlayerWin = new SVsPlayerWin();
            double peiM = entry.getValue().getWinMoney() * 2;
            battleRole.setPlayerZJ(battleRole.getPlayerZJ() + peiM);

            sVsPlayerWin.setRoomId(vsPokerRoom.getRoomId());
            sVsPlayerWin.setPlayerId(battleRole.getPlayerId());
            sVsPlayerWin.setTotalMoney(battleRole.getPlayerZJ());
            sVsPlayerWin.setWinMoney(peiM);
            sVsPlayerWin.setWinTypes(entry.getValue().getWinTypes());

            if (battleRole.isQuite()) continue;
            vsPokerRoom.sendMessageToBattle(sVsPlayerWin, battleRole.getPlayerId());
            //@TODO 数据库更新
        }
    }

    private void zjPaiFu (VsPokerRoom vsPokerRoom) {
        Map<Long, PlayerWin> playerWinMap = new HashMap<>();
        for (int i = 1; i <= 4; i++){
            PlayerObject playerObject = vsPokerRoom.getPlayerObjectMap().get(i);
            if (playerObject.getWin() == 1) {
                Map<Long, Double> betBattleRoleId = playerObject.getBetBattleRoleId();
                if (betBattleRoleId == null) {
                    continue;
                }

                for (Map.Entry<Long, Double> entry : betBattleRoleId.entrySet()) {
                    BattleRole battleRole = vsPokerRoom.getBattleRoleMap().get(entry.getKey());
                    if (battleRole != null && battleRole.getIsVir() == 0) {
                        PlayerWin playerWin = playerWinMap.get(battleRole.getPlayerId());
                        if (playerWin == null) {
                            playerWin = new PlayerWin();

                        }
                        playerWin.getWinTypes().add(i);
                        playerWin.setWinMoney(playerWin.getWinMoney() + entry.getValue());
                        playerWinMap.put(battleRole.getPlayerId(), playerWin);
                    }
                }
            }
        }

        for (Map.Entry<Long, PlayerWin> entry : playerWinMap.entrySet()) {
            BattleRole battleRole = vsPokerRoom.getBattleRoleMap().get(entry.getKey());
            if (battleRole == null) {
                logger.error("结算时进行赔付，结果没找到下注的battleRole,playerId = {} money = {}", entry.getKey(),
                        JSONObject.toJSONString(entry.getValue()));
                continue;
            }
            if (battleRole.getIsVir() == 1) continue;
            //赔付
            SVsPlayerWin sVsPlayerWin = new SVsPlayerWin();
            double peiM = entry.getValue().getWinMoney() * 2;
            battleRole.setPlayerZJ(battleRole.getPlayerZJ() + peiM);

            sVsPlayerWin.setRoomId(vsPokerRoom.getRoomId());
            sVsPlayerWin.setPlayerId(battleRole.getPlayerId());
            sVsPlayerWin.setTotalMoney(battleRole.getPlayerZJ());
            sVsPlayerWin.setWinMoney(peiM);
            sVsPlayerWin.setWinTypes(entry.getValue().getWinTypes());

            vsPokerRoom.sendMessageToBattle(sVsPlayerWin, battleRole.getPlayerId());
            //数据库更新
            PlayerService.getInstance().updatePlayerZjPoint(peiM, entry.getKey());
        }
    }

    /**
     * 淘汰用户
     * @param vsPokerRoom
     * @return
     */
    private int playerOut(VsPokerRoom vsPokerRoom) {
        for (Map.Entry<Long, BattleRole> entry : vsPokerRoom.getBattleRoleMap().entrySet()) {
            RankingCache.getInstance().addRank(vsPokerRoom.getRaceId(), entry.getValue().getPlayerZJ(), entry.getKey());
            entry.getValue().setRankZJ(entry.getValue().getPlayerZJ());
//            entry.getValue().setRank(rankNo.intValue());
        }

        int otherPlayer = 0;
        for (Map.Entry<Long, BattleRole> entry : vsPokerRoom.getBattleRoleMap().entrySet()) {
            if (entry.getValue().getIsOut() == 1) {
                continue;
            }
            if (entry.getValue().getPlayerZJ() < 10) {
                SVsTaoTai sVsTaoTai = new SVsTaoTai();
                sVsTaoTai.setPlayerId(entry.getKey());

                Long rankNo = RankingCache.getInstance().getRankNo(vsPokerRoom.getRaceId(), entry.getKey());
                int rank = rankNo.intValue() + 1;
                Double bonus = GameBonusManager.getInstance().getBonus(vsPokerRoom, rank);
                if (bonus == null) bonus = 0D;
                sVsTaoTai.setBonus(bonus);
                sVsTaoTai.setRank(rank);

                sVsTaoTai.setRoomId(vsPokerRoom.getRoomId());
                vsPokerRoom.sendMessageToBattle(sVsTaoTai, entry.getKey());
                entry.getValue().setIsOut(1);

                VsPokerBasePlay vsPokerBasePlay = PlayVsPokerManager.getInstance().getPlayObject(2, vsPokerRoom.getBasePoint(), 0);
                vsPokerBasePlay.deletePlayerMap(entry.getKey());
            } else {
                otherPlayer++;
            }
        }
        return otherPlayer;
    }

    private void updateRank(VsPokerRoom vsPokerRoom) {
        int raceId = vsPokerRoom.getRaceId();
        Map<Long, BattleRole> battleRoleMap = vsPokerRoom.getBattleRoleMap();

        Set<String> rankInfo = RankingCache.getInstance().getRankInfo(raceId, 0, -1);
        if (CollectionUtils.isEmpty(rankInfo)) return;

        List<Long> playerIds = new ArrayList<>();
        for (String s : rankInfo) {
            playerIds.add(Long.parseLong(s));
        }

        for (int i = 0; i < playerIds.size(); i++) {
            for (Map.Entry<Long, BattleRole> entry : battleRoleMap.entrySet()) {
                if (entry.getKey().equals(playerIds.get(i))) {
                    Long rankNo = RankingCache.getInstance().getRankNo(raceId, entry.getKey());
                    logger.info("更新排名：raceId：{}，playerId：{}，rank：{}, zj:{}, round:{}",
                            raceId, entry.getKey(), rankNo + 1, entry.getValue().getPlayerZJ(), vsPokerRoom.getRound());

                    if (entry.getValue().isQuite()) continue;
                    SVsPokerPerRanking sVsPokerPerRanking = new SVsPokerPerRanking();
                    sVsPokerPerRanking.setPlayerId(playerIds.get(i));
                    sVsPokerPerRanking.setRank(i + 1);
                    vsPokerRoom.sendMessageToBattle(sVsPokerPerRanking, entry.getKey());
                }
            }
        }
    }
}
