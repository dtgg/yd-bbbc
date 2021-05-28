package com.ydqp.vspoker.room.play;

public class RaceVsPokerPlayObject extends AbstractVsPokerPlay {

    public RaceVsPokerPlayObject(int basePoint, int roomType, int raceId, int totalRound) {
        super(basePoint, roomType, raceId, totalRound);
    }

    @Override
    public boolean checkRoomId(int roomId, long playerId) {
        return false;
    }
}
