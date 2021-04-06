package com.ydqp.vspoker.dao;

import com.cfq.jdbc.JdbcOrm;
import com.ydqp.common.entity.VsPlayerRace;

import java.util.List;
import java.util.Map;

public class VsPlayerRaceDao {

    private VsPlayerRaceDao() {
    }

    private static VsPlayerRaceDao instance = new VsPlayerRaceDao();

    public static VsPlayerRaceDao getInstance() {
        return instance;
    }

    public void insert(Map<String, Object> paramMap) {
        JdbcOrm.getInstance().insert("vs_player_race", paramMap);
    }

    public List<VsPlayerRace> getPlayerRaceByPlayerIdAndRaceIds(long playerId, String raceIds) {
        String sql = "select * from vs_player_race where playerId = " + playerId + " and raceId in " + raceIds + ";";
        return JdbcOrm.getInstance().getListBean(sql, VsPlayerRace.class);
    }

    public List<VsPlayerRace> getPlayerRaceByPlayerIdAndRaceId(long playerId, int raceId) {
        String sql = "select * from vs_player_race where playerId = " + playerId + " and raceId = " + raceId + ";";
        return JdbcOrm.getInstance().getListBean(sql, VsPlayerRace.class);
    }
}
