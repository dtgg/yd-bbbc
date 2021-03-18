package com.ydqp.common.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cfq
 */
public class PlayerBonusDraw implements Serializable {

    public static final long serialVersionUID = 1616038517985l;
    private Map<String, Object> entityMap = new HashMap<String, Object>(16, 1);
    public static final String ID_NAME = "id";
    public static final String PLAYERID_NAME = "playerId";
    public static final String ORDERID_NAME = "orderId";
    public static final String AMOUNT_NAME = "amount";
    public static final String STATUS_NAME = "status";
    public static final String CREATETIME_NAME = "createTime";
    public static final String APPID_NAME = "appId";
    public static final String KFID_NAME = "kfId";


    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        entityMap.put(ID_NAME, id);
    }

    private long playerId;

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
        entityMap.put(PLAYERID_NAME, playerId);
    }

    private String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
        entityMap.put(ORDERID_NAME, orderId);
    }

    private BigDecimal amount;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
        entityMap.put(AMOUNT_NAME, amount);
    }

    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
        entityMap.put(STATUS_NAME, status);
    }

    private int createTime;

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
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

    private long kfId;

    public long getKfId() {
        return kfId;
    }

    public void setKfId(long kfId) {
        this.kfId = kfId;
        entityMap.put(KFID_NAME, kfId);
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
