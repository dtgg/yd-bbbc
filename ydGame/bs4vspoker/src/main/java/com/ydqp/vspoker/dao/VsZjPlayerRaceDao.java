package com.ydqp.vspoker.dao;

import com.cfq.jdbc.JdbcOrm;

import java.util.Map;

public class VsZjPlayerRaceDao {

    private VsZjPlayerRaceDao() {}

    private static VsZjPlayerRaceDao instance = new VsZjPlayerRaceDao();

    public static VsZjPlayerRaceDao getInstance() {
        return instance;
    }

    public void insert(Map<String, Object> map) {
        JdbcOrm.getInstance().insert("vs_zj_player_race", map);
    }
}
