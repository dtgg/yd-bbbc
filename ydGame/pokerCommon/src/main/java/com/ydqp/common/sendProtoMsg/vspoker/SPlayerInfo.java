package com.ydqp.common.sendProtoMsg.vspoker;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.cfq.annotation.GenProto;
import com.ydqp.common.poker.Poker;
import lombok.Getter;
import lombok.Setter;

@GenProto(modulePro = "vsPoker")
public class SPlayerInfo {
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.DOUBLE , order = 1, description = "下注池子")
    private double betPool;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32 , order = 2, description = "下注池子")
    private int win;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.OBJECT , order = 3, description = "下注池子")
    private Poker poker;

}
