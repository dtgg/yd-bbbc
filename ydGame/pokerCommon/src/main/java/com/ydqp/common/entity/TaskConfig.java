package com.ydqp.common.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cfq
 */
public class TaskConfig implements Serializable {

    public static final long serialVersionUID = 1592971037733l;
    private Map<String, Object> entityMap = new HashMap<String, Object>(16, 1);
    public static final String ID_NAME = "id";
    public static final String NAME_NAME = "name";
    public static final String TYPE_NAME = "type";
    public static final String REWARD_NAME = "reward";
    public static final String NEXTTASKID_NAME = "nextTaskId";
    public static final String TARGET_NAME = "target";
    public static final String REWARDTYPE_NAME = "rewardType";


    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        entityMap.put(ID_NAME, id);
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        entityMap.put(NAME_NAME, name);
    }

    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
        entityMap.put(TYPE_NAME, type);
    }

    private int reward;

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
        entityMap.put(REWARD_NAME, reward);
    }

    private Integer nextTaskId;

    public Integer getNextTaskId() {
        return nextTaskId;
    }

    public void setNextTaskId(Integer nextTaskId) {
        this.nextTaskId = nextTaskId;
        entityMap.put(NEXTTASKID_NAME, nextTaskId);
    }

    private int target;

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
        entityMap.put(TARGET_NAME, target);
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
    public String toString() {
        return this.entityMap.toString();
    }
}
