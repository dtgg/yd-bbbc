package com.ydqp.common.dao.lottery;

import com.cfq.jdbc.JdbcOrm;
import com.ydqp.common.data.Total;
import com.ydqp.common.entity.PlayerPromote;

import java.util.List;
import java.util.Map;

public class PlayerPromoteDao {

    private PlayerPromoteDao() {
    }

    private static PlayerPromoteDao instance;

    public static PlayerPromoteDao getInstance() {
        if (instance == null)
            instance = new PlayerPromoteDao();
        return instance;
    }

    public PlayerPromote findByPlayerId(Long playerId) {
        String sql = "select * from player_promote where playerId = " + playerId + ";";
        return (PlayerPromote) JdbcOrm.getInstance().getBean(sql, PlayerPromote.class);
    }

    public void insert(Map<String, Object> parameterMap) {
        JdbcOrm.getInstance().insert("player_promote", parameterMap);
    }

    public void update(Object[] param) {
        String sql = "update player_promote set superiorAmount = superiorAmount + ?, grandAmount = grandAmount + ?, isEffective = ? where id = ?;";
        JdbcOrm.getInstance().updateByArray(sql, param);
    }

    public void updateSubEffective(Long superiorId) {
        String sql = "update player_promote set subNum = subNum + 1 where playerId = " + superiorId + ";";
        JdbcOrm.getInstance().update(sql);
    }

    public void updateSonEffective(Long grandId) {
        String sql = "update player_promote set sonNum = sonNum + 1 where playerId = " + grandId + ";";
        JdbcOrm.getInstance().update(sql);
    }

    public List<PlayerPromote> findChildren(long playerId) {
        String sql = "select * from player_promote where isEffective = 1 and (superiorId = " + playerId + " or grandId = " + playerId + ");";
        return JdbcOrm.getInstance().getListBean(sql, PlayerPromote.class);
    }

    public List<PlayerPromote> findChildren(long playerId, int lv, int offset, int limit) {
        String columnName = lv == 1 ? "superiorId" : "grandId";
        String sql = "select * from player_promote where isEffective = 1 and " + columnName + " = " + playerId +
                " order by id desc limit " + offset + "," + limit + ";";
        return JdbcOrm.getInstance().getListBean(sql, PlayerPromote.class);
    }

    public Total sumBonus(long playerId, int lv) {
        String columnName = lv == 1 ? "superiorAmount" : "grandAmount";
        String conditionName = lv == 1 ? "superiorId" : "grandId";
        String sql = "select ifnull(sum(" + columnName + "),0) as sum from player_promote where isEffective = 1 and " + conditionName + " = " + playerId;
        return (Total) JdbcOrm.getInstance().getBean(sql, Total.class);
    }

    public Total count(long playerId, int lv) {
        String columnName = lv == 1 ? "superiorId" : "grandId";
        String sql = "select count(1) as total from player_promote where isEffective = 1 and " + columnName + " = " + playerId;
        return (Total) JdbcOrm.getInstance().getBean(sql, Total.class);
    }

    private static final String UPDATE_BONUS_AMOUNT = "update player_promote set bonusAmount = bonusAmount + ? where playerId = ? and bonusAmount > 0;";
    public int updateBonusAmount(Object[] params) {
        return JdbcOrm.getInstance().updateRowByArray(UPDATE_BONUS_AMOUNT, params);
    }
}
