package com.ydqp.common.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author cfq
 */
public class ServiceConfig implements Serializable {
  
       public static final long serialVersionUID = 1616565230937l;
       private Map<String,Object> entityMap = new HashMap<String,Object>(16,1);
	   	public static final String ID_NAME = "id";
	public static final String APPID_NAME = "appId";
	public static final String TELEGRAM_NAME = "telegram";
	public static final String WHATSAPP_NAME = "whatsapp";


    	private int id;
	public int getId() {
	return id;
	}
 	 public void setId(int id){
	 this.id = id;
	 entityMap.put(ID_NAME, id);
	}
	private int appId;
	public int getAppId() {
	return appId;
	}
 	 public void setAppId(int appId){
	 this.appId = appId;
	 entityMap.put(APPID_NAME, appId);
	}
	private String telegram;
	public String getTelegram() {
	return telegram;
	}
 	 public void setTelegram(String telegram){
	 this.telegram = telegram;
	 entityMap.put(TELEGRAM_NAME, telegram);
	}
	private String whatsapp;
	public String getWhatsapp() {
	return whatsapp;
	}
 	 public void setWhatsapp(String whatsapp){
	 this.whatsapp = whatsapp;
	 entityMap.put(WHATSAPP_NAME, whatsapp);
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
