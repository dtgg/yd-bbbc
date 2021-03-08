package com.ydqp.common.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author cfq
 */
public class SysCloseServer implements Serializable {
  
       public static final long serialVersionUID = 1593324586234l;
       private Map<String,Object> entityMap = new HashMap<String,Object>(16,1);
	   	public static final String ID_NAME = "id";
	public static final String CLOSESERVER_NAME = "closeServer";
	public static final String SERVERCODE_NAME = "serverCode";
	public static final String STATUS_NAME = "status";
	public static final String CREATETIME_NAME = "createTime";


    	private int id;
	public int getId() {
	return id;
	}
 	 public void setId(int id){
	 this.id = id;
	 entityMap.put(ID_NAME, id);
	}
	private int closeServer;
	public int getCloseServer() {
	return closeServer;
	}
 	 public void setCloseServer(int closeServer){
	 this.closeServer = closeServer;
	 entityMap.put(CLOSESERVER_NAME, closeServer);
	}
	private int serverCode;
	public int getServerCode() {
	return serverCode;
	}
 	 public void setServerCode(int serverCode){
	 this.serverCode = serverCode;
	 entityMap.put(SERVERCODE_NAME, serverCode);
	}
	private int status;
	public int getStatus() {
	return status;
	}
 	 public void setStatus(int status){
	 this.status = status;
	 entityMap.put(STATUS_NAME, status);
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
