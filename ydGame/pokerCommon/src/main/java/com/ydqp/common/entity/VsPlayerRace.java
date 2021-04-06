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
