package com.ydqp.vspoker.handler;

import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.data.PlayerData;
import com.ydqp.vspoker.room.play.PlayVsPokerManager;
import com.ydqp.vspoker.room.play.VsPokerBasePlay;

public class VsPokerCheckPlayerJoinHandler implements IServerHandler {
    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        int basePoint = 10;

        PlayerData playerData = PlayerCache.getInstance().getPlayer(abstartParaseMessage.getConnId());

        VsPokerBasePlay vsPokerBasePlay = PlayVsPokerManager.getInstance().getPlayObject(2, basePoint, 0);

        boolean b = vsPokerBasePlay.checkRoomId(playerData.getRoomId(), playerData.getPlayerId());
    }
}
