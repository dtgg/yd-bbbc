package com.ydqp.common.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author cfq
 */
public class VsRace implements Serializable {
  
       public static final long serialVersionUID = 1617409332785l;
       private Map<String,Object> entityMap = new HashMap<String,Object>(16,1);
	   	public static final String ID_NAME = "id";
	public static final String RACETYPE_NAME = "raceType";
	public static final String BASEPOINT_NAME = "basePoint";
	public static final String MAXPLAYERNUM_NAME = "maxPlayerNum";
	public static final String CURPLAYERNUM_NAME = "curPlayerNum";
	public static final String STATUS_NAME = "status";
	public static final String BEGINTIME_NAME = "beginTime";
	public static final String CREATETIME_NAME = "createTime";


    	private int id;
	public int getId() {
	return id;
	}
 	 public void setId(int id){
	 this.id = id;
	 entityMap.put(ID_NAME, id);
	}
	private int raceType;
	public int getRaceType() {
	return raceType;
	}
 	 public void setRaceType(int raceType){
	 this.raceType = raceType;
	 entityMap.put(RACETYPE_NAME, raceType);
	}
	private int basePoint;
	public int getBasePoint() {
	return basePoint;
	}
 	 public void setBasePoint(int basePoint){
	 this.basePoint = basePoint;
	 entityMap.put(BASEPOINT_NAME, basePoint);
	}
	private int maxPlayerNum;
	public int getMaxPlayerNum() {
	return maxPlayerNum;
	}
 	 public void setMaxPlayerNum(int maxPlayerNum){
	 this.maxPlayerNum = maxPlayerNum;
	 entityMap.put(MAXPLAYERNUM_NAME, maxPlayerNum);
	}
	private int curPlayerNum;
	public int getCurPlayerNum() {
	return curPlayerNum;
	}
 	 public void setCurPlayerNum(int curPlayerNum){
	 this.curPlayerNum = curPlayerNum;
	 entityMap.put(CURPLAYERNUM_NAME, curPlayerNum);
	}
	private int status;
	public int getStatus() {
	return status;
	}
 	 public void setStatus(int status){
	 this.status = status;
	 entityMap.put(STATUS_NAME, status);
	}
	private int beginTime;
	public int getBeginTime() {
	return beginTime;
	}
 	 public void setBeginTime(int beginTime){
	 this.beginTime = beginTime;
	 entityMap.put(BEGINTIME_NAME, beginTime);
	}
	private int createTime;
	public int getCreateTime() {
	return createTime;
	}
 	 public void setCreateTime(int createTime){
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
