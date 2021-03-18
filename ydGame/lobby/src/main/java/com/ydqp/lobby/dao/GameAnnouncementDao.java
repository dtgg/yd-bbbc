package com.ydqp.lobby.dao;

import com.cfq.jdbc.JdbcOrm;
import com.ydqp.common.entity.GameAnnouncement;
import com.ydqp.common.entity.GameVersion;

public class GameAnnouncementDao {
    private GameAnnouncementDao() {}

    public static GameAnnouncementDao instance;

    public static GameAnnouncementDao getInstance() {
        if (instance == null) instance = new GameAnnouncementDao();
        return instance;
    }

    public GameAnnouncement getGameAnnouncement(int appId) {
        String sql = "select * from game_announcement where appId = " + appId + ";";
        GameAnnouncement gameAnnouncement = (GameAnnouncement) JdbcOrm.getInstance().getBean(sql, GameAnnouncement.class);
        return gameAnnouncement;
    }
}
