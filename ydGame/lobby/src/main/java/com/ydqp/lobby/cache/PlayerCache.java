package com.ydqp.lobby.cache;

import com.alibaba.fastjson.JSONObject;
import com.cfq.redis.JedisUtil;
import com.ydqp.common.data.PlayerData;
import redis.clients.jedis.Jedis;

public class PlayerCache {

    private final  String playerKey = "lottery:playerDataByConnId:";
    private final  String playerKeyByPlayerId = "lottery:playerDataByPlayerId:";
    private final  String playerFbKey= "lottery:playerFbKey:";

    private static final String VERIFICATION_CODE = "verifyCode:";

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


    public void addPlayerFbToken (String key , String accessToken) {
        Jedis jedis = JedisUtil.getInstance().getJedis();
        try {
            jedis.set(playerFbKey + key, accessToken);
            jedis.expire(playerFbKey + key, 3600 * 720);
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            JedisUtil.getInstance().closeJedis(jedis);
        }
    }


    public String getPlayerFbToken (String key) {
        Jedis jedis = JedisUtil.getInstance().getJedis();
        String accessToken = "";
        try {
            if (jedis.exists(playerFbKey + key)) {
                accessToken = jedis.get(playerFbKey + key);
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            JedisUtil.getInstance().closeJedis(jedis);
        }
        return accessToken;
    }

    public void delPlayerFbToken (String key ) {
        Jedis jedis = JedisUtil.getInstance().getJedis();
        try {
            jedis.del(playerFbKey + key);
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            JedisUtil.getInstance().closeJedis(jedis);
        }
    }

    public void addVerificationCode (String key , String verificationCode) {
        Jedis jedis = JedisUtil.getInstance().getJedis();
        try {
            jedis.set(VERIFICATION_CODE + key, verificationCode);
            jedis.expire(VERIFICATION_CODE + key, 5 * 60);
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            JedisUtil.getInstance().closeJedis(jedis);
        }
    }

    public String getVerificationCode(String key) {
        String verificationCode = "";

        Jedis jedis = JedisUtil.getInstance().getJedis();
        try {
            if (jedis.exists(VERIFICATION_CODE + key)) {
                verificationCode = jedis.get(VERIFICATION_CODE + key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JedisUtil.getInstance().closeJedis(jedis);
        }
        return verificationCode;
    }

    public int getVerificationCodeExpire(String key) {
        int expire = 0;
        Jedis jedis = JedisUtil.getInstance().getJedis();
        try {
            if (jedis.exists(VERIFICATION_CODE + key)) {
                expire = Math.toIntExact(jedis.ttl(VERIFICATION_CODE + key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JedisUtil.getInstance().closeJedis(jedis);
        }
        return expire;
    }
}
