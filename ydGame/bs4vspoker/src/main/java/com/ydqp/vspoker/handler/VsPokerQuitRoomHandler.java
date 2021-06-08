package com.ydqp.vspoker.handler;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.poker.room.BattleRole;
import com.ydqp.common.receiveProtoMsg.vspoker.VsPokerQuitRoom;
import com.ydqp.common.sendProtoMsg.vspoker.SVsPokerQuitRoom;
import com.ydqp.common.service.PlayerService;
import com.ydqp.vspoker.room.RoomManager;
import com.ydqp.vspoker.room.VsPokerRoom;

@ServerHandler(command = 7000020, module = "vsPoker")
public class VsPokerQuitRoomHandler implements IServerHandler {
    private static final Logger logger = LoggerFactory.getLogger(VsPokerQuitRoomHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        VsPokerQuitRoom vsPokerQuitRoom = (VsPokerQuitRoom) abstartParaseMessage;
        SVsPokerQuitRoom sVsPokerQuitRoom = new SVsPokerQuitRoom();

        PlayerData playerData = PlayerCache.getInstance().getPlayer(vsPokerQuitRoom.getConnId());
        if (playerData == null) {
            logger.error("player is not true");
            return;
        }

        VsPokerRoom vsPokerRoom = RoomManager.getInstance().getRoom(vsPokerQuitRoom.getRoomId());
        if (vsPokerRoom == null) {
            logger.error("退出房间失败，未找到房间，roomId:{}", vsPokerQuitRoom.getRoomId());
            sVsPokerQuitRoom.setRoomId(vsPokerQuitRoom.getRoomId());
            sVsPokerQuitRoom.setPlayerId(vsPokerQuitRoom.getPlayerId());
            sVsPokerQuitRoom.setQuit(false);
            iSession.sendMessageByID(sVsPokerQuitRoom, vsPokerQuitRoom.getConnId());
            return;
        }

        BattleRole battleRole = vsPokerRoom.getBattleRoleMap().get(vsPokerQuitRoom.getPlayerId());
        if (battleRole == null) {
            logger.error("退出房间失败，用户不在房间中，roomId:{}, playerId:{}", vsPokerQuitRoom.getRoomId(), vsPokerQuitRoom.getPlayerId());
            sVsPokerQuitRoom.setRoomId(vsPokerQuitRoom.getRoomId());
            sVsPokerQuitRoom.setPlayerId(vsPokerQuitRoom.getPlayerId());
            sVsPokerQuitRoom.setQuit(false);
            iSession.sendMessageByID(sVsPokerQuitRoom, vsPokerQuitRoom.getConnId());
            return;
        }

        battleRole.setQuite(true);
        PlayerService.getInstance().updatePlayerRoomId(playerData.getPlayerId(), 0);
        playerData.setRoomId(0);
        PlayerCache.getInstance().addPlayer(vsPokerQuitRoom.getConnId(), playerData);
//        if (vsPokerRoom.getRoomType() == 3) {
//            vsPokerRoom.getBattleRoleMap().remove(vsPokerQuitRoom.getPlayerId());
//            PlayerService.getInstance().updatePlayerRoomId(playerData.getPlayerId(), 0);
//        }
        logger.info("玩家离开房间, roomId:{}, playerId:{}", vsPokerQuitRoom.getRoomId(), vsPokerQuitRoom.getPlayerId());

        sVsPokerQuitRoom.setRoomId(vsPokerQuitRoom.getRoomId());
        sVsPokerQuitRoom.setPlayerId(vsPokerQuitRoom.getPlayerId());
        sVsPokerQuitRoom.setQuit(true);
        iSession.sendMessageByID(sVsPokerQuitRoom, vsPokerQuitRoom.getConnId());
    }
}


