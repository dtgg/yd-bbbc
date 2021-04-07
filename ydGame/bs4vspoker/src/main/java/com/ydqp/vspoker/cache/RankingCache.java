package com.ydqp.vspoker.cache;

import com.cfq.redis.JedisUtil;
import redis.clients.jedis.Jedis;

import java.util.Set;

public class RankingCache {

    private RankingCache() {}

    private static final RankingCache instance = new RankingCache();

    public static RankingCache getInstance() {
        return instance;
    }

    private static final String RANKING_KEY = "RANKING:";

    public void addRank(int raceId, Double point, long playerId) {
        long currentTimeMillis = System.currentTimeMillis();
        double decimal = currentTimeMillis / 10e13;
        point += decimal;

        Jedis jedis = JedisUtil.getInstance().getJedis();
        try {
            jedis.zadd(RANKING_KEY + raceId, point, String.valueOf(playerId));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JedisUtil.getInstance().closeJedis(jedis);
        }
    }

    public Set<String> getRankInfo(int raceId, int start, int stop) {
        Jedis jedis = JedisUtil.getInstance().getJedis();
        Set<String> data = null;
        try {
            data = jedis.zrevrange(RANKING_KEY + raceId, start, stop);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JedisUtil.getInstance().closeJedis(jedis);
        }
        return data;
    }

    public Long getRankNo(int raceId, long playerId) {
        Jedis jedis = JedisUtil.getInstance().getJedis();
        Long zrank = null;
        try {
            zrank = jedis.zrevrank(RANKING_KEY + raceId, String.valueOf(playerId));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JedisUtil.getInstance().closeJedis(jedis);
        }
        return zrank;
    }

    public void delRankInfo(Integer raceId) {
        Jedis jedis = JedisUtil.getInstance().getJedis();
        try {
            jedis.del(RANKING_KEY + raceId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JedisUtil.getInstance().closeJedis(jedis);
        }
    }

    public static void main(String[] args) {
        System.out.println(RankingCache.getInstance().getRankNo(12, 6));
    }
}
