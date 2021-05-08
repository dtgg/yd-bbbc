package com.ydqp.vspoker.handler;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.receiveProtoMsg.vspoker.VsPokerGameBonus;
import com.ydqp.common.sendProtoMsg.vspoker.SVsPokerGameBonus;
import com.ydqp.vspoker.room.GameBonusManager;

import java.util.HashMap;
import java.util.Map;

@ServerHandler(module = "vsPoker", command = 7000018)
public class VsPokerGameBonusHandler implements IServerHandler {

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        VsPokerGameBonus vsPokerGameBonus = (VsPokerGameBonus) abstartParaseMessage;

        Map<Integer, Double> gameAwardMap = GameBonusManager.getInstance().gameBonusMap(vsPokerGameBonus.getRaceId());

        Map<Integer, Integer> bonusMap = new HashMap<>();
        gameAwardMap.forEach((k, v) -> bonusMap.put(k, v.intValue()));

        SVsPokerGameBonus sVsPokerGameBonus = new SVsPokerGameBonus();
        sVsPokerGameBonus.setBonusMap(bonusMap);
        iSession.sendMessageByID(sVsPokerGameBonus, vsPokerGameBonus.getConnId());
    }
}
