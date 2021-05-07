package com.ydqp.vspoker.cache;

import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.redis.JedisUtil;
import redis.clients.jedis.Jedis;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public class RankingCache {

    private static final Logger logger = LoggerFactory.getLogger(RankingCache.class);

    private RankingCache() {}

    private static final RankingCache instance = new RankingCache();

    public static RankingCache getInstance() {
        return instance;
    }

    private static final String RANKING_KEY = "RANKING:";
    private static final String RACEJOIN_KEY = "RACEJOIN:";

    public void addRank(int raceId, Double point, long playerId) {
        long currentTimeMillis = System.currentTimeMillis();
        double decimal = currentTimeMillis / 10e13;
        point += decimal;

        Jedis jedis = JedisUtil.getInstance().getJedis();
        try {
            Long zadd = jedis.zadd(RANKING_KEY + raceId, point, String.valueOf(playerId));
            logger.info("加入缓存：raceId：{}，playerId：{}，zj:{}, zadd:{}", raceId, playerId, point, zadd);
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

    public void setRaceJoin(long playerId) {
        Jedis jedis = JedisUtil.getInstance().getJedis();
        try {
            jedis.set(RACEJOIN_KEY + getTime() + ":" + playerId, "1");
            jedis.expire(RACEJOIN_KEY + getTime() + ":" + playerId, 24 * 3600);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JedisUtil.getInstance().closeJedis(jedis);
        }
    }

    public boolean exitRaceJoin(long playerId) {
        Jedis jedis = JedisUtil.getInstance().getJedis();
        boolean isEx = true;
        try {
            isEx = jedis.exists(RACEJOIN_KEY + getTime() + ":" + playerId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JedisUtil.getInstance().closeJedis(jedis);
        }

        return isEx;
    }

    public String getTime () {
        ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return formatter.format(zdt);
    }

    public static void main(String[] args) {
        ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.println(formatter.format(zdt));

        //System.out.println(RankingCache.getInstance().getRankNo(12, 6));
    }
}
