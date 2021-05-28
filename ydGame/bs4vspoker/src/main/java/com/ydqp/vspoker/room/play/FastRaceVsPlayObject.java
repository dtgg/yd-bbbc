package com.ydqp.vspoker.room.play;

import com.cfq.connection.ISession;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.dao.PlayerDao;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.entity.VsRace;
import com.ydqp.common.poker.room.BattleRole;
import com.ydqp.vspoker.dao.VsPlayerRaceDao;
import com.ydqp.vspoker.dao.VsPokerDao;
import com.ydqp.vspoker.room.RoomManager;
import com.ydqp.vspoker.room.VsPokerRoom;

import java.util.Map;

public class FastRaceVsPlayObject extends AbstractVsPokerPlay {

    public FastRaceVsPlayObject(int basePoint, int roomType) {
        super(basePoint, roomType);
    }

    @Override
    public void enterRoom(PlayerData playerData, ISession iSession, int roomId) {
        for (Integer integer : roomIdList) {
            VsPokerRoom vsPokerRoom = RoomManager.getInstance().getRoom(integer);
            if (vsPokerRoom != null && vsPokerRoom.getCurWaitTime() > 0 && vsPokerRoom.getStatus() == 0) {
                if (vsPokerRoom.getBattleRoleMap().size() < vsPokerRoom.getMaxPlayerNum()) {
                    vsPokerRoom.enterRoom(playerData, iSession);
                    return;
                }
            }
        }

        VsPokerRoom vsPokerRoom = generatorRoom();
        vsPokerRoom.enterRoom(playerData, iSession);

        //设置房间信息
        BattleRole battleRole = vsPokerRoom.getBattleRoleMap().get(playerData.getPlayerId());
        vsPokerRoom.getVsPokerRoomInfo(battleRole);

        playerData.setRoomId(vsPokerRoom.getRoomId());
        PlayerCache.getInstance().addPlayer(iSession.getSessionId(), playerData);
        PlayerDao.getInstance().updatePlayerRoomId(playerData.getPlayerId(), vsPokerRoom.getRoomId());
    }

    @Override
    public boolean checkRoomId(int roomId, long playerId) {
        if (roomIdList.contains(roomId)) {
            VsPokerRoom vsPokerRoom = RoomManager.getInstance().getRoom(roomId);
            if (vsPokerRoom == null) {
                return false;
            }

            BattleRole battleRole = vsPokerRoom.getBattleRoleMap().get(playerId);
            return battleRole != null;
        }
        return false;
    }

    @Override
    public VsPokerRoom generatorRoom() {
        VsPokerRoom vsPokerRoom = RoomManager.getInstance().createVsPokerRoom(roomType,basePoint);
        vsPokerRoom.setCurWaitTime(60);
        vsPokerRoom.setMaxPlayerNum(10);
        RoomManager.getInstance().putRoom(vsPokerRoom);

        roomIdList.add(vsPokerRoom.getRoomId());

        // 生成赛事,通过数据库 生成 raceId
        VsRace vsRace = new VsRace();
        vsRace.setRaceType(roomType);
        vsRace.setBasePoint(basePoint);
        vsRace.setMaxPlayerNum(10);
        vsRace.setCurPlayerNum(0);
        vsRace.setTotalRound(15);
        vsRace.setStatus(0);
        int nowTime = new Long(System.currentTimeMillis() / 1000L).intValue();
        vsRace.setCreateTime(nowTime);
        vsRace.setBeginTime(nowTime);
        vsRace.setIsPermission(0);
        int raceId = VsPokerDao.getInstance().save(vsRace.getParameterMap());

        vsPokerRoom.setRaceId(raceId);
        return vsPokerRoom;
    }
}
