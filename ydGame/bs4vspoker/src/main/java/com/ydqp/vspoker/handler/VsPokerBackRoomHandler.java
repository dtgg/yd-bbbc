package com.ydqp.vspoker.handler;

import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.data.PlayerData;
import com.ydqp.vspoker.room.RoomManager;
import com.ydqp.vspoker.room.VsPokerRoom;

public class VsPokerBackRoomHandler implements IServerHandler {
    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        PlayerData playerData = PlayerCache.getInstance().getPlayer(abstartParaseMessage.getConnId());

        VsPokerRoom room = RoomManager.getInstance().getRoom(playerData.getRoomId());
        if (room == null) {
            //房间已结束
        }

        //进入
        room.vsEnterRoom(playerData, iSession);
    }
}
