package com.ydqp.common.data;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.ydqp.common.entity.PlayerOrder;
import com.ydqp.common.entity.PlayerWithdrawal;
import lombok.Getter;
import lombok.Setter;

public class RechargeData {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 1)
    private String orderNumber;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.DOUBLE, order = 2)
    private double amount;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 3)
    private String orderTime;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 4)
    private int status;

    public RechargeData() {

    }

    public RechargeData(PlayerOrder order, String orderTime) {
        this.orderNumber = order.getOrderId();
        this.amount = order.getAmount();
        this.orderTime = orderTime;
        this.status = order.getStatus();
    }
}
