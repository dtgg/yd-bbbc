package com.ydqp.lobby.dao;

import com.cfq.jdbc.JdbcOrm;
import com.ydqp.common.entity.Player;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class PlayerLoginDao {

    public static PlayerLoginDao instance;

    public static PlayerLoginDao getInstance() {
        if (instance == null) {
            instance = new PlayerLoginDao();
        }
        return instance;
    }

    private static final String select_player = "select * from player where playerName = '";
    private static final String select_player_Fb = "select * from player where fbUserId = '";

    public Player selectPlayerByPN(String playerName) {
        String sql = select_player + playerName + "' ;";
        Player player = (Player) JdbcOrm.getInstance().getBean(sql, Player.class);
        return player;
    }

    public long insertPlayer(Map<String, Object> params) {
        long id = (Long) JdbcOrm.getInstance().insertReturnPrimkey("player", params);

        return id;
    }

    private static final String query_player = "select * from player where playerName = ";

    public Player queryByCondition(String queryCondition) {
        String sql = query_player + "'" + queryCondition + "' or id = '" + queryCondition + "'" + ";";
        return (Player) JdbcOrm.getInstance().getBean(sql, Player.class);
    }

    public void updatePlayerCoinPoint(double coinPoint, long id) {
        String sql = "update player set coinPoint = coinPoint + " + coinPoint + " where id = " + id + ";";
        JdbcOrm.getInstance().update(sql);
    }

    public void updatePlayerZjPoint(double zjPoint, long id) {
        String sql = "update player set zjPoint = zjPoint + " + zjPoint + " where id = " + id + ";";
        JdbcOrm.getInstance().update(sql);
    }

    public void updatePlayerGrade(long playerId, int grade, double experience) {
        String sql = "update player set grade = grade + " + grade + ", experience = experience + " + experience +
                " where id = " + playerId + ";";
        JdbcOrm.getInstance().update(sql);
    }

    private static final String FIND_ALL_BY_IDS = "select * from player where id in ";

    public List<Player> findAllByIds(Set<String> playIds) {
        String ids = "";
        for (String playId : playIds) {
            if (StringUtils.isBlank(ids)) ids += playId;
            else ids += "," + playId;
        }
        ids = "(" + ids + ")";

        String sql = FIND_ALL_BY_IDS + ids + ";";
        return JdbcOrm.getInstance().getListBean(sql, Player.class);
    }

    public Player selectPlayerByFb(String fbUserId) {
        String sql = select_player_Fb + fbUserId + "' ;";
        Player player = (Player) JdbcOrm.getInstance().getBean(sql, Player.class);
        return player;
    }

    public void bindFb(long playerId, String fbUserId, String nickname) {
        String sql = "update player set fbUserId = '" + fbUserId + "',fbBind = '" + 1 + "', nickname = '" + nickname + "'"+ " where id = " + playerId +";" ;
        JdbcOrm.getInstance().update(sql);
    }

    public Player selectPlayerById(long playerId) {
        String sql = "select * from player where id = " + playerId + ";";
        Player player = (Player) JdbcOrm.getInstance().getBean(sql, Player.class);
        return player;
    }

    public void updateOnLineTime (long id, int time) {
        String sql = "update player set onLineTime = "+ time + " where id = " + id + ";";
        JdbcOrm.getInstance().update(sql);
    }
}
