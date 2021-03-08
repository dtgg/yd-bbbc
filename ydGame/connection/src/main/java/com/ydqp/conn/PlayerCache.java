package com.ydqp.conn;

import com.alibaba.fastjson.JSONObject;
import com.cfq.redis.JedisUtil;
import com.ydqp.common.data.PlayerData;
import redis.clients.jedis.Jedis;

public class PlayerCache {

    private final  String playerKey = "playerDataByConnId:";

    private final  String playerKeyByPlayerId = "playerDataByPlayerId:";

    private PlayerCache(){
    }

    public static PlayerCache instance = new PlayerCache();

    public static PlayerCache getInstance() {
        if(instance == null) {
            instance = new PlayerCache();
        }
        return instance;
    }


    public void addPlayer (long key , PlayerData playerData) {
        Jedis jedis = JedisUtil.getInstance().getJedis();
        try {
            jedis.set(playerKey + key , JSONObject.toJSONString(playerData));
        }catch (Exception e) {
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
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            JedisUtil.getInstance().closeJedis(jedis);
        }
        return playerData;
    }

    public void addPlayerByPlayerId (long key , PlayerData playerData) {
        Jedis jedis = JedisUtil.getInstance().getJedis();
        try {
            jedis.set(playerKeyByPlayerId + key , JSONObject.toJSONString(playerData));
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            JedisUtil.getInstance().closeJedis(jedis);
        }
    }

    public PlayerData getPlayerByPlayerID (long key) {
        Jedis jedis = JedisUtil.getInstance().getJedis();
        PlayerData playerData = new PlayerData();
        try {
            String data = jedis.get(playerKeyByPlayerId + key);
            playerData = JSONObject.parseObject(data, PlayerData.class);
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            JedisUtil.getInstance().closeJedis(jedis);
        }
        return playerData;
    }

    public void delPlayerByConnId (long connId) {
        Jedis jedis = JedisUtil.getInstance().getJedis();
        try {
            jedis.del(playerKey + connId);
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            JedisUtil.getInstance().closeJedis(jedis);
        }
    }

    public void delPlayerByPlayerId (long playerId) {
        Jedis jedis = JedisUtil.getInstance().getJedis();
        try {
            jedis.del(playerKeyByPlayerId + playerId);
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            JedisUtil.getInstance().closeJedis(jedis);
        }
    }

}
