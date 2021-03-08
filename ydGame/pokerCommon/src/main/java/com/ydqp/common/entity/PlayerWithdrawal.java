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
	public static final String BENEID_NAME = "beneId";
	public static final String TRANSFERID_NAME = "transferId";
	public static final String REFERENCEID_NAME = "referenceId";
	public static final String UTR_NAME = "utr";
	public static final String ACKNOWLEDGED_NAME = "acknowledged";
	public static final String STATUS_NAME = "status";
	public static final String ADDEDON_NAME = "addedOn";
	public static final String PROCESSEDON_NAME = "processedOn";
	public static final String ERRORMSG_NAME = "errorMsg";
	public static final String CREATETIME_NAME = "createTime";
	public static final String TRANSFERMODE_NAME = "transferMode";
	public static final String APPID_NAME = "appId";
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
	private String beneId;
	public String getBeneId() {
	return beneId;
	}
 	 public void setBeneId(String beneId){
	 this.beneId = beneId;
	 entityMap.put(BENEID_NAME, beneId);
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
	private String utr;
	public String getUtr() {
	return utr;
	}
 	 public void setUtr(String utr){
	 this.utr = utr;
	 entityMap.put(UTR_NAME, utr);
	}
	private String acknowledged;
	public String getAcknowledged() {
	return acknowledged;
	}
 	 public void setAcknowledged(String acknowledged){
	 this.acknowledged = acknowledged;
	 entityMap.put(ACKNOWLEDGED_NAME, acknowledged);
	}
	private int status;
	public int getStatus() {
	return status;
	}
 	 public void setStatus(int status){
	 this.status = status;
	 entityMap.put(STATUS_NAME, status);
	}
	private int addedOn;
	public int getAddedOn() {
	return addedOn;
	}
 	 public void setAddedOn(int addedOn){
	 this.addedOn = addedOn;
	 entityMap.put(ADDEDON_NAME, addedOn);
	}
	private int processedOn;
	public int getProcessedOn() {
	return processedOn;
	}
 	 public void setProcessedOn(int processedOn){
	 this.processedOn = processedOn;
	 entityMap.put(PROCESSEDON_NAME, processedOn);
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
	private String transferMode;
	public String getTransferMode() {
		return transferMode;
	}
	public void setTransferMode(String transferMode) {
		this.transferMode = transferMode;
		entityMap.put(TRANSFERMODE_NAME, transferMode);
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
