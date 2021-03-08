package com.ydqp.lobby.dao;

import com.cfq.jdbc.JdbcOrm;
import com.ydqp.common.entity.GameSwitch;

import java.util.List;

public class GameSwitchDao {
    private GameSwitchDao() {}

    public static GameSwitchDao instance;

    public static GameSwitchDao getInstance() {
        if (instance == null) instance = new GameSwitchDao();
        return instance;
    }

    public List<GameSwitch> getGameSwitch() {
        String sql = "select * from game_switch where status = 1;";
        List<GameSwitch> gameSwitchs = JdbcOrm.getInstance().getListBean(sql, GameSwitch.class);

        return gameSwitchs;
    }
}
