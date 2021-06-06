package com.ydqp.common.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cfq
 */
public class VsZjPlayerRace implements Serializable {

    public static final long serialVersionUID = 1616661867772L;
    private Map<String, Object> entityMap = new HashMap<String, Object>(16, 1);
    public static final String ID_NAME = "id";
    public static final String PLAYERID_NAME = "playerId";
    public static final String RACETYPE_NAME = "raceType";
    public static final String RACEID_NAME = "raceId";
    public static final String ROUND_NAME = "round";
    public static final String AMOUNT_NAME = "amount";
    public static final String ISAWARD_NAME = "isAward";
    public static final String BONUS_NAME = "bonus";
    public static final String CREATETIME_NAME = "createTime";


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

    private int raceType;

    public int getRaceType() {
        return raceType;
    }

    public void setRaceType(int raceType) {
        this.raceType = raceType;
        entityMap.put(RACETYPE_NAME, raceType);
    }

    private int raceId;

    public int getRaceId() {
        return raceId;
    }

    public void setRaceId(int raceId) {
        this.raceId = raceId;
        entityMap.put(RACEID_NAME, raceId);
    }

    private int round;

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
        entityMap.put(ROUND_NAME, round);
    }

    private double amount;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
        entityMap.put(AMOUNT_NAME, amount);
    }

    private int isAward;

    public int getIsAward() {
        return isAward;
    }

    public void setIsAward(int isAward) {
        this.isAward = isAward;
        entityMap.put(ISAWARD_NAME, isAward);
    }

    private double bonus;

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
        entityMap.put(BONUS_NAME, bonus);
    }

    private int createTime;

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
        entityMap.put(CREATETIME_NAME, createTime);
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
