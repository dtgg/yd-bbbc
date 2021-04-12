package com.ydqp.vspoker.handler;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.receiveProtoMsg.vspoker.VsPokerSignInBonus;
import com.ydqp.common.sendProtoMsg.vspoker.SVsPokerSignInBonus;
import com.ydqp.vspoker.room.GameBonusManager;
import com.ydqp.vspoker.room.RoomManager;
import com.ydqp.vspoker.room.VsPokerRoom;

import java.util.HashMap;
import java.util.Map;

@ServerHandler(module = "vsPoker", command = 7000019)
public class VsPokerSignInBonusHandler implements IServerHandler {
    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        VsPokerSignInBonus signInBonus = (VsPokerSignInBonus) abstartParaseMessage;

        VsPokerRoom room = RoomManager.getInstance().getRoom(signInBonus.getRoomId());
        Map<Integer, Double> rateMap = GameBonusManager.getInstance().gameBonusMap(2);

        Map<Integer, Double> bonusMap = new HashMap<>();
        rateMap.forEach((rank, rate) -> bonusMap.put(rank, rate * room.getBonus()));

        SVsPokerSignInBonus sVsPokerSignInBonus = new SVsPokerSignInBonus();
        sVsPokerSignInBonus.setBonusMap(bonusMap);
        iSession.sendMessageByID(sVsPokerSignInBonus, signInBonus.getConnId());
    }
}
