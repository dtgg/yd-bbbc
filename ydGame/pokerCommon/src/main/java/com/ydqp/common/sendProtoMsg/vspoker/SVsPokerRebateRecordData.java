package com.ydqp.common.sendProtoMsg.vspoker;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.cfq.annotation.GenProto;
import lombok.Getter;
import lombok.Setter;

@GenProto(modulePro = "vsPoker")
public class SVsPokerRebateRecordData {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING , order = 1)
    private String playerName;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32 , order = 2)
    private int createTime;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING , order = 3)
    private String orderId;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.FLOAT , order = 4)
    private Float rebate;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32 , order = 5)
    private int raceId;
}
