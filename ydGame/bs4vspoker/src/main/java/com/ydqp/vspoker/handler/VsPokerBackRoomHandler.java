package com.ydqp.vspoker.handler;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.sendProtoMsg.vspoker.SVsPokerBackRoom;
import com.ydqp.vspoker.room.RoomManager;
import com.ydqp.vspoker.room.VsPokerRoom;

@ServerHandler(module = "vsPoker", command = 7000027)
public class VsPokerBackRoomHandler implements IServerHandler {
    private static final Logger logger = LoggerFactory.getLogger(VsPokerBackRoomHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        PlayerData playerData = PlayerCache.getInstance().getPlayer(abstartParaseMessage.getConnId());
        if (playerData == null) {
            logger.error("player is not true");
            return;
        }

        SVsPokerBackRoom backRoom = new SVsPokerBackRoom();

        VsPokerRoom room = RoomManager.getInstance().getRoom(playerData.getRoomId());
        if (room == null) {
            logger.error("返回快速赛房间失败，房间不存在，playerId：{}", playerData.getPlayerId());
            backRoom.setBackEnabled(false);
            iSession.sendMessageByID(backRoom, abstartParaseMessage.getConnId());
            return;
        }

        //进入
        room.vsEnterRoom(playerData, iSession);

        backRoom.setBackEnabled(true);
        iSession.sendMessageByID(backRoom, abstartParaseMessage.getConnId());
    }
}
