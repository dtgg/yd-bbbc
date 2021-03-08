package com.ydqp.common.dao;

import com.cfq.jdbc.JdbcOrm;
import com.ydqp.common.entity.PlayerRewardHistory;

import java.util.List;
import java.util.Map;

public class PlayerRewardHistoryDao {

    private PlayerRewardHistoryDao() {}

    private static PlayerRewardHistoryDao instance = new PlayerRewardHistoryDao();

    public static PlayerRewardHistoryDao getInstance() {
        return instance;
    }

    public void insert(Map<String, Object> params) {
        JdbcOrm.getInstance().insert("player_reward_history", params);
    }

    public List<PlayerRewardHistory> findBySourceAndPlayerId(int rewardSource, long playerId) {
        String sql = "select * from player_reward_history where rewardSource = " + rewardSource + " and playerId = " + playerId + ";";
        return JdbcOrm.getInstance().getListBean(sql, PlayerRewardHistory.class);
    }

    public PlayerRewardHistory findByTaskIdAndPlayerId(int taskId, long playerId) {
        String sql = "select * from player_reward_history where taskId = " + taskId + " and playerId = " + playerId + ";";
        return (PlayerRewardHistory) JdbcOrm.getInstance().getBean(sql, PlayerRewardHistory.class);
    }
}
