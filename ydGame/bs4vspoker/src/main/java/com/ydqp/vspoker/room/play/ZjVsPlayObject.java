package com.ydqp.vspoker.room.play;

import com.cfq.connection.ISession;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.dao.PlayerDao;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.entity.VsPlayerRace;
import com.ydqp.common.entity.VsRace;
import com.ydqp.common.entity.VsRaceConfig;
import com.ydqp.common.poker.room.BattleRole;
import com.ydqp.vspoker.cache.RankingCache;
import com.ydqp.vspoker.dao.VsPlayerRaceDao;
import com.ydqp.vspoker.dao.VsPokerDao;
import com.ydqp.vspoker.dao.VsRaceConfigDao;
import com.ydqp.vspoker.room.RoomManager;
import com.ydqp.vspoker.room.VsPokerRoom;

import java.util.Random;

public class ZjVsPlayObject extends AbstractVsPokerPlay {

    public ZjVsPlayObject(int roomType) {
        super(roomType);
    }

    public ZjVsPlayObject(int roomType, int raceId) {
        super(roomType, raceId, -1);
    }

    @Override
    public void enterRoom(PlayerData playerData, ISession iSession, int roomId) {
        for (Integer integer : roomIdList) {
            VsPokerRoom vsPokerRoom = RoomManager.getInstance().getRoom(integer);
            if (vsPokerRoom != null) {
                vsPokerRoom.vsEnterRoom(playerData, iSession);
                //更新数据库缓存
                playerData.setRoomId(vsPokerRoom.getRoomId());
                PlayerCache.getInstance().addPlayer(playerData.getSessionId(), playerData);
                PlayerDao.getInstance().updatePlayerRoomId(playerData.getPlayerId(), vsPokerRoom.getRoomId());
                return;
            }
        }

        VsPokerRoom vsPokerRoom = generatorRoom();
        vsPokerRoom.vsEnterRoom(playerData, iSession);
        //更新数据库缓存
        playerData.setRoomId(vsPokerRoom.getRoomId());
        PlayerCache.getInstance().addPlayer(playerData.getSessionId(), playerData);
        PlayerDao.getInstance().updatePlayerRoomId(playerData.getPlayerId(), vsPokerRoom.getRoomId());
    }

    @Override
    public void putPlayerMap(long playerId, int roomId) {
        playerMap.put(playerId, roomId);
    }

    @Override
    public VsPokerRoom generatorRoom() {
        VsPokerRoom vsPokerRoom = RoomManager.getInstance().createVsPokerRoom(roomType,basePoint);
        vsPokerRoom.setMaxPlayerNum(10);
        RoomManager.getInstance().putRoom(vsPokerRoom);

        roomIdList.add(vsPokerRoom.getRoomId());

        return vsPokerRoom;
    }
}
