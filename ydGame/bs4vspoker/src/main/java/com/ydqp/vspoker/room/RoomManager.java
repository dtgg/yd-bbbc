package com.ydqp.vspoker.room;

import com.ydqp.common.poker.room.Room;
import com.ydqp.common.poker.vspoker.VsPokerCard;
import com.ydqp.vspoker.NumberPool;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class RoomManager {

    private RoomManager() {}

    public static RoomManager instance = new RoomManager();

    public static RoomManager getInstance() {
        if (instance == null) {
            instance = new RoomManager();
        }

        return instance;
    }

    private int closeServer = 0;

    private Map<Integer, Room> vsPokerRoomMap = new ConcurrentHashMap<>();

    public Map<Integer, Room> getVsPokerRoomMapMap () {
        return vsPokerRoomMap;
    }

    @Getter
    @Setter
    private List<Integer> delRoomId = new CopyOnWriteArrayList<Integer>();


    public void putRoom(VsPokerRoom vsPokerRoom) {
        vsPokerRoomMap.put(vsPokerRoom.getRoomId(), vsPokerRoom);
    }

    public VsPokerRoom getRoom(Integer roomId) {
        VsPokerRoom vsPokerRoom = (VsPokerRoom) vsPokerRoomMap.get(roomId);
        return vsPokerRoom;
    }

    public VsPokerRoom createVsPokerRoom(int roomType, int basePoint) {
        VsPokerRoom vsPokerRoom = new VsPokerRoom();
        try {
            vsPokerRoom.setRoomId(7000000 + NumberPool.getInstance().pop());
            vsPokerRoom.setStatus(0);
            vsPokerRoom.setRoomType(roomType);
            vsPokerRoom.setBasePoint(basePoint);
            vsPokerRoom.setICardPoker(new VsPokerCard());

            for (int i = 1; i <= 4; i++) {
                vsPokerRoom.getPlayerObjectMap().put(i, new PlayerObject());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return vsPokerRoom;
    }

}
