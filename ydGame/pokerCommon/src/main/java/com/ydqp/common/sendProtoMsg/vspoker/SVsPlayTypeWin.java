package com.ydqp.common.sendProtoMsg.vspoker;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.cfq.annotation.GenProto;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@GenProto(modulePro = "vsPoker")
public class SVsPlayTypeWin {
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.MAP , order = 1, description = "下注区域,输赢")
    private Map<Integer, Boolean> playTypeWinMap = new HashMap<>();
}
