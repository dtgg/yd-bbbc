package com.ydqp.lobby.cache;

import com.cfq.redis.JedisUtil;
import redis.clients.jedis.Jedis;

public class PayCache {

    private PayCache() {}

    private static PayCache instance = new PayCache();

    public static PayCache getInstance() {
        if (instance == null) instance = new PayCache();
        return instance;
    }

    private static final String KEY = "lottery:";

    public void addBeneId(String beneId) {
        Jedis jedis = JedisUtil.getInstance().getJedis();
        try {
            jedis.set(KEY + beneId, "true");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JedisUtil.getInstance().closeJedis(jedis);
        }
    }

    public boolean existBeneId(String beneId) {
        Jedis jedis = JedisUtil.getInstance().getJedis();
        boolean exist = false;
        try {
            exist = jedis.exists(beneId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JedisUtil.getInstance().closeJedis(jedis);
        }
        return exist;
    }

    public void remove(String key) {
        Jedis jedis = JedisUtil.getInstance().getJedis();
        try {
            jedis.del(KEY + key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JedisUtil.getInstance().closeJedis(jedis);
        }
    }
}
