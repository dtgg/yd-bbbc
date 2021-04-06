package com.ydqp.common.sendProtoMsg.vspoker;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.cfq.annotation.GenProto;
import lombok.Getter;
import lombok.Setter;

@GenProto(modulePro = "vsPoker")
public class SVsPlayerRankData {
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT64 , order = 1, description = "赛事ID")
    private Long playerId;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING , order = 2, description = "赛事ID")
    private String playerName;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.DOUBLE , order = 3, description = "赛事ID")
    private Double bonus;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32 , order = 4, description = "赛事ID")
    private Integer point;
}
