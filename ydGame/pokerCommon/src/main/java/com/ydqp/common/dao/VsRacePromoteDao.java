package com.ydqp.common.dao;

import com.cfq.jdbc.JdbcOrm;
import com.ydqp.common.data.PlayerRebateRecord;
import com.ydqp.common.data.Total;
import com.ydqp.common.entity.VsRacePromote;

import java.util.List;
import java.util.Map;

public class VsRacePromoteDao {

    private VsRacePromoteDao() {
    }

    private static VsRacePromoteDao instance = new VsRacePromoteDao();

    public static VsRacePromoteDao getInstance() {
        return instance;
    }

    public void insert(Map<String, Object> parameterMap) {
        JdbcOrm.getInstance().insert("vs_race_promote", parameterMap);
    }

    public List<VsRacePromote> findById(long playerId) {
        String sql = "select * from vs_race_promote where status = 0 and playerId = " + playerId + ";";
        return JdbcOrm.getInstance().getListBean(sql, VsRacePromote.class);
    }

    public void updateStatus(String idsStr) {
        String sql = "update vs_race_promote set status = 1 where id in " + idsStr + ";";
        JdbcOrm.getInstance().update(sql);
    }

    public Total sumRebateAmount(long playerId) {
        String sql = "select sum(fee) as sum from vs_race_promote where status = 0 and playerId = " + playerId + ";";
        return (Total) JdbcOrm.getInstance().getBean(sql, Total.class);
    }

    public List<VsRacePromote> findByPlayerId(long playerId) {
        String sql = "select * from vs_race_promote where playerId = " + playerId + " order by raceId desc;";
//        String sql = "select beginTime, raceId, sum(fee) as rebate from vs_race_promote where playerId = " + playerId + " GROUP BY raceId order by raceId desc;";
        return JdbcOrm.getInstance().getListBean(sql, VsRacePromote.class);
    }

    public List<VsRacePromote> findByPlayerIdAndRaceId(long playerId, int raceId) {
        String sql = "select * from vs_race_promote where playerId = " + playerId + " and raceId = " + raceId + ";";
        return JdbcOrm.getInstance().getListBean(sql, VsRacePromote.class);
    }
}
