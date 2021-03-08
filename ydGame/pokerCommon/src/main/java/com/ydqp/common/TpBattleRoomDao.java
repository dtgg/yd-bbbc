package com.ydqp.common;

import com.cfq.jdbc.JdbcOrm;

import java.util.Map;

public class TpBattleRoomDao {

    public static TpBattleRoomDao instance;

    public static TpBattleRoomDao getInstance () {
        if (instance == null) {
            instance = new TpBattleRoomDao();
        }
        return instance;
    }



    public void insertBattleTpRoomDetail (String tableName, Map<String, Object> params) {
        JdbcOrm.getInstance().insert(tableName, params);
    }

    public void delBattleRoom (int roomId) {
        String delRoomSql = "delete from battle_tp_room where roomId = " + roomId + ";";

        JdbcOrm.getInstance().update(delRoomSql);
    }

    public void addTpBattleRoomPlayerNum(int roomId) {
        String sql = "update battle_tp_room set playerNum = playerNum + 1 where roomId = " + roomId + ";";
        JdbcOrm.getInstance().update(sql);
    }

    public void decTpBattleRoomPlayerNum(int roomId) {
        String sql = "update battle_tp_room set playerNum = playerNum - 1 where roomId = " + roomId + ";";
        JdbcOrm.getInstance().update(sql);
    }

    public void addTpBattleRoomBootPlayerNum(int roomId) {
        String sql = "update battle_tp_room set bootPlayerNum = bootPlayerNum + 1 where roomId = " + roomId + ";";
        JdbcOrm.getInstance().update(sql);
    }

    public void decTpBattleRoomBootPlayerNum(int roomId) {
        String sql = "update battle_tp_room set bootPlayerNum = bootPlayerNum - 1 where roomId = " + roomId + ";";
        JdbcOrm.getInstance().update(sql);
    }
}
