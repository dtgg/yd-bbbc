package com.ydqp.common.receiveProtoMsg.mission;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.cfq.annotation.ReceiveCommandAnnotation;
import com.cfq.message.AbstartParaseMessage;
import com.cfq.message.NetProtoMessage;
import lombok.Getter;
import lombok.Setter;

@ReceiveCommandAnnotation(command = 1000076)
public class ParityCollectBonus extends AbstartParaseMessage {
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT64, order = 1)
    private long playerId;

    @Override
    public AbstartParaseMessage paraseMessage(NetProtoMessage netProtoMessage) throws Exception {
        byte[] body = netProtoMessage.getNetProtoMessageBody().getBody();

        Codec<ParityCollectBonus> parityCollectBonusCodec = ProtobufProxy.create(ParityCollectBonus.class);

        return parityCollectBonusCodec.decode(body);
    }
}
