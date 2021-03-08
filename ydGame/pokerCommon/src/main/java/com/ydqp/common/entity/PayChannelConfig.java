package com.ydqp.common.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author cfq
 */
public class PayChannelConfig implements Serializable {
  
       public static final long serialVersionUID = 1594964840520l;
       private Map<String,Object> entityMap = new HashMap<String,Object>(16,1);
	   	public static final String ID_NAME = "id";
	public static final String CHANNEL_NAME = "channel";
	public static final String NAME_NAME = "name";
	public static final String APPID_NAME = "appId";
	public static final String SECRETKEY_NAME = "secretKey";
	public static final String ENABLED_NAME = "enabled";
	public static final String WITHDRAWALAUDIT_NAME = "withdrawalAudit";
	public static final String PAYMENTNOTIFYURL_NAME = "paymentNotifyUrl";
	public static final String CLIENTID_NAME = "clientId";
	public static final String CLIENTSECRET_NAME = "clientSecret";
	public static final String PAYOUTNOTIFYURL_NAME = "payoutNotifyUrl";
	public static final String BUSINESSACCOUNT_NAME = "businessAccount";
	public static final String WITHDRAWFEE_NAME = "withdrawFee";
	public static final String PAYMENTURL_NAME = "paymentUrl";
	public static final String MCHID_NAME = "mchId";


    	private int id;
	public int getId() {
	return id;
	}
 	 public void setId(int id){
	 this.id = id;
	 entityMap.put(ID_NAME, id);
	}
	private int channel;
	public int getChannel() {
	return channel;
	}
 	 public void setChannel(int channel){
	 this.channel = channel;
	 entityMap.put(CHANNEL_NAME, channel);
	}
	private String name;
	public String getName() {
	return name;
	}
 	 public void setName(String name){
	 this.name = name;
	 entityMap.put(NAME_NAME, name);
	}
	private String appId;
	public String getAppId() {
	return appId;
	}
 	 public void setAppId(String appId){
	 this.appId = appId;
	 entityMap.put(APPID_NAME, appId);
	}
	private String secretKey;
	public String getSecretKey() {
	return secretKey;
	}
 	 public void setSecretKey(String secretKey){
	 this.secretKey = secretKey;
	 entityMap.put(SECRETKEY_NAME, secretKey);
	}
	private int enabled;
	public int getEnabled() {
	return enabled;
	}
 	 public void setEnabled(int enabled){
	 this.enabled = enabled;
	 entityMap.put(ENABLED_NAME, enabled);
	}
	private int withdrawalAudit;
	public int getWithdrawalAudit() {
	return withdrawalAudit;
	}
 	 public void setWithdrawalAudit(int withdrawalAudit){
	 this.withdrawalAudit = withdrawalAudit;
	 entityMap.put(WITHDRAWALAUDIT_NAME, withdrawalAudit);
	}
	private String paymentNotifyUrl;
	public String getPaymentNotifyUrl() {
	return paymentNotifyUrl;
	}
 	 public void setPaymentNotifyUrl(String paymentNotifyUrl){
	 this.paymentNotifyUrl = paymentNotifyUrl;
	 entityMap.put(PAYMENTNOTIFYURL_NAME, paymentNotifyUrl);
	}
	private String clientId;
	public String getClientId() {
	return clientId;
	}
 	 public void setClientId(String clientId){
	 this.clientId = clientId;
	 entityMap.put(CLIENTID_NAME, clientId);
	}
	private String clientSecret;
	public String getClientSecret() {
	return clientSecret;
	}
 	 public void setClientSecret(String clientSecret){
	 this.clientSecret = clientSecret;
	 entityMap.put(CLIENTSECRET_NAME, clientSecret);
	}
	private String payoutNotifyUrl;
	public String getPayoutNotifyUrl() {
	return payoutNotifyUrl;
	}
 	 public void setPayoutNotifyUrl(String payoutNotifyUrl){
	 this.payoutNotifyUrl = payoutNotifyUrl;
	 entityMap.put(PAYOUTNOTIFYURL_NAME, payoutNotifyUrl);
	}
	private String businessAccount;
	public String getBusinessAccount() {
		return businessAccount;
	}
	public void setBusinessAccount(String businessAccount) {
		this.businessAccount = businessAccount;
		entityMap.put(BUSINESSACCOUNT_NAME, businessAccount);
	}
	private BigDecimal withdrawFee;
	public BigDecimal getWithdrawFee() {
		return withdrawFee;
	}
	public void setWithdrawFee(BigDecimal withdrawFee) {
		this.withdrawFee = withdrawFee;
		entityMap.put(WITHDRAWFEE_NAME, withdrawFee);
	}
	private String paymentUrl;
	public String getPaymentUrl() {
		return paymentUrl;
	}
	public void setPaymentUrl(String paymentUrl) {
		this.paymentUrl = paymentUrl;
		entityMap.put(PAYMENTURL_NAME, paymentUrl);
	}
	private String mchId;
	public String getMchId() {
		return mchId;
	}
	public void setMchId(String mchId) {
		this.mchId = mchId;
		entityMap.put(MCHID_NAME, mchId);
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
