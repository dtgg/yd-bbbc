package com.ydqp.common.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author cfq
 */
public class PlayerTask implements Serializable {
  
       public static final long serialVersionUID = 1594836343995l;
       private Map<String,Object> entityMap = new HashMap<String,Object>(16,1);
	   	public static final String ID_NAME = "id";
	public static final String PLAYERID_NAME = "playerId";
	public static final String TASKTYPE_NAME = "taskType";
	public static final String TASKID_NAME = "taskId";
	public static final String ACCEPTTIME_NAME = "acceptTime";
	public static final String PROGRESS_NAME = "progress";
	public static final String ISCOMPLETE_NAME = "isComplete";
	public static final String COMPLETETIME_NAME = "completeTime";
	public static final String ISRECEIVED_NAME = "isReceived";
	public static final String RECEIVETIME_NAME = "receiveTime";


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
	private int taskType;
	public int getTaskType() {
	return taskType;
	}
 	 public void setTaskType(int taskType){
	 this.taskType = taskType;
	 entityMap.put(TASKTYPE_NAME, taskType);
	}
	private int taskId;
	public int getTaskId() {
	return taskId;
	}
 	 public void setTaskId(int taskId){
	 this.taskId = taskId;
	 entityMap.put(TASKID_NAME, taskId);
	}
	private int acceptTime;
	public int getAcceptTime() {
	return acceptTime;
	}
 	 public void setAcceptTime(int acceptTime){
	 this.acceptTime = acceptTime;
	 entityMap.put(ACCEPTTIME_NAME, acceptTime);
	}
	private int progress;
	public int getProgress() {
	return progress;
	}
 	 public void setProgress(int progress){
	 this.progress = progress;
	 entityMap.put(PROGRESS_NAME, progress);
	}
	private int isComplete;
	public int getIsComplete() {
	return isComplete;
	}
 	 public void setIsComplete(int isComplete){
	 this.isComplete = isComplete;
	 entityMap.put(ISCOMPLETE_NAME, isComplete);
	}
	private int completeTime;
	public int getCompleteTime() {
	return completeTime;
	}
 	 public void setCompleteTime(int completeTime){
	 this.completeTime = completeTime;
	 entityMap.put(COMPLETETIME_NAME, completeTime);
	}
	private int isReceived;
	public int getIsReceived() {
	return isReceived;
	}
 	 public void setIsReceived(int isReceived){
	 this.isReceived = isReceived;
	 entityMap.put(ISRECEIVED_NAME, isReceived);
	}
	private int receiveTime;
	public int getReceiveTime() {
	return receiveTime;
	}
 	 public void setReceiveTime(int receiveTime){
	 this.receiveTime = receiveTime;
	 entityMap.put(RECEIVETIME_NAME, receiveTime);
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
