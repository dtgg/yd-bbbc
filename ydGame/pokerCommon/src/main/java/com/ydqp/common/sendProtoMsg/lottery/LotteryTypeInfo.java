package com.ydqp.common.sendProtoMsg.lottery;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import lombok.Getter;
import lombok.Setter;

public class LotteryTypeInfo {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 1)
    private int type;
    @Setter
    @Getter
    @Protobuf(fieldType = FieldType.STRING, order = 2,description = "期数")
    private String period;
    @Setter
    @Getter
    @Protobuf(fieldType = FieldType.INT32, order = 3,description = "开始时间")
    private int createTime;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.OBJECT, order = 4)
    private LotteryInfo lotteryInfo;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.OBJECT, order = 5)
    private PlayerLotteryInfo playerLotteryInfo;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 6)
    private int lotteryId;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 7)
    private String drawNum;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.BOOL, order = 8)
    private boolean draw;
}
