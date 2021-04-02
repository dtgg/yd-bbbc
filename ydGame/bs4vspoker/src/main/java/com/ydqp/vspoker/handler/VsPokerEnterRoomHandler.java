package com.ydqp.vspoker.handler;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.dao.PlayerDao;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.entity.Player;
import com.ydqp.common.receiveProtoMsg.vspoker.VsPokerEnterRoom;
import com.ydqp.vspoker.room.RoomManager;
import com.ydqp.vspoker.room.VsPokerRoom;

@ServerHandler(module = "vsPoker", command = 7000005)
public class VsPokerEnterRoomHandler implements IServerHandler {

    private static final Logger logger = LoggerFactory.getLogger(VsPokerXiazhuHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        VsPokerEnterRoom vsPokerEnterRoom = (VsPokerEnterRoom) abstartParaseMessage;

        PlayerData playerData = PlayerCache.getInstance().getPlayer(vsPokerEnterRoom.getConnId());
        if (playerData == null) {
            logger.error("player is not true");
            return;
        }

        Player player = PlayerDao.getInstance().queryById(playerData.getPlayerId());
        if(player == null) {
            logger.error("no player");
            return;
        }
        playerData.setZjPoint(player.getZjPoint());

        VsPokerRoom vsPokerRoom = RoomManager.getInstance().getRoom(7000001);
        vsPokerRoom.vsEnterRoom(playerData, iSession);

    }
}
