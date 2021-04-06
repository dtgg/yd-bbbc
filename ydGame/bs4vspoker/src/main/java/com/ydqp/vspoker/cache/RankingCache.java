package com.ydqp.vspoker.cache;

import com.alibaba.fastjson.JSON;
import com.cfq.redis.JedisUtil;
import com.ydqp.common.sendProtoMsg.vspoker.SVsPlayerRankData;
import redis.clients.jedis.Jedis;

import java.util.Set;

public class RankingCache {

    private RankingCache() {}

    private static final RankingCache instance = new RankingCache();

    public static RankingCache getInstance() {
        return instance;
    }

    private static final String RANKING_KEY = "RANKING:";

    public void addRank(int raceId, SVsPlayerRankData data) {
        long currentTimeMillis = System.currentTimeMillis();
        double decimal = currentTimeMillis / 10e12;
        double point = data.getPoint() + decimal;

        Jedis jedis = JedisUtil.getInstance().getJedis();
        try {
            jedis.zadd(RANKING_KEY + raceId, point, JSON.toJSONString(data));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JedisUtil.getInstance().closeJedis(jedis);
        }
    }

    public Set<String> getRankInfo(int raceId) {
        Jedis jedis = JedisUtil.getInstance().getJedis();
        Set<String> data = null;
        try {
            data = jedis.zrange(RANKING_KEY + raceId, 0, -1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JedisUtil.getInstance().closeJedis(jedis);
        }
        return data;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            SVsPlayerRankData sVsPlayerRankData = new SVsPlayerRankData();
            sVsPlayerRankData.setPlayerId((long) i);
            String format = String.format("%03d", i);
            sVsPlayerRankData.setPlayerName("0000000" + format);
            sVsPlayerRankData.setBonus((double) i);
            sVsPlayerRankData.setPoint(i);
            RankingCache.getInstance().addRank(4, sVsPlayerRankData);
        }
    }
}
