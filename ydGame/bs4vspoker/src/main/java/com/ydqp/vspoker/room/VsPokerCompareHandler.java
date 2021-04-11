package com.ydqp.vspoker.room;

import com.alibaba.fastjson.JSONObject;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.ydqp.common.poker.Poker;
import com.ydqp.common.poker.room.BattleRole;
import com.ydqp.common.sendProtoMsg.vspoker.SPlayerInfo;
import com.ydqp.common.sendProtoMsg.vspoker.SVsCompareResult;
import com.ydqp.common.sendProtoMsg.vspoker.SVsPokerRoomInfo;

import java.util.Map;

public class VsPokerCompareHandler implements IRoomStatusHandler{

    private static final Logger logger = LoggerFactory.getLogger(VsPokerCompareHandler.class);
    @Override
    public void doHandler(VsPokerRoom vsPokerRoom) {
        //下注结束，进行比牌
        SVsCompareResult sVsCompareResult = new SVsCompareResult();
        sVsCompareResult.setRoomId(vsPokerRoom.getRoomId());
        Poker bankPoker = vsPokerRoom.getPokerMap().get(1);

        for (Map.Entry<Long, BattleRole> entry : vsPokerRoom.getBattleRoleMap().entrySet()) {
            for (int i = 1; i<= 4; i++) {
                Poker playerPoker = vsPokerRoom.getPokerMap().get(i + 1);
                PlayerObject playerObject = vsPokerRoom.getPlayerObjectMap().get(i);

                SPlayerInfo sPlayerInfo = new SPlayerInfo();
                sPlayerInfo.setWin(playerObject.getWin());
                sPlayerInfo.setPoker(playerPoker);


                Double betMoney = playerObject.getBetBattleRoleId().get(entry.getKey());
                betMoney = betMoney == null ? 0 :betMoney;
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

        vsPokerRoom.setStatus(3);
        vsPokerRoom.setCurWaitTime(15);

        logger.info("VsPokerCompareHandler end body = {} ", JSONObject.toJSONString(sVsCompareResult));
    }

    /**
     * 判断大小
     * @param bankPoker
     * @param playerPoker
     * @return
     */
    private boolean isBankWin (Poker bankPoker, Poker playerPoker) {
        if (bankPoker.getNum() >= playerPoker.getNum()) {
            return true;
        }
        return false;
    }

}
