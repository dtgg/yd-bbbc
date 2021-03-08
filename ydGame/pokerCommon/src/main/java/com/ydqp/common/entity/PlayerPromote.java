package com.ydqp.common.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author cfq
 */
public class PlayerPromote implements Serializable {
  
       public static final long serialVersionUID = 1599071163988l;
       private Map<String,Object> entityMap = new HashMap<String,Object>(16,1);
	   	public static final String ID_NAME = "id";
	public static final String PLAYERID_NAME = "playerId";
	public static final String NICKNAME_NAME = "nickname";
	public static final String SUPERIORID_NAME = "superiorId";
	public static final String GRANDID_NAME = "grandId";
	public static final String SUPERIORAMOUNT_NAME = "superiorAmount";
	public static final String GRANDAMOUNT_NAME = "grandAmount";
	public static final String ISKF_NAME = "isKf";
	public static final String KFID_NAME = "kfId";
	public static final String ISEFFECTIVE_NAME = "isEffective";
	public static final String SUBNUM_NAME = "subNum";
	public static final String SONNUM_NAME = "sonNum";
	public static final String CREATETIME_NAME = "createTime";


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
 	 public void setPlayerId(long playerId){
	 this.playerId = playerId;
	 entityMap.put(PLAYERID_NAME, playerId);
	}
	private String nickname;
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
		entityMap.put(NICKNAME_NAME, nickname);
	}

	private Long superiorId;
	public Long getSuperiorId() {
	return superiorId;
	}
 	 public void setSuperiorId(Long superiorId){
	 this.superiorId = superiorId;
	 entityMap.put(SUPERIORID_NAME, superiorId);
	}
	private Long grandId;
	public Long getGrandId() {
	return grandId;
	}
 	 public void setGrandId(Long grandId){
	 this.grandId = grandId;
	 entityMap.put(GRANDID_NAME, grandId);
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
	private int isKf;
	public int getIsKf() {
	return isKf;
	}
 	 public void setIsKf(int isKf){
	 this.isKf = isKf;
	 entityMap.put(ISKF_NAME, isKf);
	}
	private Long kfId;
	public Long getKfId() {
	return kfId;
	}
 	 public void setKfId(Long kfId){
	 this.kfId = kfId;
	 entityMap.put(KFID_NAME, kfId);
	}
	private int isEffective;
	public int getIsEffective() {
	return isEffective;
	}
 	 public void setIsEffective(int isEffective){
	 this.isEffective = isEffective;
	 entityMap.put(ISEFFECTIVE_NAME, isEffective);
	}
	private int subNum;
	public int getSubNum() {
	return subNum;
	}
 	 public void setSubNum(int subNum){
	 this.subNum = subNum;
	 entityMap.put(SUBNUM_NAME, subNum);
	}
	private int sonNum;
	public int getSonNum() {
	return sonNum;
	}
 	 public void setSonNum(int sonNum){
	 this.sonNum = sonNum;
	 entityMap.put(SONNUM_NAME, sonNum);
	}
	private int createTime;
	public int getCreateTime() {
	return createTime;
	}
 	 public void setCreateTime(int createTime){
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
    public String toString(){
        return this.entityMap.toString();
    }
}
