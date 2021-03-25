package com.ydqp.common.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cfq
 */
public class DrawMethod implements Serializable {

    public static final long serialVersionUID = 1616661867772l;
    private Map<String, Object> entityMap = new HashMap<String, Object>(16, 1);
    public static final String ID_NAME = "id";
    public static final String LOTTERYID_NAME = "lotteryId";
    public static final String METHOD_NAME = "method";
    public static final String CREATETIME_NAME = "createTime";


    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        entityMap.put(ID_NAME, id);
    }

    private int lotteryId;

    public int getLotteryId() {
        return lotteryId;
    }

    public void setLotteryId(int lotteryId) {
        this.lotteryId = lotteryId;
        entityMap.put(LOTTERYID_NAME, lotteryId);
    }

    private int method;

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
        entityMap.put(METHOD_NAME, method);
    }

    private int createTime;

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
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
    public String toString() {
        return this.entityMap.toString();
    }
}
