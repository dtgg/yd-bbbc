package com.ydqp.common.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author cfq
 */
public class Player implements Serializable {
  
       public static final long serialVersionUID = 1597363999949l;
       private Map<String,Object> entityMap = new HashMap<String,Object>(16,1);
	   	public static final String ID_NAME = "id";
	public static final String PLAYERNAME_NAME = "playerName";
	public static final String PASSWORD_NAME = "passWord";
	public static final String NICKNAME_NAME = "nickname";
	public static final String HEADURL_NAME = "headUrl";
	public static final String ROOMID_NAME = "roomId";
	public static final String ZJPOINT_NAME = "zjPoint";
	public static final String COINPOINT_NAME = "coinPoint";
	public static final String ONLINETIME_NAME = "onLineTime";
	public static final String CREATETIME_NAME = "createTime";
	public static final String GRADE_NAME = "grade";
	public static final String EXPERIENCE_NAME = "experience";
	public static final String FBUSERID_NAME = "fbUserId";
	public static final String FBBIND_NAME = "fbBind";
	public static final String APPID_NAME = "appId";
	public static final String BANLOGIN_NAME = "banLogin";
	public static final String REFERRALCODE_NAME = "referralCode";
	public static final String ORDERAMOUNT_NAME = "orderAmount";
	public static final String WITHDRAWAMOUNT_NAME = "withdrawAmount";
	public static final String KFID_NAME = "kfId";


    	private long id;
	public long getId() {
	return id;
	}
 	 public void setId(long id){
	 this.id = id;
	 entityMap.put(ID_NAME, id);
	}
	private String playerName;
	public String getPlayerName() {
	return playerName;
	}
 	 public void setPlayerName(String playerName){
	 this.playerName = playerName;
	 entityMap.put(PLAYERNAME_NAME, playerName);
	}
	private String passWord;
	public String getPassWord() {
	return passWord;
	}
 	 public void setPassWord(String passWord){
	 this.passWord = passWord;
	 entityMap.put(PASSWORD_NAME, passWord);
	}
	private String nickname;
	public String getNickname() {
	return nickname;
	}
 	 public void setNickname(String nickname){
	 this.nickname = nickname;
	 entityMap.put(NICKNAME_NAME, nickname);
	}
	private String headUrl;
	public String getHeadUrl() {
	return headUrl;
	}
 	 public void setHeadUrl(String headUrl){
	 this.headUrl = headUrl;
	 entityMap.put(HEADURL_NAME, headUrl);
	}
	private int roomId;
	public int getRoomId() {
	return roomId;
	}
 	 public void setRoomId(int roomId){
	 this.roomId = roomId;
	 entityMap.put(ROOMID_NAME, roomId);
	}
	private double zjPoint;
	public double getZjPoint() {
	return zjPoint;
	}
 	 public void setZjPoint(double zjPoint){
	 this.zjPoint = zjPoint;
	 entityMap.put(ZJPOINT_NAME, zjPoint);
	}
	private double coinPoint;
	public double getCoinPoint() {
	return coinPoint;
	}
 	 public void setCoinPoint(double coinPoint){
	 this.coinPoint = coinPoint;
	 entityMap.put(COINPOINT_NAME, coinPoint);
	}
	private int onLineTime;
	public int getOnLineTime() {
	return onLineTime;
	}
 	 public void setOnLineTime(int onLineTime){
	 this.onLineTime = onLineTime;
	 entityMap.put(ONLINETIME_NAME, onLineTime);
	}
	private int createTime;
	public int getCreateTime() {
	return createTime;
	}
 	 public void setCreateTime(int createTime){
	 this.createTime = createTime;
	 entityMap.put(CREATETIME_NAME, createTime);
	}
	private int grade;
	public int getGrade() {
	return grade;
	}
 	 public void setGrade(int grade){
	 this.grade = grade;
	 entityMap.put(GRADE_NAME, grade);
	}
	private double experience;
	public double getExperience() {
	return experience;
	}
 	 public void setExperience(double experience){
	 this.experience = experience;
	 entityMap.put(EXPERIENCE_NAME, experience);
	}
	private String fbUserId;
	public String getFbUserId() {
	return fbUserId;
	}
 	 public void setFbUserId(String fbUserId){
	 this.fbUserId = fbUserId;
	 entityMap.put(FBUSERID_NAME, fbUserId);
	}
	private byte fbBind;
	public byte getFbBind() {
	return fbBind;
	}
 	 public void setFbBind(byte fbBind){
	 this.fbBind = fbBind;
	 entityMap.put(FBBIND_NAME, fbBind);
	}
	private int appId;
	public int getAppId() {
	return appId;
	}
 	 public void setAppId(int appId){
	 this.appId = appId;
	 entityMap.put(APPID_NAME, appId);
	}
	private int banLogin;
	public int getBanLogin() {
	return banLogin;
	}
 	 public void setBanLogin(int banLogin){
	 this.banLogin = banLogin;
	 entityMap.put(BANLOGIN_NAME, banLogin);
	}
	private String referralCode;
	public String getReferralCode() {
		return referralCode;
	}
	public void setReferralCode(String referralCode) {
		this.referralCode = referralCode;
		entityMap.put(REFERRALCODE_NAME, referralCode);
	}
	private double orderAmount;
	public double getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(double orderAmount) {
		this.orderAmount = orderAmount;
		entityMap.put(ORDERAMOUNT_NAME, orderAmount);
	}
	private double withdrawAmount;
	public double getWithdrawAmount() {
		return withdrawAmount;
	}
	public void setWithdrawAmount(double withdrawAmount) {
		this.withdrawAmount = withdrawAmount;
		entityMap.put(WITHDRAWAMOUNT_NAME, withdrawAmount);
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
