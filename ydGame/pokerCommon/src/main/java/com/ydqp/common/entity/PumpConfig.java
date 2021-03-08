package com.ydqp.common.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author cfq
 */
public class PumpConfig implements Serializable {
  
       public static final long serialVersionUID = 1595489591216l;
       private Map<String,Object> entityMap = new HashMap<String,Object>(16,1);
	   	public static final String ID_NAME = "id";
	public static final String SERVERCODE_NAME = "serverCode";
	public static final String PUMP_NAME = "pump";
	public static final String CREATETIME_NAME = "createTime";


    	private int id;
	public int getId() {
	return id;
	}
 	 public void setId(int id){
	 this.id = id;
	 entityMap.put(ID_NAME, id);
	}
	private int serverCode;
	public int getServerCode() {
	return serverCode;
	}
 	 public void setServerCode(int serverCode){
	 this.serverCode = serverCode;
	 entityMap.put(SERVERCODE_NAME, serverCode);
	}
	private double pump;
	public double getPump() {
	return pump;
	}
 	 public void setPump(double pump){
	 this.pump = pump;
	 entityMap.put(PUMP_NAME, pump);
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
