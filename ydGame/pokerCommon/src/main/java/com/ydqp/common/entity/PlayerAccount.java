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
	public static final String ENABLED_NAME = "enabled";
	public static final String ACCOUNTID_NAME = "accountId";
	public static final String PASSWORD_NAME = "password";
	public static final String BANKNAME_NAME = "bankName";
	public static final String ISUPI_NAME = "isUpi";
	public static final String UPIID_NAME = "upiId";
	public static final String UPINAME_NAME = "upiName";


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
	private int enabled;
	public int getEnabled() {
		return enabled;
	}
	public void setEnabled(int enabled) {
		this.enabled = enabled;
		entityMap.put(ENABLED_NAME, enabled);
	}
	private String accountId;
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
		entityMap.put(ACCOUNTID_NAME, accountId);
	}
	private String password;
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
		entityMap.put(PASSWORD_NAME, password);
	}
	private String bankName;
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
		entityMap.put(BANKNAME_NAME, bankName);
	}
	private int isUpi;
	public int getIsUpi() {
		return isUpi;
	}
	public void setIsUpi(int isUpi) {
		this.isUpi = isUpi;
		entityMap.put(ISUPI_NAME, isUpi);
	}
	private String upiId;
	public String getUpiId() {
		return upiId;
	}
	public void setUpiId(String upiId) {
		this.upiId = upiId;
		entityMap.put(UPIID_NAME, upiId);
	}
	private String upiName;
	public String getUpiName() {
		return upiName;
	}
	public void setUpiName(String upiName) {
		this.upiName = upiName;
		entityMap.put(UPINAME_NAME, upiName);
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
