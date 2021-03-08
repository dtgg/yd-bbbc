package com.ydqp.common.cache;

import com.alibaba.fastjson.JSONObject;
import com.cfq.redis.JedisUtil;
import com.ydqp.common.data.PlayerData;
import org.apache.commons.collections.CollectionUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlayerCache {

    private final String playerKey = "lottery:playerDataByConnId:";
    private final String playerKeyByPlayerId = "lottery:playerDataByPlayerId:";

    private PlayerCache() {
    }

    public static PlayerCache instance = new PlayerCache();

    public static PlayerCache getInstance() {
        if (instance == null) {
            instance = new PlayerCache();
        }
        return instance;
    }


    public void addPlayer(long key, PlayerData playerData) {
        Jedis jedis = JedisUtil.getInstance().getJedis();
        try {
            jedis.set(playerKey + key, JSONObject.toJSONString(playerData));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JedisUtil.getInstance().closeJedis(jedis);
        }
    }

    public PlayerData getPlayer(long key) {
        Jedis jedis = JedisUtil.getInstance().getJedis();
        PlayerData playerData = new PlayerData();
        try {
            String data = jedis.get(playerKey + key);
            playerData = JSONObject.parseObject(data, PlayerData.class);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JedisUtil.getInstance().closeJedis(jedis);
        }
        return playerData;
    }

    public void addPlayerByPlayerId(long key, PlayerData playerData) {
        Jedis jedis = JedisUtil.getInstance().getJedis();
        try {
            jedis.set(playerKeyByPlayerId + key, JSONObject.toJSONString(playerData));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JedisUtil.getInstance().closeJedis(jedis);
        }
    }

    public PlayerData getPlayerByPlayerID(long key) {
        Jedis jedis = JedisUtil.getInstance().getJedis();
        PlayerData playerData = new PlayerData();
        try {
            String data = jedis.get(playerKeyByPlayerId + key);
            playerData = JSONObject.parseObject(data, PlayerData.class);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JedisUtil.getInstance().closeJedis(jedis);
        }
        return playerData;
    }

    public void delPlayerByConnId(long connId) {
        Jedis jedis = JedisUtil.getInstance().getJedis();
        try {
            jedis.del(playerKey + connId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JedisUtil.getInstance().closeJedis(jedis);
        }
    }

    public void delPlayerByPlayerId(long playerId) {
        Jedis jedis = JedisUtil.getInstance().getJedis();
        try {
            jedis.del(playerKeyByPlayerId + playerId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JedisUtil.getInstance().closeJedis(jedis);
        }
    }

    public List<PlayerData> getPlayers(List<Long> keys) {
        Jedis jedis = JedisUtil.getInstance().getJedis();
        List<PlayerData> playerDataList = new ArrayList<>();
        try {
            Pipeline pipeline = jedis.pipelined();
            for (Long key : keys) {
                pipeline.get(playerKey + key);
            }
            List<Object> objects = pipeline.syncAndReturnAll();
            if (CollectionUtils.isNotEmpty(objects)) {
                playerDataList = objects.stream()
                        .map(o -> JSONObject.parseObject(String.valueOf(o), PlayerData.class))
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JedisUtil.getInstance().closeJedis(jedis);
        }
        return playerDataList;
    }

    public void addPlayers(Map<Long, PlayerData> data) {
        Jedis jedis = JedisUtil.getInstance().getJedis();
        try {
            Pipeline pipeline = jedis.pipelined();
            data.forEach((key, value) -> {
                pipeline.set(playerKey + key, JSONObject.toJSONString(value));
            });
            pipeline.sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JedisUtil.getInstance().closeJedis(jedis);
        }
    }
}
