package com.ydqp.common.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author cfq
 */
public class GameAnnouncement implements Serializable {
  
       public static final long serialVersionUID = 1616134675567l;
       private Map<String,Object> entityMap = new HashMap<String,Object>(16,1);
	   	public static final String ID_NAME = "id";
	public static final String APPID_NAME = "appId";
	public static final String ANNOUNCEMENT_NAME = "announcement";
	public static final String CREATETIME_NAME = "createTime";


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
	private String announcement;
	public String getAnnouncement() {
	return announcement;
	}
 	 public void setAnnouncement(String announcement){
	 this.announcement = announcement;
	 entityMap.put(ANNOUNCEMENT_NAME, announcement);
	}
	private String createTime;
	public String getCreateTime() {
	return createTime;
	}
 	 public void setCreateTime(String createTime){
	 this.createTime = createTime;
	 entityMap.put(CREATETIME_NAME, createTime);
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
