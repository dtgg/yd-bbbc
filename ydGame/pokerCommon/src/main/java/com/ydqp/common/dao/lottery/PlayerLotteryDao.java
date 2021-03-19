package com.ydqp.common.dao.lottery;

import com.cfq.jdbc.JdbcOrm;
import com.ydqp.common.data.Total;
import com.ydqp.common.entity.Lottery;
import com.ydqp.common.entity.PlayerLottery;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;

public class PlayerLotteryDao {

    private PlayerLotteryDao() {
    }

    private static PlayerLotteryDao instance;

    public static PlayerLotteryDao getInstance() {
        if (instance == null)
            instance = new PlayerLotteryDao();
        return instance;
    }

    public long insert(Map<String, Object> params) {
        Object key = JdbcOrm.getInstance().insertReturnPrimkey("player_lottery", params);
        return (long) key;
    }

    public List<PlayerLottery> findByLotteryId(int lotteryId) {
        String sql = "select * from player_lottery where lotteryId = " + lotteryId + ";";
        return JdbcOrm.getInstance().getListBean(sql, PlayerLottery.class);
    }

    public List<PlayerLottery> findByLotteryIds(String lotteryIdsStr) {
        String sql = "select * from player_lottery where status = 1 and lotteryId in "+lotteryIdsStr+";";
        return JdbcOrm.getInstance().getListBean(sql, PlayerLottery.class);
    }

    public void batchUpdate(Object[][] params) {
        String sql = "update player_lottery set status = ?, award = ?, openTime = ?, result = ? where id = ?;";
        JdbcOrm.getInstance().batchUpdate(sql, params);
    }

    public List<PlayerLottery> findNewestLottery(long playerId) {
        String sql = "SELECT t.* FROM (SELECT type,max(createTime) as createTime FROM player_lottery WHERE playerId = " + playerId + " GROUP BY type) a " +
                "LEFT JOIN player_lottery t ON t.type=a.type and t.createTime=a.createTime;";
        return JdbcOrm.getInstance().getListBean(sql, PlayerLottery.class);
    }

    public List<PlayerLottery> page(long playerId, int type, int offset, int limit) {
        String sql = "select * from player_lottery pl " +
                "where pl.playerId = " + playerId + " and pl.type = " + type +
                " order by pl.id desc limit " + offset + "," + limit;
        return JdbcOrm.getInstance().getListBean(sql, PlayerLottery.class);
    }

    public Total count(long playerId, int type) {
        String sql = "select count(1) as total from player_lottery pl " +
                " where pl.playerId = " + playerId + " and pl.type = " + type;
        return (Total) JdbcOrm.getInstance().getBean(sql, Total.class);
    }

    public Total countByPlayerId(long playerId) {
        String sql = "select count(1) as total from player_lottery where playerId = " + playerId + ";";
        return (Total) JdbcOrm.getInstance().getBean(sql, Total.class);
    }

    //获取用户下注的数据
    public List<PlayerLottery> findByLotteryIdsOnlyBuy(String lotteryIdsStr) {
        String sql = "select * from player_lottery where status = 0 and lotteryId in "+lotteryIdsStr+";";
        return JdbcOrm.getInstance().getListBean(sql, PlayerLottery.class);
    }
}
