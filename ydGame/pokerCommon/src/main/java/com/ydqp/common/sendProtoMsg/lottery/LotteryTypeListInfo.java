package com.ydqp.common.sendProtoMsg.lottery;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class LotteryTypeListInfo {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 1)
    private int type;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.OBJECT, order = 2)
    private List<PlayerLotteryInfo> playerLotteryInfos;
}
