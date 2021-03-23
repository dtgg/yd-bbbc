package com.ydqp.lobby.dao.mall;

import com.cfq.jdbc.JdbcOrm;
import com.ydqp.common.entity.PlayerOrder;
import com.ydqp.common.entity.PlayerWithdrawal;

import java.util.List;
import java.util.Map;

public class PlayerOrderDao {

    private PlayerOrderDao() {}

    private static PlayerOrderDao instance = new PlayerOrderDao();

    public static PlayerOrderDao getInstance() {
        if (instance == null) instance = new PlayerOrderDao();
        return instance;
    }

    public void saveOrder(Map<String, Object> params) {
        JdbcOrm.getInstance().insert("player_order", params);
    }

    private static final String QUERY_ORDER = "select * from player_order where txnOrderId = '";
    public PlayerOrder queryOrder(String txnOrderId) {
        String sql = QUERY_ORDER + txnOrderId + "';";
        return (PlayerOrder) JdbcOrm.getInstance().getBean(sql, PlayerOrder.class);
    }

    public void updateOrder(Object[] params) {
        String sql = "update player_order set status = ? where id = ?;";
        JdbcOrm.getInstance().updateByArray(sql, params);
    }

    public List<PlayerOrder> page(long playerId, Integer offset, Integer limit) {
        String sql = "select * from player_order where status = 1 and playerId = " + playerId + " order by createTime desc";
        if (offset != null && limit != null) {
            sql += " limit " + offset + "," + limit;
        }
        sql += ";";
        List<PlayerOrder> page = JdbcOrm.getInstance().getListBean(sql, PlayerOrder.class);
        return page;
    }
}
