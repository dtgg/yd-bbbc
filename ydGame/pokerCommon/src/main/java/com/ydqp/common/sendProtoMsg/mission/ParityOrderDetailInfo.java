package com.ydqp.common.sendProtoMsg.mission;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.cfq.annotation.GenProto;
import lombok.Getter;
import lombok.Setter;

@GenProto(modulePro = "lottery")
public class ParityOrderDetailInfo {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT64, order = 1)
    private long playerId;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 2)
    private String nickname;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.DOUBLE, order = 3)
    private double contribute;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 4)
    private int date;
}
