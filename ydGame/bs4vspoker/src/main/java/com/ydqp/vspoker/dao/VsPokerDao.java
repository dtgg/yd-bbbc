package com.ydqp.vspoker.dao;

import com.cfq.jdbc.JdbcOrm;
import com.ydqp.common.entity.VsRace;

import java.util.List;

public class VsPokerDao {
    private VsPokerDao() {}

    public static VsPokerDao instance;

    public static VsPokerDao getInstance() {
        if (instance == null) instance = new VsPokerDao();
        return instance;
    }

    public List<VsRace> getVsRaceByCreateTime (int beginTime) {
        String sql = "select * from vs_race where status = 0 and beginTime <= " + beginTime + ";";
        List<VsRace> raceList = JdbcOrm.getInstance().getListBean(sql, VsRace.class);

        return raceList;
    }

    public void updateRaceStatus(int id) {
        String  sql = "update vs_race set status = 1 where id = " + id +";";
        JdbcOrm.getInstance().update(sql);
    }
}
