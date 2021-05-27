package com.ydqp.vspoker.room.play;

import com.cfq.connection.ISession;
import com.ydqp.common.data.PlayerData;
import com.ydqp.vspoker.room.RoomManager;
import com.ydqp.vspoker.room.VsPokerRoom;

public class FastRaceVsPlayObject extends AbstractVsPokerPlay{

    public FastRaceVsPlayObject(int basePoint, int roomType) {
        super(basePoint, roomType);
    }

    @Override
    public void enterRoom(PlayerData playerData, ISession iSession, int roomId) {
        for (int rid : roomIdList) {
            //判断用户的房间id是否存在

            VsPokerRoom vsPokerRoom = RoomManager.getInstance().getRoom(roomId);

        }

        generatorRoom();
    }

    @Override
    public VsPokerRoom generatorRoom() {
        VsPokerRoom vsPokerRoom = RoomManager.getInstance().createVsPokerRoom(roomType,basePoint);
        vsPokerRoom.setCurWaitTime(60);
        RoomManager.getInstance().putRoom(vsPokerRoom);

        roomIdList.add(vsPokerRoom.getRoomId());

        // 生成赛事,通过数据库 生成 raceId
        vsPokerRoom.setRaceId(raceId);

        return vsPokerRoom;
    }
}
