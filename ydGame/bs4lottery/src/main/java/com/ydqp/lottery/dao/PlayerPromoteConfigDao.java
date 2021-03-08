package com.ydqp.lottery.dao;

import com.cfq.jdbc.JdbcOrm;
import com.ydqp.common.entity.PlayerPromoteConfig;

public class PlayerPromoteConfigDao {

    private PlayerPromoteConfigDao() {}

    private static PlayerPromoteConfigDao instance;

    public static PlayerPromoteConfigDao getInstance() {
        if (instance == null)
            instance = new PlayerPromoteConfigDao();
        return instance;
    }

    public PlayerPromoteConfig getConfig() {
        String sql = "select * from player_promote_config limit 1";
        return (PlayerPromoteConfig) JdbcOrm.getInstance().getBean(sql, PlayerPromoteConfig.class);
    }
}
