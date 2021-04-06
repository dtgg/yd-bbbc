package com.ydqp.common.sendProtoMsg.vspoker;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.cfq.annotation.GenProto;
import com.cfq.annotation.SendCommandAnnotation;
import com.cfq.message.AbstartCreateMessage;
import com.cfq.message.NetProtoMessage;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@SendCommandAnnotation(command = 7000010)
@GenProto(modulePro = "vsPoker")
public class SVsRaceEnd extends AbstartCreateMessage {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32 , order = 1, description = "roomId")
    private int roomId;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.MAP , order = 2, description = "4个下注区域 1 = A")
    private Map<Integer, SVsBonusRank> bonusRankMap = new HashMap<>(3);
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT64 , order = 3, description = "roomId")
    private long playerId;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32 , order = 4, description = "roomId")
    private int rank;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.DOUBLE , order = 5, description = "roomId")
    private double bonus;

    @Override
    public NetProtoMessage encodeSendMessage() {
        NetProtoMessage netProtoMessage = new NetProtoMessage();
        netProtoMessage.getNetProtoMessageHead().setCmd(this.getCommand());
        try {
            Codec<SVsRaceEnd> vsRaceEndCodec = ProtobufProxy.create(SVsRaceEnd.class);
            byte[] bytes = vsRaceEndCodec.encode(this);
            netProtoMessage.getNetProtoMessageBody().setBody(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return netProtoMessage;
    }
}
