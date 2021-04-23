package com.ydqp.common.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class VsRacePromote implements Serializable {

    public static final long serialVersionUID = 1616661867772l;
    private Map<String, Object> entityMap = new HashMap<String, Object>(16, 1);
    public static final String ID_NAME = "id";
    public static final String PLAYERID_NAME = "playerId";
    public static final String SUBID_NAME = "subId";
    public static final String PLAYERNAME_NAME = "playerName";
    public static final String NICKNAME_NAME = "nickname";
    public static final String RACEID_NAME = "raceId";
    public static final String ORDERID_NAME = "orderId";
    public static final String SIGNFEE_NAME = "signFee";
    public static final String FEE_NAME = "fee";
    public static final String STATUS_NAME = "status";
    public static final String CREATETIME_NAME = "createTime";
    public static final String BEGINTIME_NAME = "beginTime";
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

    private long subId;

    public long getSubId() {
        return subId;
    }

    public void setSubId(long subId) {
        this.subId = subId;
        entityMap.put(SUBID_NAME, subId);
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

    private int raceId;

    public int getRaceId() {
        return raceId;
    }

    public void setRaceId(int raceId) {
        this.raceId = raceId;
        entityMap.put(RACEID_NAME, raceId);
    }

    private String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
        entityMap.put(ORDERID_NAME, orderId);
    }

    private float signFee;

    public float getSignFee() {
        return signFee;
    }

    public void setSignFee(float signFee) {
        this.signFee = signFee;
        entityMap.put(SIGNFEE_NAME, signFee);
    }

    private float fee;

    public float getFee() {
        return fee;
    }

    public void setFee(float fee) {
        this.fee = fee;
        entityMap.put(FEE_NAME, fee);
    }

    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
        entityMap.put(STATUS_NAME, status);
    }

    private int createTime;

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
        entityMap.put(CREATETIME_NAME, createTime);
    }

    private int beginTime;

    public int getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(int beginTime) {
        this.beginTime = beginTime;
        entityMap.put(BEGINTIME_NAME, beginTime);
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
