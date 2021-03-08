package com.ydqp.common.dao;

import com.cfq.jdbc.JdbcOrm;
import com.ydqp.common.data.Total;

public class PlayerOrderDao {

    private PlayerOrderDao() {}

    private static PlayerOrderDao instance;

    public static PlayerOrderDao getInstance() {
        if (instance == null)
            instance = new PlayerOrderDao();
        return instance;
    }

    public Total countByPlayId(long playerId) {
        String sql = "select count(1) as total from player_order where payStatus = 1 and productId >= 7 and playerId = " + playerId + ";";
        return (Total) JdbcOrm.getInstance().getBean(sql, Total.class);
    }
}
