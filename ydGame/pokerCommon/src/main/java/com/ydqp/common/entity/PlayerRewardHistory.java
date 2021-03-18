package com.ydqp.common.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author cfq
 */
public class PlayerRewardHistory implements Serializable {
  
       public static final long serialVersionUID = 1595820917609l;
       private Map<String,Object> entityMap = new HashMap<String,Object>(16,1);
	   	public static final String ID_NAME = "id";
	public static final String PLAYERID_NAME = "playerId";
	public static final String REWARD_NAME = "reward";
	public static final String REWARDSOURCE_NAME = "source";
	public static final String TASKID_NAME = "taskId";
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
	private long playerId;
	public long getPlayerId() {
	return playerId;
	}
 	 public void setPlayerId(long playerId){
	 this.playerId = playerId;
	 entityMap.put(PLAYERID_NAME, playerId);
	}
	private double reward;
	public double getReward() {
	return reward;
	}
 	 public void setReward(double reward){
	 this.reward = reward;
	 entityMap.put(REWARD_NAME, reward);
	}
	private int source;
	public int getSource() {
	return source;
	}
 	 public void setSource(int source){
	 this.source = source;
	 entityMap.put(REWARDSOURCE_NAME, source);
	}
	private int taskId;
	public int getTaskId() {
	return taskId;
	}
 	 public void setTaskId(int taskId){
	 this.taskId = taskId;
	 entityMap.put(TASKID_NAME, taskId);
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
	public void setAppId(int appId) {
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
