package com.ydqp.lobby.utils;

import com.cfq.jdbc.JdbcOrm;
import com.cfq.redis.JedisUtil;
import com.ydqp.common.entity.Player;
import redis.clients.jedis.Jedis;

import java.util.List;

public class RankInit {

    private static final long OFFSET = 10000000000L;
    private static final long MAX_TIME = 9999999999L;

    public static void main(String[] args) {
        Jedis jedis = JedisUtil.getInstance().getJedis();
        try {
            String sql = "select * from player";
            List<Player> players = JdbcOrm.getInstance().getListBean(sql, Player.class);

            players.forEach(player -> {
                double coinPoint = player.getCoinPoint() * OFFSET - ((int)(System.currentTimeMillis()/1000));
                System.out.println(coinPoint);
                jedis.zadd("COIN_POINT_RANK", coinPoint, String.valueOf(player.getId()));
                double zjPoint = player.getZjPoint() * OFFSET + MAX_TIME - ((int)(System.currentTimeMillis()/1000));
                System.out.println(zjPoint);
                jedis.zadd("ZJ_POINT_RANK", player.getZjPoint() * OFFSET + MAX_TIME - System.currentTimeMillis(), String.valueOf(player.getId()));
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JedisUtil.getInstance().closeJedis(jedis);
        }
    }
}
