package com.ydqp.common.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author cfq
 */
public class PlayerAccount implements Serializable {
  
       public static final long serialVersionUID = 1594792239215l;
       private Map<String,Object> entityMap = new HashMap<String,Object>(16,1);
	   	public static final String ID_NAME = "id";
	public static final String PLAYERID_NAME = "playerId";
	public static final String NAME_NAME = "name";
	public static final String ACCNO_NAME = "accNo";
	public static final String IFSC_NAME = "ifsc";
	public static final String MOBILE_NAME = "mobile";
	public static final String WITHDRAWING_NAME = "withdrawing";
	public static final String PAYMOBILE_NAME = "payMobile";
	public static final String EMAIL_NAME = "email";
	public static final String BENEID_NAME = "beneId";
	public static final String FAID_NAME = "faId";
	public static final String PAYCHANNELID_NAME = "payChannelId";
	public static final String BANKCODE_NAME = "bankCode";
	public static final String DEPOSITNAME_NAME = "depositName";


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
	private double withdrawing;
	public double getWithdrawing() {
		return withdrawing;
	}
	public void setWithdrawing(double withdrawing) {
		this.withdrawing = withdrawing;
		entityMap.put(WITHDRAWING_NAME, withdrawing);
	}
	private String payMobile;
	public String getPayMobile() {
		return payMobile;
	}
	public void setPayMobile(String payMobile) {
		this.payMobile = payMobile;
		entityMap.put(PAYMOBILE_NAME, withdrawing);
	}
	private String email;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
		entityMap.put(EMAIL_NAME, withdrawing);
	}
	private String beneId;
	public String getBeneId() {
		return beneId;
	}
	public void setBeneId(String beneId) {
		this.beneId = beneId;
		entityMap.put(BENEID_NAME, beneId);
	}
	private String faId;
	public String getFaId() {
		return faId;
	}
	public void setFaId(String faId) {
		this.faId = faId;
		entityMap.put(FAID_NAME, faId);
	}
	private Integer payChannelId;
	public Integer getPayChannelId() {
		return payChannelId;
	}
	public void setPayChannelId(Integer payChannelId) {
		this.payChannelId = payChannelId;
		entityMap.put(PAYCHANNELID_NAME, payChannelId);
	}
	private String bankCode;
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
		entityMap.put(BANKCODE_NAME, bankCode);
	}
	private String depositName;
	public String getDepositName() {
		return depositName;
	}
	public void setDepositName(String depositName) {
		this.depositName = depositName;
		entityMap.put(DEPOSITNAME_NAME, depositName);
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
