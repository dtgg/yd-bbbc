package com.ydqp.lottery.Cache;

import com.alibaba.fastjson.JSONObject;
import com.cfq.redis.JedisUtil;
import com.ydqp.common.sendProtoMsg.lottery.LotteryDrawNum;
import redis.clients.jedis.Jedis;

public class LotteryCache {

    private static final String DRAW_INFO_KEY = "drawInfo";

    private LotteryCache() {}

    private static LotteryCache instance;

    public static LotteryCache getInstance() {
        if (instance == null) {
            instance = new LotteryCache();
        }
        return instance;
    }

    public void addDrawInfo(LotteryDrawNum lotteryDrawNum) {
        Jedis jedis = JedisUtil.getInstance().getJedis();
        try {
            jedis.set(DRAW_INFO_KEY, JSONObject.toJSONString(lotteryDrawNum));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JedisUtil.getInstance().closeJedis(jedis);
        }
    }

    public LotteryDrawNum getDrawInfo() {
        Jedis jedis = JedisUtil.getInstance().getJedis();
        LotteryDrawNum lotteryDrawNum = new LotteryDrawNum();
        try {
            String data = jedis.get(DRAW_INFO_KEY);
            lotteryDrawNum = JSONObject.parseObject(data, LotteryDrawNum.class);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JedisUtil.getInstance().closeJedis(jedis);
        }
        return lotteryDrawNum;
    }
}
