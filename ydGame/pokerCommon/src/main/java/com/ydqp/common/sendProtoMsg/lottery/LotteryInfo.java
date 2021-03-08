package com.ydqp.common.sendProtoMsg.lottery;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.ydqp.common.entity.Lottery;
import lombok.Getter;
import lombok.Setter;

public class LotteryInfo {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 1)
    private int id;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 2)
    private int type;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 3)
    private String period;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 4)
    private String price;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 5)
    private String number;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 6)
    private int status;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 7)
    private int createTime;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 8)
    private int openTime;

    public LotteryInfo() {
    }

    public LotteryInfo(Lottery lottery, String period) {
        this.id = lottery.getId();
        this.type = lottery.getType();
        this.period = period;
        this.price = lottery.getPrice();
        this.number = lottery.getNumber();
        this.status = lottery.getStatus();
        this.createTime = lottery.getCreateTime();
        this.openTime = lottery.getOpenTime();
    }
}
