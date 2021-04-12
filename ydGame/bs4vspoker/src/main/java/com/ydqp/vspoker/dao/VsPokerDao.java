package com.ydqp.vspoker.dao;

import com.cfq.jdbc.JdbcOrm;
import com.ydqp.common.entity.VsRace;

import java.util.List;

public class VsPokerDao {
    private VsPokerDao() {
    }

    public static VsPokerDao instance;

    public static VsPokerDao getInstance() {
        if (instance == null) instance = new VsPokerDao();
        return instance;
    }

    public List<VsRace> getVsRaceByCreateTime(int beginTime) {
        String sql = "select * from vs_race where status = 0 and beginTime <= " + beginTime + ";";
        List<VsRace> raceList = JdbcOrm.getInstance().getListBean(sql, VsRace.class);

        return raceList;
    }

    public void updateRaceStatus(int id, int status) {
        String sql = "update vs_race set status = " + status + " where id = " + id + ";";
        JdbcOrm.getInstance().update(sql);
    }

    public List<VsRace> getVsRaces(int status, int roomType) {
        String sql = "select * from vs_race where raceType = "+roomType+" and status != " + status + " order by beginTime asc;";
        return JdbcOrm.getInstance().getListBean(sql, VsRace.class);
    }

    public List<VsRace> getVsRaceHistory(int status, int roomType) {
        String sql = "select * from vs_race where raceType = "+roomType+" and status = " + status + " order by beginTime desc;";
        return JdbcOrm.getInstance().getListBean(sql, VsRace.class);
    }

    public VsRace getRaceById(int id) {
        String sql = "select *from vs_race where id = " + id + ";";
        return (VsRace) JdbcOrm.getInstance().getBean(sql, VsRace.class);
    }

    public int updateCurPlayerNum(int raceId) {
        String sql = "update vs_race set curPlayerNum = curPlayerNum + 1 where id = " + raceId + " and curPlayerNum < maxPlayerNum";
        return JdbcOrm.getInstance().updateByRow(sql);
    }

    public static void main(String[] args) {
        int startTime = 1618215900;
        for (int i = 0; i < 24; i++) {
            VsRace vsRace = new VsRace();
            vsRace.setRaceType(1);
            vsRace.setBasePoint(1);
            vsRace.setMaxPlayerNum(1000);
            vsRace.setCurPlayerNum(0);
            vsRace.setStatus(0);
            vsRace.setBeginTime(startTime + 300 * i);
            vsRace.setCreateTime(startTime);
            vsRace.setTotalRound(15);
            if (i % 2 == 0) {
                vsRace.setRaceType(2);
                vsRace.setBasePoint(10);
            }
            JdbcOrm.getInstance().insert("vs_race", vsRace.getParameterMap());
        }
    }
}
