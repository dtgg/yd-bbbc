package com.ydqp.common.sendProtoMsg.vspoker;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.cfq.annotation.GenProto;
import lombok.Getter;
import lombok.Setter;

@GenProto(modulePro = "vsPoker")
public class SVsBonusRank {
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING , order = 1, description = "roomId")
    private String playerName;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.DOUBLE , order = 2, description = "roomId")
    private double bonus;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.DOUBLE , order = 3, description = "roomId")
    private double points;
}
