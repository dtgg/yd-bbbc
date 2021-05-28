package com.ydqp.common.receiveProtoMsg.vspoker;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.cfq.annotation.GenProto;
import com.cfq.annotation.ReceiveCommandAnnotation;
import com.cfq.message.AbstartParaseMessage;
import com.cfq.message.NetProtoMessage;
import lombok.Getter;
import lombok.Setter;

@ReceiveCommandAnnotation(command = 7000028)
@GenProto(modulePro = "vsPoker")
public class VsPokerSignGameBonus extends AbstartParaseMessage {

    @Setter
    @Getter
    @Protobuf(fieldType = FieldType.INT32, order = 1)
    private int raceType;

    @Setter
    @Getter
    @Protobuf(fieldType = FieldType.INT32, order = 2)
    private int basePoint;

    @Override
    public AbstartParaseMessage paraseMessage(NetProtoMessage netProtoMessage) throws Exception {
        byte[] body = netProtoMessage.getNetProtoMessageBody().getBody();

        Codec<VsPokerSignGameBonus> vsPokerGameBonusCodec = ProtobufProxy
                .create(VsPokerSignGameBonus.class);

        return vsPokerGameBonusCodec.decode(body);
    }
}
