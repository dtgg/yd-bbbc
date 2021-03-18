package com.ydqp.lottery.Cache;

import com.alibaba.fastjson.JSONObject;
import com.cfq.redis.JedisUtil;
import com.ydqp.common.sendProtoMsg.lottery.LotteryDrawNum;
import redis.clients.jedis.Jedis;

public class LotteryCache {

    public static final String DRAW_INFO_KEY = "drawInfo:";

    public static final String RACING_SEED_KEY = "RACINGSEED";

    public static final String MAINTAIN_TIME_KEY = "MAINTAIN_TIME";

    private LotteryCache() {}

    private static LotteryCache instance;

    public static LotteryCache getInstance() {
        if (instance == null) {
            instance = new LotteryCache();
        }
        return instance;
    }

    public void addDrawInfo(String key, LotteryDrawNum lotteryDrawNum) {
        Jedis jedis = JedisUtil.getInstance().getJedis();
        try {
            jedis.set(key, JSONObject.toJSONString(lotteryDrawNum));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JedisUtil.getInstance().closeJedis(jedis);
        }
    }

    public LotteryDrawNum getDrawInfo(String key) {
        Jedis jedis = JedisUtil.getInstance().getJedis();
        LotteryDrawNum lotteryDrawNum = new LotteryDrawNum();
        try {
            String data = jedis.get(key);
            lotteryDrawNum = JSONObject.parseObject(data, LotteryDrawNum.class);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JedisUtil.getInstance().closeJedis(jedis);
        }
        return lotteryDrawNum;
    }

    public String getRacingSeed() {
        Jedis jedis = JedisUtil.getInstance().getJedis();
        String data = null;
        try {
            data = jedis.get(RACING_SEED_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JedisUtil.getInstance().closeJedis(jedis);
        }
        return data;
    }

    public void setRacingSeed(String data) {
        Jedis jedis = JedisUtil.getInstance().getJedis();
        try {
            jedis.set(RACING_SEED_KEY, data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JedisUtil.getInstance().closeJedis(jedis);
        }
    }

    public String getMaintainTime() {
        Jedis jedis = JedisUtil.getInstance().getJedis();
        String time = null;
        try {
            time = jedis.get(MAINTAIN_TIME_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JedisUtil.getInstance().closeJedis(jedis);
        }
        return time;
    }

    public void setMaintainTime(String time) {
        Jedis jedis = JedisUtil.getInstance().getJedis();
        try {
            jedis.setex(MAINTAIN_TIME_KEY, 24 * 60 * 60, time);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JedisUtil.getInstance().closeJedis(jedis);
        }
    }
}
