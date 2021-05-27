package com.ydqp.vspoker.handler;

import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.vspoker.room.play.PlayVsPokerManager;
import com.ydqp.vspoker.room.play.VsPokerBasePlay;

public class VsPokerFastRaceJoinHandler implements IServerHandler {
    private static final Logger logger = LoggerFactory.getLogger(VsPokerFastRaceJoinHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {

        // 1、客户端传入 basePoint, playerId
        // 2、根据playerId 获取player， player中保存 raceId

        VsPokerBasePlay vsPokerBasePlay = PlayVsPokerManager.getInstance().getPlayObject(2, 10, 0);

        vsPokerBasePlay.enterRoom();

    }
}
