package com.ydqp.common.dao;

import com.cfq.jdbc.JdbcOrm;
import com.ydqp.common.entity.Player;

import java.util.List;
import java.util.Map;

public class PlayerDao {

    public static PlayerDao instance;

    public static PlayerDao getInstance() {
        if (instance == null) {
            instance = new PlayerDao();
        }
        return instance;
    }

    private static final String query_player = "select * from player where playerName = ";

    public Player queryByCondition(String queryCondition) {
        String sql = query_player + "'" + queryCondition + "' or id = '" + queryCondition + "';";
        return (Player) JdbcOrm.getInstance().getBean(sql, Player.class);
    }

    public void updatePlayerCoinPoint(double coinPoint, long id) {
        String sql = "update player set coinPoint = coinPoint + " + coinPoint + " where id = " + id + ";";
        JdbcOrm.getInstance().update(sql);
    }

    public int updatePlayerZjPoint(double zjPoint, long id) {
        String sql = "update player set zjPoint = zjPoint + " + zjPoint + " where id = " + id + " and zjPoint > 0;";
        return JdbcOrm.getInstance().updateByRow(sql);
    }

    public void updatePlayerGrade(long playerId, int grade, double experience) {
        String sql = "update player set grade = " + grade + ", experience = " + experience + " where id = " + playerId + ";";
        JdbcOrm.getInstance().update(sql);
    }

    public void updateHeadUrl(Object[] params) {
        String sql = "update player set headUrl = ? where id = ?";
        JdbcOrm.getInstance().updateByArray(sql, params);
    }

    public void setUpdatePlayerRoomId (long playerId, int roomId) {
        String updatePlayerMoney = "update player set roomId = " +roomId + " where id = " +playerId + ";";
        JdbcOrm.getInstance().update(updatePlayerMoney);
    }

    public void updatePlayerRoomId(long playerId, int roomId) {
        String updatePlayerMoney = "update player set roomId = " +roomId + " where id = " +playerId + ";";
        JdbcOrm.getInstance().update(updatePlayerMoney);
    }

    public void batchUpdate(Object[][] params) {
        String sql = "update player set zjPoint = zjPoint + ? where id = ?;";
        JdbcOrm.getInstance().batchUpdate(sql, params);
    }

    public void updateReferralCode(Object[] params) {
        String sql = "update player set referralCode = ? where id = ?;";
        JdbcOrm.getInstance().updateByArray(sql, params);
    }

    public void updatePassword(Object[] params) {
        String sql = "update player set password = ? where playerName = ?";
        JdbcOrm.getInstance().updateByArray(sql, params);
    }

    public Player queryById(long queryCondition) {
        String sql = "select * from player where " + "id = '" + queryCondition + "';";
        return (Player) JdbcOrm.getInstance().getBean(sql, Player.class);
    }

    public List<Player> getPlayerByPlayerIds(String longString) {
        String sql = "select * from player where id in "+longString+";";
        return JdbcOrm.getInstance().getListBean(sql, Player.class);
    }
}
