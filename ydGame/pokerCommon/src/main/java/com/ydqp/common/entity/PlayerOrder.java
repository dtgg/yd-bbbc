package com.ydqp.common.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author cfq
 */
public class PlayerOrder implements Serializable {
  
       public static final long serialVersionUID = 1594870570991l;
       private Map<String,Object> entityMap = new HashMap<String,Object>(16,1);
	   	public static final String ID_NAME = "id";
	public static final String ORDERID_NAME = "orderId";
	public static final String PLAYERID_NAME = "playerId";
	public static final String NAME_NAME = "name";
	public static final String MOBILE_NAME = "mobile";
	public static final String PAYERVA_NAME = "payerVA";
	public static final String PRODUCTID_NAME = "productId";
	public static final String AMOUNT_NAME = "amount";
	public static final String ORDERTIME_NAME = "orderTime";
	public static final String PAYTIME_NAME = "payTime";
	public static final String PAYSTATUS_NAME = "payStatus";
	public static final String TXNORDERID_NAME = "txnOrderId";
	public static final String TXNSTATUS_NAME = "txnStatus";
	public static final String TXNCOMPLETIONDATE_NAME = "txnCompletionDate";
	public static final String PAYCHANNEL_NAME = "payChannel";
	public static final String ERRORMSG_NAME = "errorMsg";
	public static final String PLATFORMPAYCHANNEL_NAME = "platformPayChannel";
	public static final String CFTOKEN_NAME = "cftoken";
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
	private String orderId;
	public String getOrderId() {
	return orderId;
	}
 	 public void setOrderId(String orderId){
	 this.orderId = orderId;
	 entityMap.put(ORDERID_NAME, orderId);
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
	private String mobile;
	public String getMobile() {
	return mobile;
	}
 	 public void setMobile(String mobile){
	 this.mobile = mobile;
	 entityMap.put(MOBILE_NAME, mobile);
	}
	private String payerVA;
	public String getPayerVA() {
	return payerVA;
	}
 	 public void setPayerVA(String payerVA){
	 this.payerVA = payerVA;
	 entityMap.put(PAYERVA_NAME, payerVA);
	}
	private int productId;
	public int getProductId() {
	return productId;
	}
 	 public void setProductId(int productId){
	 this.productId = productId;
	 entityMap.put(PRODUCTID_NAME, productId);
	}
	private double amount;
	public double getAmount() {
	return amount;
	}
 	 public void setAmount(double amount){
	 this.amount = amount;
	 entityMap.put(AMOUNT_NAME, amount);
	}
	private int orderTime;
	public int getOrderTime() {
	return orderTime;
	}
 	 public void setOrderTime(int orderTime){
	 this.orderTime = orderTime;
	 entityMap.put(ORDERTIME_NAME, orderTime);
	}
	private int payTime;
	public int getPayTime() {
	return payTime;
	}
 	 public void setPayTime(int payTime){
	 this.payTime = payTime;
	 entityMap.put(PAYTIME_NAME, payTime);
	}
	private int payStatus;
	public int getPayStatus() {
	return payStatus;
	}
 	 public void setPayStatus(int payStatus){
	 this.payStatus = payStatus;
	 entityMap.put(PAYSTATUS_NAME, payStatus);
	}
	private String txnOrderId;
	public String getTxnOrderId() {
	return txnOrderId;
	}
 	 public void setTxnOrderId(String txnOrderId){
	 this.txnOrderId = txnOrderId;
	 entityMap.put(TXNORDERID_NAME, txnOrderId);
	}
	private String txnStatus;
	public String getTxnStatus() {
	return txnStatus;
	}
 	 public void setTxnStatus(String txnStatus){
	 this.txnStatus = txnStatus;
	 entityMap.put(TXNSTATUS_NAME, txnStatus);
	}
	private int txnCompletionDate;
	public int getTxnCompletionDate() {
	return txnCompletionDate;
	}
 	 public void setTxnCompletionDate(int txnCompletionDate){
	 this.txnCompletionDate = txnCompletionDate;
	 entityMap.put(TXNCOMPLETIONDATE_NAME, txnCompletionDate);
	}
	private String payChannel;
	public String getPayChannel() {
	return payChannel;
	}
 	 public void setPayChannel(String payChannel){
	 this.payChannel = payChannel;
	 entityMap.put(PAYCHANNEL_NAME, payChannel);
	}
	private String errorMsg;
	public String getErrorMsg() {
	return errorMsg;
	}
 	 public void setErrorMsg(String errorMsg){
	 this.errorMsg = errorMsg;
	 entityMap.put(ERRORMSG_NAME, errorMsg);
	}
	private int platformPayChannel;
	public int getPlatformPayChannel() {
	return platformPayChannel;
	}
 	 public void setPlatformPayChannel(int platformPayChannel){
	 this.platformPayChannel = platformPayChannel;
	 entityMap.put(PLATFORMPAYCHANNEL_NAME, platformPayChannel);
	}
	private String cftoken;
	public String getCftoken() {
	return cftoken;
	}
 	 public void setCftoken(String cftoken){
	 this.cftoken = cftoken;
	 entityMap.put(CFTOKEN_NAME, cftoken);
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
