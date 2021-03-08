package com.ydqp.common.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author cfq
 */
public class GameVersion implements Serializable {
  
       public static final long serialVersionUID = 1597194979160l;
       private Map<String,Object> entityMap = new HashMap<String,Object>(16,1);
	   	public static final String ID_NAME = "id";
	public static final String APKVERSION_NAME = "apkVersion";
	public static final String APKMANDATORY_NAME = "apkMandatory";
	public static final String JSVERSION_NAME = "jsVersion";
	public static final String APKURL_NAME = "apkUrl";
	public static final String CREATETIME_NAME = "createTime";
	public static final String APPID_NAME = "appId";


    	private int id;
	public int getId() {
	return id;
	}
 	 public void setId(int id){
	 this.id = id;
	 entityMap.put(ID_NAME, id);
	}
	private String apkVersion;
	public String getApkVersion() {
	return apkVersion;
	}
 	 public void setApkVersion(String apkVersion){
	 this.apkVersion = apkVersion;
	 entityMap.put(APKVERSION_NAME, apkVersion);
	}
	private byte apkMandatory;
	public byte getApkMandatory() {
	return apkMandatory;
	}
 	 public void setApkMandatory(byte apkMandatory){
	 this.apkMandatory = apkMandatory;
	 entityMap.put(APKMANDATORY_NAME, apkMandatory);
	}
	private String jsVersion;
	public String getJsVersion() {
	return jsVersion;
	}
 	 public void setJsVersion(String jsVersion){
	 this.jsVersion = jsVersion;
	 entityMap.put(JSVERSION_NAME, jsVersion);
	}
	private String apkUrl;
	public String getApkUrl() {
	return apkUrl;
	}
 	 public void setApkUrl(String apkUrl){
	 this.apkUrl = apkUrl;
	 entityMap.put(APKURL_NAME, apkUrl);
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
 	 public void setAppId(int appId){
	 this.appId = appId;
	 entityMap.put(APPID_NAME, appId);
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
