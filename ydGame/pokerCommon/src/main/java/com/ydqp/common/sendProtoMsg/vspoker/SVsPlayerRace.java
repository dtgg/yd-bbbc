package com.ydqp.common.sendProtoMsg.vspoker;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.cfq.annotation.GenProto;
import lombok.Getter;
import lombok.Setter;

@GenProto(modulePro = "vsPoker")
public class SVsPlayerRace {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT64 , order = 1, description = "用户ID")
    private long playerId;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32 , order = 2, description = "赛事ID")
    private int raceId;
}
