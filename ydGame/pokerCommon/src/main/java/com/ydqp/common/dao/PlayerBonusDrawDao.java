package com.ydqp.common.dao;

import com.cfq.jdbc.JdbcOrm;
import com.ydqp.common.entity.PlayerBonusDraw;

import java.util.List;
import java.util.Map;

public class PlayerBonusDrawDao {

    private PlayerBonusDrawDao() {}

    private static PlayerBonusDrawDao instance = new PlayerBonusDrawDao();

    public static PlayerBonusDrawDao getInstance() {
        return instance;
    }

    public List<PlayerBonusDraw> getBonusDrawsByPlayerId(Long playerId) {
        String sql = "select * from player_bonus_draw where playerId = " + playerId + " order by id desc;";
        return JdbcOrm.getInstance().getListBean(sql, PlayerBonusDraw.class);
    }

    public void insert(Map<String, Object> parameterMap) {
        JdbcOrm.getInstance().insert("player_bonus_draw", parameterMap);
    }
}
