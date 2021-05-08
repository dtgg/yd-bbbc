package com.ydqp.vspoker.handler;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.entity.VsRace;
import com.ydqp.common.receiveProtoMsg.vspoker.VsPokerGameBonus;
import com.ydqp.common.sendProtoMsg.vspoker.SVsPokerGameBonus;
import com.ydqp.vspoker.dao.VsPokerDao;
import com.ydqp.vspoker.room.GameBonusManager;

import java.util.HashMap;
import java.util.Map;

@ServerHandler(module = "vsPoker", command = 7000018)
public class VsPokerGameBonusHandler implements IServerHandler {
    private static final Logger logger = LoggerFactory.getLogger(VsPokerGameBonusHandler.class);
    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        VsPokerGameBonus vsPokerGameBonus = (VsPokerGameBonus) abstartParaseMessage;

        VsRace race = VsPokerDao.getInstance().getRaceById(vsPokerGameBonus.getRaceId());
        if (race == null) {
            logger.error("获取奖励信息失败，raceId 错误 ： {}", vsPokerGameBonus.getRaceId());
            return;
        }
        Map<Integer, Double> gameAwardMap = new HashMap<>();
        if (race.getRaceType() == 1) {
            gameAwardMap = GameBonusManager.getInstance().gameBonusMap(1);
        } else if (race.getRaceType() == 2) {
            double totalBonus = race.getBasePoint() * race.getCurPlayerNum() * 0.9;
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
