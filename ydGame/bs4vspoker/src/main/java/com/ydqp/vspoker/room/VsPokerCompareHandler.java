package com.ydqp.vspoker.room;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.ydqp.common.poker.CompareUtil;
import com.ydqp.common.poker.Poker;
import com.ydqp.common.poker.room.BattleRole;
import com.ydqp.common.sendProtoMsg.vspoker.SPlayerInfo;
import com.ydqp.common.sendProtoMsg.vspoker.SVsCompareResult;
import com.ydqp.common.sendProtoMsg.vspoker.SVsPlayTypeWin;

import java.util.*;

public class VsPokerCompareHandler implements IRoomStatusHandler {

    private static final Logger logger = LoggerFactory.getLogger(VsPokerCompareHandler.class);

    @Override
    public void doHandler(VsPokerRoom vsPokerRoom) {
        //下注结束，进行比牌
        SVsCompareResult sVsCompareResult = new SVsCompareResult();
        sVsCompareResult.setRoomId(vsPokerRoom.getRoomId());
        Poker bankPoker = vsPokerRoom.getPokerMap().get(1);

        if (vsPokerRoom.getRoomType() == 3) {
            zjPokerCompare(vsPokerRoom, bankPoker, sVsCompareResult);
            vsPokerRoom.setStatus(4);
            vsPokerRoom.setCurWaitTime(15);
            return;
        }

        for (Map.Entry<Long, BattleRole> entry : vsPokerRoom.getBattleRoleMap().entrySet()) {
            for (int i = 1; i <= 4; i++) {
                Poker playerPoker = vsPokerRoom.getPokerMap().get(i + 1);
                PlayerObject playerObject = vsPokerRoom.getPlayerObjectMap().get(i);

                SPlayerInfo sPlayerInfo = new SPlayerInfo();
                sPlayerInfo.setWin(playerObject.getWin());
                sPlayerInfo.setPoker(playerPoker);


                Double betMoney = playerObject.getBetBattleRoleId().get(entry.getKey());
                betMoney = betMoney == null ? 0 : betMoney;
                sPlayerInfo.setBetPool(betMoney * 2);

                if (isBankWin(bankPoker, playerPoker)) {
                    sPlayerInfo.setWin(0);
                    sPlayerInfo.setWinBetPool(betMoney * 2);
                    vsPokerRoom.getPlayerObjectMap().get(i).setWin(0);
                }
                sVsCompareResult.getSPlayerInfoMap().put(i, sPlayerInfo);
            }

            sVsCompareResult.setBankPoker(bankPoker);

            if (entry.getValue().isQuite()) continue;
            //发送大小结果
            vsPokerRoom.sendMessageToBattle(sVsCompareResult, entry.getKey());
        }

        vsPokerRoom.setStatus(4);
        vsPokerRoom.setCurWaitTime(15);

        logger.info("VsPokerCompareHandler end body = {} ", JSONObject.toJSONString(sVsCompareResult));
    }

    /**
     * 判断大小
     *
     * @param bankPoker
     * @param playerPoker
     * @return
     */
    private boolean isBankWin(Poker bankPoker, Poker playerPoker) {
        if (bankPoker.getNum() >= playerPoker.getNum()) {
            return true;
        }
        return false;
    }

    private void zjPokerCompare(VsPokerRoom vsPokerRoom, Poker bankPoker, SVsCompareResult sVsCompareResult) {
        logger.info("现金场比牌");
        if (vsPokerRoom.isHarvest()) {
            logger.info("换牌前bankPoker：{}", JSON.toJSONString(bankPoker));
            changePoker(vsPokerRoom);
            bankPoker = vsPokerRoom.getPokerMap().get(1);
            logger.info("换牌后bankPoker：{}", JSON.toJSONString(bankPoker));
        }

        for (Map.Entry<Long, BattleRole> entry : vsPokerRoom.getBattleRoleMap().entrySet()) {
            if (entry.getValue().getIsVir() == 1) continue;
            for (int i = 1; i <= 4; i++) {
                Poker playerPoker = vsPokerRoom.getPokerMap().get(i + 1);
                PlayerObject playerObject = vsPokerRoom.getPlayerObjectMap().get(i);

                SPlayerInfo sPlayerInfo = new SPlayerInfo();
                sPlayerInfo.setWin(playerObject.getWin());
                sPlayerInfo.setPoker(playerPoker);

                Double betMoney = playerObject.getBetBattleRoleId().get(entry.getKey());
                betMoney = betMoney == null ? 0 : betMoney;
                sPlayerInfo.setBetPool(betMoney * 1.95);

                if (isBankWin(bankPoker, playerPoker)) {
                    sPlayerInfo.setWin(0);
                    sPlayerInfo.setWinBetPool(betMoney * 1.95);
                    vsPokerRoom.getPlayerObjectMap().get(i).setWin(0);
                }
                sVsCompareResult.getSPlayerInfoMap().put(i, sPlayerInfo);
            }

            sVsCompareResult.setBankPoker(bankPoker);
            //发送大小结果
            vsPokerRoom.sendMessageToBattle(sVsCompareResult, entry.getKey());
        }

        Map<Integer, Poker> pokerMap = vsPokerRoom.getPokerMap();
        Poker newBankPoker = pokerMap.get(1);
        Map<Integer, Boolean> pokerWinMap = new HashMap<>();
        for (int i = 1; i <= 4; i++) {
            Poker playerPoker = vsPokerRoom.getPokerMap().get(i + 1);
            pokerWinMap.put(i + 1, !isBankWin(newBankPoker, playerPoker));
        }
        SVsPlayTypeWin sVsPlayTypeWin = new SVsPlayTypeWin();
        sVsPlayTypeWin.setPlayTypeWinMap(pokerWinMap);
        vsPokerRoom.getTrendList().add(sVsPlayTypeWin);
        if (vsPokerRoom.getTrendList().size() > 20) vsPokerRoom.getTrendList().remove(0);
    }

    private void changePoker(VsPokerRoom vsPokerRoom) {
        logger.info("换牌前：{}", JSON.toJSONString(vsPokerRoom.getPokerMap()));
        //真实用户下注金额, <poker,betAmount>
        Map<Integer, Double> pokerBetMap = new HashMap<>();
        List<Poker> pokers = new ArrayList<>();
        for (Map.Entry<Integer, Poker> entry : vsPokerRoom.getPokerMap().entrySet()) {
            pokers.add(entry.getValue());
        }

        Double totalBet = 0D;
        for (Map.Entry<Long, BattleRole> entry : vsPokerRoom.getBattleRoleMap().entrySet()) {
            //过滤虚拟用户
            if (entry.getValue().getIsVir() == 1) continue;
            for (int i = 1; i <= 4; i++) {
                PlayerObject playerObject = vsPokerRoom.getPlayerObjectMap().get(i);
                Double betMoney = playerObject.getBetBattleRoleId().get(entry.getKey());
                betMoney = betMoney == null ? 0 : betMoney;

                totalBet += betMoney;
                if (pokerBetMap.get(i + 1) == null) {
                    //加上区域ID，防止金额相同
                    pokerBetMap.put(i + 1, betMoney + i + 1);
                } else {
                    pokerBetMap.put(i + 1, pokerBetMap.get(i + 1) + betMoney);
                }
            }
        }
        if (pokerBetMap.size() == 0) return;
        logger.info("各区域下注金额：{}", JSON.toJSONString(pokerBetMap));
        if (totalBet == 0D) return;
        if (vsPokerRoom.getVsRaceConfig().getKillArea() == 0) return;

        List<Double> list = new ArrayList<>();
        for (Map.Entry<Integer, Double> entry : pokerBetMap.entrySet()) {
            list.add(entry.getValue());
        }
        //下注金额从小到大排序
        Collections.sort(list);
        //手牌从小到大排序
        logger.info("排序前手牌：{}", JSON.toJSONString(pokers));
        Collections.sort(pokers, new CompareUtil(1));
        logger.info("排序后手牌：{}", JSON.toJSONString(pokers));

        logger.info("排序后手牌：{}", JSON.toJSONString(pokers));
        Poker bankPoker = pokers.get(vsPokerRoom.getVsRaceConfig().getKillArea());
        vsPokerRoom.getPokerMap().put(1, bankPoker);
        pokers.remove(vsPokerRoom.getVsRaceConfig().getKillArea());

        //换牌
        for (int i = 0; i < list.size(); i++) {
            for (Map.Entry<Integer, Double> entry : pokerBetMap.entrySet()) {
                if (list.get(i).equals(entry.getValue())) {
                    Poker playerPoker = pokers.get(3-i);
                    vsPokerRoom.getPokerMap().put(entry.getKey(), playerPoker);
                }
            }
        }
        logger.info("换牌后：{}", JSON.toJSONString(vsPokerRoom.getPokerMap()));
    }
}
