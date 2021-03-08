package com.ydqp.common;

import com.cfq.jdbc.JdbcOrm;

import java.util.Map;

public class BattleRoomDao {

    public static BattleRoomDao instance;

    public static BattleRoomDao getInstance () {
        if (instance == null) {
            instance = new BattleRoomDao();
        }
        return instance;
    }



    public void insertBattleRoomDetail (Map<String, Object> params) {
        JdbcOrm.getInstance().insert("battle_room", params);
    }

    public void delBattleRoom (int roomId) {
        String delRoomSql = "delete from battle_room where roomId = " + roomId + ";";

        JdbcOrm.getInstance().update(delRoomSql);
    }

    public void addBattleRoomPlayerNum(int roomId) {
        String sql = "update battle_room set playerNum = playerNum + 1 where roomId = " + roomId + ";";
        JdbcOrm.getInstance().update(sql);
    }

    public void decBattleRoomPlayerNum(int roomId) {
        String sql = "update battle_room set playerNum = playerNum - 1 where roomId = " + roomId + ";";
        JdbcOrm.getInstance().update(sql);
    }

    public void addBattleRoomBootPlayerNum(int roomId) {
        String sql = "update battle_room set bootPlayerNum = bootPlayerNum + 1 where roomId = " + roomId + ";";
        JdbcOrm.getInstance().update(sql);
    }

    public void decBattleRoomBootPlayerNum(int roomId) {
        String sql = "update battle_room set bootPlayerNum = bootPlayerNum - 1 where roomId = " + roomId + ";";
        JdbcOrm.getInstance().update(sql);
    }
}
