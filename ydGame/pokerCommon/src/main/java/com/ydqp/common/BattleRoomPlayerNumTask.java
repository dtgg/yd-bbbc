package com.ydqp.common;

import lombok.Getter;
import lombok.Setter;

public class BattleRoomPlayerNumTask implements Runnable{

    @Setter
    @Getter
    private int roomId;
    @Setter
    @Getter
    private int type;
    @Setter
    @Getter
    private int gameType;

    @Setter
    @Getter
    private int playerType;

    public BattleRoomPlayerNumTask (int roomId, int type, int gameType, int playerType) {
        this.roomId = roomId;
        this.type = type;
        this.gameType = gameType;
        this.playerType = playerType; // 1 boot 2 fei boot
    }

    @Override
    public void run() {
        if(gameType == 20) {
            if(type == 1) {
                if(playerType == 1) {
                    BattleRoomDao.getInstance().addBattleRoomBootPlayerNum(roomId);
                } else {
                    BattleRoomDao.getInstance().addBattleRoomPlayerNum(roomId);
                }

            } else {
                if(playerType == 1) {
                    BattleRoomDao.getInstance().decBattleRoomBootPlayerNum(roomId);
                } else {
                    BattleRoomDao.getInstance().decBattleRoomPlayerNum(roomId);
                }

            }
        } else {
            if(type == 1) {
                if(playerType == 1) {
                    TpBattleRoomDao.getInstance().addTpBattleRoomBootPlayerNum(roomId);
                } else {
                    TpBattleRoomDao.getInstance().addTpBattleRoomPlayerNum(roomId);
                }

            } else {
                if(playerType == 1) {
                    TpBattleRoomDao.getInstance().decTpBattleRoomBootPlayerNum(roomId);
                } else {
                    TpBattleRoomDao.getInstance().decTpBattleRoomPlayerNum(roomId);
                }

            }
        }

    }
}
