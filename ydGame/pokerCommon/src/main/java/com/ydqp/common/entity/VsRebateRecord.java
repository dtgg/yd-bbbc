package com.ydqp.common.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class VsRebateRecord implements Serializable {

    public static final long serialVersionUID = 1616661867772l;
    private Map<String, Object> entityMap = new HashMap<String, Object>(16, 1);
    public static final String ID_NAME = "id";
    public static final String PLAYERID_NAME = "playerId";
    public static final String PLAYERNAME_NAME = "playerName";
    public static final String NICKNAME_NAME = "nickname";
    public static final String REBATE_NAME = "rebate";
    public static final String CREATETIME_NAME = "createTime";
    public static final String APPID_NAME = "appId";
    public static final String KFID_NAME = "kfId";


    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        entityMap.put(ID_NAME, id);
    }

    private long playerId;

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
        entityMap.put(PLAYERID_NAME, playerId);
    }

    private String playerName;

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
        entityMap.put(PLAYERNAME_NAME, playerName);
    }

    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
        entityMap.put(NICKNAME_NAME, nickname);
    }

    private float rebate;

    public float getRebate() {
        return rebate;
    }

    public void setRebate(float rebate) {
        this.rebate = rebate;
        entityMap.put(REBATE_NAME, rebate);
    }

    private int createTime;

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
        entityMap.put(CREATETIME_NAME, createTime);
    }

    private int appId;

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
        entityMap.put(APPID_NAME, appId);
    }

    private long kfId;

    public long getKfId() {
        return kfId;
    }

    public void setKfId(long kfId) {
        this.kfId = kfId;
        entityMap.put(KFID_NAME, kfId);
    }

    public Object getValueByFieldName(String fieldName) {
        return this.entityMap.get(fieldName);
    }

    public Map<String, Object> getParameterMap() {
        return this.entityMap;
    }

    @Override
    public String toString() {
        return this.entityMap.toString();
    }
}
