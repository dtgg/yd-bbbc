package com.ydqp.vspoker.room;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.ydqp.common.poker.Poker;
import com.ydqp.common.poker.room.BattleRole;
import com.ydqp.common.sendProtoMsg.vspoker.SPlayerInfo;
import com.ydqp.common.sendProtoMsg.vspoker.SVsCompareResult;
import com.ydqp.common.sendProtoMsg.vspoker.SVsPokerRoomInfo;

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
        if (vsPokerRoom.isHarvest()) {
            changePoker(vsPokerRoom, bankPoker);
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
                sPlayerInfo.setBetPool(betMoney * 2);

                if (isBankWin(bankPoker, playerPoker)) {
                    sPlayerInfo.setWin(0);
                    sPlayerInfo.setWinBetPool(betMoney * 2);
                    vsPokerRoom.getPlayerObjectMap().get(i).setWin(0);

                    if (vsPokerRoom.getTrendMap().get(i + 1) == null) {
                        vsPokerRoom.getTrendMap().put(i + 1, new ArrayList<Boolean>(){{add(!isBankWin(bankPoker, playerPoker));}});
                    } else {
                        vsPokerRoom.getTrendMap().get(i + 1).add(!isBankWin(bankPoker, playerPoker));
                    }
                }
                sVsCompareResult.getSPlayerInfoMap().put(i, sPlayerInfo);
            }

            sVsCompareResult.setBankPoker(bankPoker);
            //发送大小结果
            vsPokerRoom.sendMessageToBattle(sVsCompareResult, entry.getKey());
        }
    }

    private void changePoker(VsPokerRoom vsPokerRoom, Poker bankPoker) {
        logger.info("换牌前：{}", JSON.toJSONString(vsPokerRoom.getPokerMap()));
        //真实用户下注金额, <poker,betAmount>
        Map<Integer, Double> pokerBetMap = new HashMap<>();
        for (Map.Entry<Long, BattleRole> entry : vsPokerRoom.getBattleRoleMap().entrySet()) {
            //过滤虚拟用户
            if (entry.getValue().getIsVir() == 1) continue;
            for (int i = 1; i <= 4; i++) {
//                Poker playerPoker = vsPokerRoom.getPokerMap().get(i + 1);
                PlayerObject playerObject = vsPokerRoom.getPlayerObjectMap().get(i);
                Double betMoney = playerObject.getBetBattleRoleId().get(entry.getKey());
                betMoney = betMoney == null ? 0 : betMoney;

                if (pokerBetMap.get(i + 1) == null) {
                    pokerBetMap.put(i + 1, betMoney);
                } else {
                    pokerBetMap.put(i + 1, pokerBetMap.get(i + 1) + betMoney);
                }
            }
        }
        if (pokerBetMap.size() == 0) return;
        logger.info("各区域下注金额：{}", JSON.toJSONString(pokerBetMap));

        List<Double> list = new ArrayList<>();
        for (Map.Entry<Integer, Double> entry : pokerBetMap.entrySet()) {
            list.add(entry.getValue());
        }
        //下注金额从小到大排序
        Collections.sort(list);

        //换牌
        for (int i = 0; i < list.size(); i++) {
            for (Map.Entry<Integer, Double> entry : pokerBetMap.entrySet()) {
                //比庄家小的两个牌
                if (list.get(i).equals(entry.getValue())) {
                    Poker playerPoker = vsPokerRoom.getPokerMap().get(entry.getKey());
                    if (i < 2) {
                        if (isBankWin(bankPoker, playerPoker)) {
                            vsPokerRoom.getPokerMap().put(entry.getKey(), bankPoker);
                            bankPoker = playerPoker;
                        }
                    } else {
                        if (!isBankWin(bankPoker, playerPoker)) {
                            vsPokerRoom.getPokerMap().put(entry.getKey(), bankPoker);
                            bankPoker = playerPoker;
                        }
                    }
                }
            }
        }
        vsPokerRoom.getPokerMap().put(1, bankPoker);
        logger.info("换牌后：{}", JSON.toJSONString(vsPokerRoom.getPokerMap()));
    }
}
