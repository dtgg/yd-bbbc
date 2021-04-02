package com.ydqp.vspoker.room.play;

import com.cfq.connection.ISession;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.poker.room.Room;
import com.ydqp.vspoker.room.RoomManager;
import com.ydqp.vspoker.room.VsPokerRoom;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractVsPokerPlay implements VsPokerBasePlay {

    @Setter
    @Getter
    private int basePoint;

    @Getter
    @Setter
    private int roomType; // 1 免费赛  2 报名赛  3 zj

    private List<Integer> roomIdList = new ArrayList<>();

    public AbstractVsPokerPlay(int basePoint, int roomType){
        this.basePoint = basePoint;
        this.roomType = roomType;
    }

    @Override
    public VsPokerRoom generatorRoom() {
        VsPokerRoom vsPokerRoom = RoomManager.getInstance().createVsPokerRoom(roomType,basePoint);
        RoomManager.getInstance().putRoom(vsPokerRoom);

        roomIdList.add(vsPokerRoom.getRoomId());

        return vsPokerRoom;
    }

    @Override
    public void enterRoom(PlayerData playerData, ISession iSession, int roomId) {

        for (int rid : roomIdList) {
            VsPokerRoom vsPokerRoom = RoomManager.getInstance().getRoom(rid);
            vsPokerRoom.vsEnterRoom(playerData, iSession);
        }
    }

    @Override
    public boolean checkTheRoomType(int roomType, int basePoint) {
        if (this.roomType == roomType && this.basePoint == basePoint){
            return true;
        }
        return false;
    }
}
