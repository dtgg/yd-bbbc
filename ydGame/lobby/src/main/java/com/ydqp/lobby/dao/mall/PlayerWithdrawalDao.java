package com.ydqp.lobby.dao.mall;

import com.cfq.jdbc.JdbcOrm;
import com.ydqp.common.data.Total;
import com.ydqp.common.entity.PlayerWithdrawal;
import com.ydqp.lobby.constant.WithdrawalStatus;

import java.util.List;
import java.util.Map;

public class PlayerWithdrawalDao {

    private PlayerWithdrawalDao() {}

    private static PlayerWithdrawalDao instance = new PlayerWithdrawalDao();

    public static PlayerWithdrawalDao getInstance() {
        if (instance == null) instance = new PlayerWithdrawalDao();
        return instance;
    }

    public void savePlayerWithdrawal(Map<String, Object> params) {
        JdbcOrm.getInstance().insert("player_withdrawal", params);
    }

    public int withdrawalCount(long playerId, int zero) {
        String sql = "select count(1) as total from player_withdrawal where status = 1 and playerId = " + playerId + " and createTime >= " + zero + ";";
        Total total = (Total) JdbcOrm.getInstance().getBean(sql, Total.class);
        return total.getTotal();
    }

    public List<PlayerWithdrawal> page(long playerId, Integer offset, Integer limit) {
        String sql = "select * from player_withdrawal where playerId = " + playerId + " order by createTime desc";
        if (offset != null && limit != null) {
            sql += " limit " + offset + "," + limit;
        }
        sql += ";";
        List<PlayerWithdrawal> page = JdbcOrm.getInstance().getListBean(sql, PlayerWithdrawal.class);
        return page;
    }
}
