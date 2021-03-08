package com.ydqp.lobby.dao;

import com.cfq.jdbc.JdbcOrm;
import com.ydqp.common.entity.GameVersion;

public class GetVersionDao {
    private GetVersionDao() {}

    public static GetVersionDao instance;

    public static GetVersionDao getInstance() {
        if (instance == null) instance = new GetVersionDao();
        return instance;
    }

    public GameVersion getGameVersion(int appId) {
        String sql = "select * from game_version where appId = " + appId + ";";
        GameVersion gameVersion = (GameVersion) JdbcOrm.getInstance().getBean(sql, GameVersion.class);
        return gameVersion;
    }

}
