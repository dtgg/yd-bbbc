package com.ydqp.common.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author cfq
 */
public class PlayerPromoteDetail implements Serializable {
  
       public static final long serialVersionUID = 1599112332066l;
       private Map<String,Object> entityMap = new HashMap<String,Object>(16,1);
	   	public static final String ID_NAME = "id";
	public static final String PLAYERID_NAME = "playerId";
	public static final String NICKNAME_NAME = "nickname";
	public static final String BETAMOUNT_NAME = "betAmount";
	public static final String SUPERIORAMOUNT_NAME = "superiorAmount";
	public static final String GRANDAMOUNT_NAME = "grandAmount";
	public static final String CREATETIME_NAME = "createTime";
	public static final String APPID_NAME = "appId";


    	private long id;
	public long getId() {
	return id;
	}
 	 public void setId(long id){
	 this.id = id;
	 entityMap.put(ID_NAME, id);
	}
	private long playerId;
	public long getPlayerId() {
	return playerId;
	}
 	 public void setPlayerId(long playerId){
	 this.playerId = playerId;
	 entityMap.put(PLAYERID_NAME, playerId);
	}
	private String nickname;
	public String getNickname() {
	return nickname;
	}
 	 public void setNickname(String nickname){
	 this.nickname = nickname;
	 entityMap.put(NICKNAME_NAME, nickname);
	}
	private BigDecimal betAmount;
	public BigDecimal getBetAmount() {
	return betAmount;
	}
 	 public void setBetAmount(BigDecimal betAmount){
	 this.betAmount = betAmount;
	 entityMap.put(BETAMOUNT_NAME, betAmount);
	}
	private BigDecimal superiorAmount;
	public BigDecimal getSuperiorAmount() {
	return superiorAmount;
	}
 	 public void setSuperiorAmount(BigDecimal superiorAmount){
	 this.superiorAmount = superiorAmount;
	 entityMap.put(SUPERIORAMOUNT_NAME, superiorAmount);
	}
	private BigDecimal grandAmount;
	public BigDecimal getGrandAmount() {
	return grandAmount;
	}
 	 public void setGrandAmount(BigDecimal grandAmount){
	 this.grandAmount = grandAmount;
	 entityMap.put(GRANDAMOUNT_NAME, grandAmount);
	}
	private int createTime;
	public int getCreateTime() {
	return createTime;
	}
 	 public void setCreateTime(int createTime){
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
