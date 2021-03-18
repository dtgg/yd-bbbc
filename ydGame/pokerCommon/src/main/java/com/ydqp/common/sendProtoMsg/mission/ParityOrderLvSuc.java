package com.ydqp.common.sendProtoMsg.mission;

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

import java.util.List;

@SendCommandAnnotation(command = 1001072)
@GenProto(modulePro = "lottery")
public class ParityOrderLvSuc extends AbstartCreateMessage {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.OBJECT, order = 1)
    private List<ParityOrderDetailInfo> infos;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT64, order = 2)
    private long playerId;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 3)
    private int count;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.DOUBLE, order = 4)
    private double bonus;

    @Override
    public NetProtoMessage encodeSendMessage() {
        NetProtoMessage netProtoMessage = new NetProtoMessage();
        netProtoMessage.getNetProtoMessageHead().setCmd(this.getCommand());
        try {
            Codec<ParityOrderLvSuc> parityOrderLvSucCodec = ProtobufProxy.create(ParityOrderLvSuc.class);
            byte[] bytes = parityOrderLvSucCodec.encode(this);
            netProtoMessage.getNetProtoMessageBody().setBody(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return netProtoMessage;
    }
}
