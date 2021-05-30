package com.ydqp.common.sendProtoMsg.vspoker;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.cfq.annotation.GenProto;
import lombok.Getter;
import lombok.Setter;

@GenProto(modulePro = "vsPoker")
public class SVsPokerFastRaceInfo {
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32 , order = 1)
    private int createTime;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32 , order = 2)
    private int basePoint;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32 , order = 3)
    private int ranking;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.DOUBLE , order = 4)
    private double Bonus;
}
