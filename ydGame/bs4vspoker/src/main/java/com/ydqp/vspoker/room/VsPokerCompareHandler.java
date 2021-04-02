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
        Poker aPlayerPoker = vsPokerRoom.getPokerMap().get(2);
        Poker bPlayerPoker = vsPokerRoom.getPokerMap().get(3);
        Poker cPlayerPoker = vsPokerRoom.getPokerMap().get(4);
        Poker dPlayerPoker = vsPokerRoom.getPokerMap().get(5);

        // A
        SPlayerInfo sPlayerInfo = new SPlayerInfo();
        sPlayerInfo.setWin(1);
        sPlayerInfo.setPoker(aPlayerPoker);
        sPlayerInfo.setBetPool(vsPokerRoom.getABetPool());

        //B
        SPlayerInfo sBPlayerInfo = new SPlayerInfo();
        sBPlayerInfo.setWin(1);
        sBPlayerInfo.setPoker(bPlayerPoker);
        sBPlayerInfo.setBetPool(vsPokerRoom.getBBetPool());

        //C
        SPlayerInfo sCPlayerInfo = new SPlayerInfo();
        sCPlayerInfo.setWin(1);
        sCPlayerInfo.setPoker(cPlayerPoker);
        sCPlayerInfo.setBetPool(vsPokerRoom.getCBetPool());

        //D
        SPlayerInfo sDPlayerInfo = new SPlayerInfo();
        sDPlayerInfo.setWin(1);
        sDPlayerInfo.setPoker(dPlayerPoker);
        sDPlayerInfo.setBetPool(vsPokerRoom.getDBetPool());

        if (isBankWin(bankPoker, aPlayerPoker)) {
            sPlayerInfo.setWin(0);
            vsPokerRoom.setAWin(0);
        }
        if (isBankWin(bankPoker, bPlayerPoker)) {
            sBPlayerInfo.setWin(0);
            vsPokerRoom.setBWin(0);
        }
        if (isBankWin(bankPoker, cPlayerPoker)) {
            sCPlayerInfo.setWin(0);
            vsPokerRoom.setCWin(0);
        }
        if (isBankWin(bankPoker, dPlayerPoker)) {
            sDPlayerInfo.setWin(0);
            vsPokerRoom.setDWin(0);
        }

        sVsCompareResult.setAPlayer(sPlayerInfo);
        sVsCompareResult.setBPlayer(sBPlayerInfo);
        sVsCompareResult.setCPlayer(sCPlayerInfo);
        sVsCompareResult.setDPlayer(sDPlayerInfo);
        sVsCompareResult.setBankPoker(bankPoker);

        //发送大小结果
        vsPokerRoom.sendMessageToBattles(sVsCompareResult);

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
