package com.ydqp.common.cache;

import com.cfq.redis.JedisUtil;
import redis.clients.jedis.Jedis;

public class RankCache {

    private RankCache() {}

    public static RankCache instance = new RankCache();

    public static RankCache getInstance() {
        if (instance == null) {
            instance = new RankCache();
        }
        return instance;
    }

    private static final long OFFSET = 10000000000L;
    private static final long MAX_TIME = 9999999999L;

    public void addRank(String key, long playerId, double point) {
        Jedis jedis = JedisUtil.getInstance().getJedis();
        try {
            point = point * OFFSET + MAX_TIME - ((int)(System.currentTimeMillis()/1000));
            jedis.zadd(key, point, String.valueOf(playerId));

            if (jedis.zcard(key) > 9999) {
                jedis.zremrangeByRank(key, 0, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JedisUtil.getInstance().closeJedis(jedis);
        }
    }
}
