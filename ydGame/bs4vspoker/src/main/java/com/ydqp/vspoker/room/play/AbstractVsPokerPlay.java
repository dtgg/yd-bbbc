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
    @Getter
    @Setter
    private int raceId;

    @Setter
    @Getter
    private int totalRound;

    private List<Integer> roomIdList = new ArrayList<>();

    public AbstractVsPokerPlay(int basePoint, int roomType, int raceId, int totalRound){
        this.basePoint = basePoint;
        this.roomType = roomType;
        this.raceId = raceId;
        this.totalRound = totalRound;
    }

    @Override
    public VsPokerRoom generatorRoom() {
        VsPokerRoom vsPokerRoom = RoomManager.getInstance().createVsPokerRoom(roomType,basePoint);
        vsPokerRoom.setRaceId(this.raceId);
        vsPokerRoom.setTotalRounds(this.totalRound);
        RoomManager.getInstance().putRoom(vsPokerRoom);

        roomIdList.add(vsPokerRoom.getRoomId());

        return vsPokerRoom;
    }

    @Override
    public void enterRoom(PlayerData playerData, ISession iSession, int roomId) {

        for (int rid : roomIdList) {
            VsPokerRoom vsPokerRoom = RoomManager.getInstance().getRoom(rid);
            if (vsPokerRoom == null) {
                continue;
            }
            vsPokerRoom.vsEnterRoom(playerData, iSession);
        }
    }

    @Override
    public boolean checkTheRoomType(int roomType, int basePoint, int raceId) {
        if (roomType == 1 || roomType == 2) {
            if (this.raceId == raceId){
                return true;
            }
        } else {
            if (this.roomType == roomType && this.basePoint == basePoint){
                return true;
            }
        }

        return false;
    }
}
