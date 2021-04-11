package com.ydqp.vspoker.handler;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.receiveProtoMsg.vspoker.VsPokerGameBonus;
import com.ydqp.common.sendProtoMsg.vspoker.SVsPokerGameBonus;

import java.util.HashMap;
import java.util.Map;

@ServerHandler(module = "vsPoker", command = 7000018)
public class VsPokerGameBonusHandler implements IServerHandler {

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        VsPokerGameBonus vsPokerGameBonus = (VsPokerGameBonus) abstartParaseMessage;

        Map<Integer, Integer> bonusMap = new HashMap<Integer, Integer>(){{
            put(1, 1000);
            put(2, 500);
            put(3, 300);
            put(4, 100);
            put(5, 100);
            put(6, 100);
            put(7, 100);
            put(8, 100);
            put(9, 100);
            put(10, 100);
            put(11, 50);
            put(12, 50);
            put(13, 50);
            put(14, 50);
            put(15, 50);
            put(16, 50);
            put(17, 50);
            put(18, 50);
            put(19, 50);
            put(20, 50);
        }};

        SVsPokerGameBonus sVsPokerGameBonus = new SVsPokerGameBonus();
        sVsPokerGameBonus.setBonusMap(bonusMap);
        iSession.sendMessageByID(sVsPokerGameBonus, vsPokerGameBonus.getConnId());
    }
}
