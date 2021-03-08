package com.ydqp.lobby.cache;

import com.cfq.redis.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

public class MallCache {

    private MallCache() {}

    private static MallCache instance = new MallCache();

    public static MallCache getInstance() {
        if (instance == null) instance = new MallCache();
        return instance;
    }

    private static final String KEY = "lottery:";

    public String getCashFreeToken(String key) {
        Jedis jedis = JedisUtil.getInstance().getJedis();
        String token = null;
        try {
            token = jedis.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JedisUtil.getInstance().closeJedis(jedis);
        }
        return token;
    }

    public void setCashFreeToken(String key, String token, int expiry) {
        Jedis jedis = JedisUtil.getInstance().getJedis();
        try {
            jedis.set(key, token, new SetParams().ex(expiry));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JedisUtil.getInstance().closeJedis(jedis);
        }
    }

    public Long getWithdrawalCount(String key) {
        Jedis jedis = JedisUtil.getInstance().getJedis();
        Long withdrawalCount = null;
        try {
            withdrawalCount = Long.parseLong(jedis.get(KEY + key));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JedisUtil.getInstance().closeJedis(jedis);
        }
        return withdrawalCount;
    }

    public Long incrWithdrawalCount(String key) {
        Jedis jedis = JedisUtil.getInstance().getJedis();
        Long withdrawalCount = 1L;
        try {
            withdrawalCount = jedis.incr(KEY + key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JedisUtil.getInstance().closeJedis(jedis);
        }
        return withdrawalCount;
    }
}
