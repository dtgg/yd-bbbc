package com.ydqp.vspoker.handler;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.sendProtoMsg.vspoker.SVsPokerRaceBasePoint;
import com.ydqp.vspoker.room.play.PlayVsPokerManager;

import java.util.List;

@ServerHandler(command = 7000029, module = "vsPoker")
public class VsPokerRaceBasePointHandler implements IServerHandler {
    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        List<Integer> fastRaceList = PlayVsPokerManager.getInstance().getFastRaceList();

        SVsPokerRaceBasePoint raceBasePoint = new SVsPokerRaceBasePoint();
        raceBasePoint.setBasePoints(fastRaceList);
        iSession.sendMessageByID(raceBasePoint, abstartParaseMessage.getConnId());
    }
}
