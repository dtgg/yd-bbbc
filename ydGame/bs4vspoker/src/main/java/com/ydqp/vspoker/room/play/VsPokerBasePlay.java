package com.ydqp.vspoker.room.play;

import com.cfq.connection.ISession;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.poker.room.Room;

public interface VsPokerBasePlay {

    Room generatorRoom();

    void enterRoom(PlayerData playerData, ISession iSession, int roomId);

    boolean checkTheRoomType(int roomType, int basePoint, int raceId);

    boolean checkPlayerOut(PlayerData playerData);

    boolean checkRoomId(PlayerData playerData, long playerId);

    void putPlayerMap(long playerId, int roomId);

    void deletePlayerMap(long playerId);

    int getPlayerRoomId(long playerId);

    void enterZjRoom(PlayerData playerData, ISession iSession, int roomId);
}
