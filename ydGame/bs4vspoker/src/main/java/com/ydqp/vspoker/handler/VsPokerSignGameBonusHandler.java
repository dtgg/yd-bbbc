package com.ydqp.vspoker.handler;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.receiveProtoMsg.vspoker.VsPokerSignGameBonus;
import com.ydqp.common.sendProtoMsg.vspoker.SVsPokerGameBonus;
import com.ydqp.vspoker.room.GameBonusManager;

import java.util.HashMap;
import java.util.Map;

@ServerHandler(module = "vsPoker", command = 7000028)
public class VsPokerSignGameBonusHandler implements IServerHandler {
    private static final Logger logger = LoggerFactory.getLogger(VsPokerSignGameBonusHandler.class);
    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        VsPokerSignGameBonus vsPokerGameBonus = (VsPokerSignGameBonus) abstartParaseMessage;

        Map<Integer, Double> gameAwardMap = new HashMap<>();
        if (vsPokerGameBonus.getRaceType() == 1) {
            gameAwardMap = GameBonusManager.getInstance().gameBonusMap(1);
        } else if (vsPokerGameBonus.getRaceType() == 2) {
            double totalBonus = vsPokerGameBonus.getBasePoint() * 10;
            Map<Integer, Double> gameAwardMapByType2 = GameBonusManager.getInstance().gameBonusMap(2);
            for (Map.Entry<Integer, Double> entry : gameAwardMapByType2.entrySet()) {
                gameAwardMap.put(entry.getKey(), entry.getValue() * totalBonus);
            }
        }

        Map<Integer, Integer> bonusMap = new HashMap<>();
        gameAwardMap.forEach((k, v) -> bonusMap.put(k, v.intValue()));

        SVsPokerGameBonus sVsPokerGameBonus = new SVsPokerGameBonus();
        sVsPokerGameBonus.setBonusMap(bonusMap);
        iSession.sendMessageByID(sVsPokerGameBonus, vsPokerGameBonus.getConnId());
    }
}
