package com.ydqp.common.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class VsPlayerRace implements Serializable {

	public static final long serialVersionUID = 1617409332785l;
	private Map<String,Object> entityMap = new HashMap<String,Object>(16,1);
	public static final String ID_NAME = "id";
	public static final String PLAYERID_NAME = "playerId";
	public static final String RACEID_NAME = "raceId";
	public static final String RACETYPE_NAME = "raceType";
	public static final String BASEPOINT_NAME = "basePoint";
	public static final String RANK_NAME = "rank";
	public static final String CREATETIM_NAME = "createTime";
	public static final String BONUS_NAME = "bonus";
	public static final String POINT_NAME = "point";
	public static final String APPID_NAME = "appId";
	public static final String KFID_NAME = "kfId";
	public static final String ISVIR_NAME = "isVir";


	private int id;
	public int getId() {
	return id;
	}
	public void setId(int id){
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
	private int raceId;
	public int getRaceId() {
		return raceId;
	}
	public void setRaceId(int raceId) {
		this.raceId = raceId;
		entityMap.put(RACEID_NAME, raceId);
	}
	private int raceType;
	public int getRaceType() {
		return raceType;
	}
	public void setRaceType(int raceType) {
		this.raceType = raceType;
		entityMap.put(RACETYPE_NAME, raceType);
	}
	private int basePoint;
	public int getBasePoint() {
		return basePoint;
	}
	public void setBasePoint(int basePoint) {
		this.basePoint = basePoint;
		entityMap.put(BASEPOINT_NAME, basePoint);
	}
	private int rank;
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
		entityMap.put(RANK_NAME, rank);
	}
	private int createTime;
	public int getCreateTime() {
		return createTime;
	}
	public void setCreateTime(int createTime) {
		this.createTime = createTime;
		entityMap.put(CREATETIM_NAME, createTime);
	}
	private double bonus;
	public double getBonus() {
		return bonus;
	}
	public void setBonus(double bonus) {
		this.bonus = bonus;
		entityMap.put(BONUS_NAME, bonus);
	}
	private double point;
	public double getPoint() {
		return point;
	}
	public void setPoint(double point) {
		this.point = point;
		entityMap.put(POINT_NAME, point);
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
	private int isVir;
	public int getIsVir() {
		return isVir;
	}
	public void setIsVir(int isVir) {
		this.isVir = isVir;
		entityMap.put(ISVIR_NAME, isVir);
	}

	public Object getValueByFieldName(String fieldName) {
        return this.entityMap.get(fieldName);
    }

    public Map<String, Object> getParameterMap() {
        return this.entityMap;
    }

	@Override
    public String toString(){
        return this.entityMap.toString();
    }
}
