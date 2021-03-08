package com.ydqp.common.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author cfq
 */
public class ProductBuyHistory implements Serializable {
  
       public static final long serialVersionUID = 1594042043357l;
       private Map<String,Object> entityMap = new HashMap<String,Object>(16,1);
	   	public static final String ID_NAME = "id";
	public static final String PLAYERID_NAME = "playerId";
	public static final String PROMOTIONID_NAME = "promotionId";
	public static final String BUYNUM_NAME = "buyNum";
	public static final String CREATETIME_NAME = "createTime";


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
	private int promotionId;
	public int getPromotionId() {
	return promotionId;
	}
 	 public void setPromotionId(int promotionId){
	 this.promotionId = promotionId;
	 entityMap.put(PROMOTIONID_NAME, promotionId);
	}
	private int buyNum;
	public int getBuyNum() {
	return buyNum;
	}
 	 public void setBuyNum(int buyNum){
	 this.buyNum = buyNum;
	 entityMap.put(BUYNUM_NAME, buyNum);
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
