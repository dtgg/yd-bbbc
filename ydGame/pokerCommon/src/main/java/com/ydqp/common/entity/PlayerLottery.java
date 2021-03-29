package com.ydqp.common.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author cfq
 */
public class PlayerLottery implements Serializable {
  
       public static final long serialVersionUID = 1598515239197l;
       private Map<String,Object> entityMap = new HashMap<String,Object>(16,1);
	   	public static final String ID_NAME = "id";
	public static final String PLAYERID_NAME = "playerId";
	public static final String TYPE_NAME = "type";
	public static final String LOTTERYID_NAME = "lotteryId";
	public static final String PERIOD_NAME = "period";
	public static final String SELECT_NAME = "selected";
	public static final String NUMBER_NAME = "number";
	public static final String PAY_NAME = "pay";
	public static final String FEE_NAME = "fee";
	public static final String STATUS_NAME = "status";
	public static final String AWARD_NAME = "award";
	public static final String CREATETIME_NAME = "createTime";
	public static final String OPENTIME_NAME = "openTime";
	public static final String RESULT_NAME = "result";
	public static final String APPID_NAME = "appId";
	public static final String KFID_NAME = "kfId";
	public static final String ISVIR_NAME = "isVir";
	public static final String REGISTERTIME_NAME = "registerTime";



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
	private int type;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
		entityMap.put(TYPE_NAME, type);
	}
	private int lotteryId;
	public int getLotteryId() {
	return lotteryId;
	}
 	 public void setLotteryId(int lotteryId){
	 this.lotteryId = lotteryId;
	 entityMap.put(LOTTERYID_NAME, lotteryId);
	}
	private String period;
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
		entityMap.put(PERIOD_NAME, period);
	}
	private Integer selected;
	public Integer getSelected() {
	return selected;
	}
 	 public void setSelected(Integer selected){
	 this.selected = selected;
	 entityMap.put(SELECT_NAME, selected);
	}
	private String number;
	public String getNumber() {
	return number;
	}
 	 public void setNumber(String number){
	 this.number = number;
	 entityMap.put(NUMBER_NAME, number);
	}
	private BigDecimal pay;
	public BigDecimal getPay() {
	return pay;
	}
 	 public void setPay(BigDecimal pay){
	 this.pay = pay;
	 entityMap.put(PAY_NAME, pay);
	}
	private BigDecimal fee;
	public BigDecimal getFee() {
	return fee;
	}
 	 public void setFee(BigDecimal fee){
	 this.fee = fee;
	 entityMap.put(FEE_NAME, fee);
	}
	private int status;
	public int getStatus() {
	return status;
	}
 	 public void setStatus(int status){
	 this.status = status;
	 entityMap.put(STATUS_NAME, status);
	}
	private BigDecimal award;
	public BigDecimal getAward() {
	return award;
	}
 	 public void setAward(BigDecimal award){
	 this.award = award;
	 entityMap.put(AWARD_NAME, award);
	}
	private int createTime;
	public int getCreateTime() {
	return createTime;
	}
 	 public void setCreateTime(int createTime){
	 this.createTime = createTime;
	 entityMap.put(CREATETIME_NAME, createTime);
	}
	private int openTime;
	public int getOpenTime() {
	return openTime;
	}
 	 public void setOpenTime(int openTime){
	 this.openTime = openTime;
	 entityMap.put(OPENTIME_NAME, openTime);
	}
	private String result;
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
		entityMap.put(RESULT_NAME, result);
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
	private int registerTime;
	public int getRegisterTime() {
		return registerTime;
	}
	public void setRegisterTime(int registerTime) {
		this.registerTime = registerTime;
		entityMap.put(REGISTERTIME_NAME, registerTime);
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
