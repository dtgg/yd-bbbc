package com.ydqp.common.data;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.ydqp.common.entity.PlayerBonusDraw;
import lombok.Getter;
import lombok.Setter;

public class PlayerBonusDrawData {
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 1)
    private int id;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT64, order = 2)
    private long playerId;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 3)
    private String orderId;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.DOUBLE, order = 4)
    private double amount;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 5)
    private int status;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 6)
    private int createTime;

    public PlayerBonusDrawData(PlayerBonusDraw playerBonusDraw) {
        this.id = playerBonusDraw.getId();
        this.playerId = playerBonusDraw.getPlayerId();
        this.orderId = playerBonusDraw.getOrderId();
        this.amount = playerBonusDraw.getAmount().doubleValue();
        this.status = playerBonusDraw.getStatus();
        this.createTime = playerBonusDraw.getCreateTime();
    }
}
