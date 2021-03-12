package com.ydqp.common.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author cfq
 */
public class PlayerWithdrawal implements Serializable {
  
       public static final long serialVersionUID = 1594853641805l;
       private Map<String,Object> entityMap = new HashMap<String,Object>(16,1);
	   	public static final String ID_NAME = "id";
	public static final String PLAYERID_NAME = "playerId";
	public static final String NAME_NAME = "name";
	public static final String ACCNO_NAME = "accNo";
	public static final String IFSC_NAME = "ifsc";
	public static final String MOBILE_NAME = "mobile";
	public static final String AMOUNT_NAME = "amount";
	public static final String TRANSFERID_NAME = "transferId";
	public static final String REFERENCEID_NAME = "referenceId";
	public static final String STATUS_NAME = "status";
	public static final String ERRORMSG_NAME = "errorMsg";
	public static final String CREATETIME_NAME = "createTime";
	public static final String APPID_NAME = "appId";
	public static final String REGISTERTIME_NAME = "registerTime";
	public static final String PAYCHANNEL_NAME = "payChannel";
	public static final String KFID_NAME = "kfId";


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
	private String name;
	public String getName() {
	return name;
	}
 	 public void setName(String name){
	 this.name = name;
	 entityMap.put(NAME_NAME, name);
	}
	private String accNo;
	public String getAccNo() {
	return accNo;
	}
 	 public void setAccNo(String accNo){
	 this.accNo = accNo;
	 entityMap.put(ACCNO_NAME, accNo);
	}
	private String ifsc;
	public String getIfsc() {
	return ifsc;
	}
 	 public void setIfsc(String ifsc){
	 this.ifsc = ifsc;
	 entityMap.put(IFSC_NAME, ifsc);
	}
	private String mobile;
	public String getMobile() {
	return mobile;
	}
 	 public void setMobile(String mobile){
	 this.mobile = mobile;
	 entityMap.put(MOBILE_NAME, mobile);
	}
	private double amount;
	public double getAmount() {
	return amount;
	}
 	 public void setAmount(double amount){
	 this.amount = amount;
	 entityMap.put(AMOUNT_NAME, amount);
	}
	private String transferId;
	public String getTransferId() {
	return transferId;
	}
 	 public void setTransferId(String transferId){
	 this.transferId = transferId;
	 entityMap.put(TRANSFERID_NAME, transferId);
	}
	private String referenceId;
	public String getReferenceId() {
	return referenceId;
	}
 	 public void setReferenceId(String referenceId){
	 this.referenceId = referenceId;
	 entityMap.put(REFERENCEID_NAME, referenceId);
	}
	private int status;
	public int getStatus() {
	return status;
	}
 	 public void setStatus(int status){
	 this.status = status;
	 entityMap.put(STATUS_NAME, status);
	}
	private String errorMsg;
	public String getErrorMsg() {
	return errorMsg;
	}
 	 public void setErrorMsg(String errorMsg){
	 this.errorMsg = errorMsg;
	 entityMap.put(ERRORMSG_NAME, errorMsg);
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
	private int registerTime;
	public int getRegisterTime() {
		return registerTime;
	}
	public void setRegisterTime(int registerTime) {
		this.registerTime = registerTime;
		entityMap.put(REGISTERTIME_NAME, registerTime);
	}
	private String payChannel;
	public String getPayChannel() {
		return payChannel;
	}
	public void setPayChannel(String payChannel) {
		this.payChannel = payChannel;
		entityMap.put(PAYCHANNEL_NAME, payChannel);
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
    public String toString(){
        return this.entityMap.toString();
    }
}
