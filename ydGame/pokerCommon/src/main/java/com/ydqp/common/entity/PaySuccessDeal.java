package com.ydqp.common.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author cfq
 */
public class PaySuccessDeal implements Serializable {
  
       public static final long serialVersionUID = 1595130182865l;
       private Map<String,Object> entityMap = new HashMap<String,Object>(16,1);
	   	public static final String ID_NAME = "id";
	public static final String PLAYERID_NAME = "playerId";
	public static final String ORDERID_NAME = "orderId";
	public static final String TYPE_NAME = "type";
	public static final String POINT_NAME = "point";
	public static final String ISDEAL_NAME = "isDeal";
	public static final String CREATETIME_NAME = "createTime";
	public static final String PAYTYPE_NAME = "payType";


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
	private int orderId;
	public int getOrderId() {
	return orderId;
	}
 	 public void setOrderId(int orderId){
	 this.orderId = orderId;
	 entityMap.put(ORDERID_NAME, orderId);
	}
	private int type;
	public int getType() {
	return type;
	}
 	 public void setType(int type){
	 this.type = type;
	 entityMap.put(TYPE_NAME, type);
	}
	private double point;
	public double getPoint() {
	return point;
	}
 	 public void setPoint(double point){
	 this.point = point;
	 entityMap.put(POINT_NAME, point);
	}
	private int isDeal;
	public int getIsDeal() {
	return isDeal;
	}
 	 public void setIsDeal(int isDeal){
	 this.isDeal = isDeal;
	 entityMap.put(ISDEAL_NAME, isDeal);
	}
	private int createTime;
	public int getCreateTime() {
		return createTime;
	}
	public void setCreateTime(int createTime) {
		this.createTime = createTime;
		entityMap.put(CREATETIME_NAME, createTime);
	}
	private int payType;
	public int getPayType() {
		return payType;
	}
	public void setPayType(int payType) {
		this.payType = payType;
		entityMap.put(PAYTYPE_NAME, payType);
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
