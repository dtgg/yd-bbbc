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
	public static final String REWARDSOURCE_NAME = "rewardSource";
	public static final String GRADE_NAME = "grade";
	public static final String EXPERIENCE_NAME = "experience";
	public static final String TASKID_NAME = "taskId";
	public static final String CREATETIME_NAME = "createTime";
	public static final String REWARDTYPE_NAME = "rewardType";


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
	private int rewardSource;
	public int getRewardSource() {
	return rewardSource;
	}
 	 public void setRewardSource(int rewardSource){
	 this.rewardSource = rewardSource;
	 entityMap.put(REWARDSOURCE_NAME, rewardSource);
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
	private int rewardType;
	public int getRewardType() {
		return rewardType;
	}
	public void setRewardType(int rewardType) {
		this.rewardType = rewardType;
		entityMap.put(REWARDTYPE_NAME, rewardType);
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
