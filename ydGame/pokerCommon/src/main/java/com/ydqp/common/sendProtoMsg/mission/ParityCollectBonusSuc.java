package com.ydqp.common.sendProtoMsg.mission;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.cfq.annotation.SendCommandAnnotation;
import com.cfq.message.AbstartCreateMessage;
import com.cfq.message.NetProtoMessage;
import com.ydqp.common.data.PlayerBonusDrawData;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@SendCommandAnnotation(command = 1001076)
public class ParityCollectBonusSuc extends AbstartCreateMessage {
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.DOUBLE, order = 1)
    private double totalBonus;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.OBJECT, order = 2)
    private List<PlayerBonusDrawData> bonusDrawList;

    @Override
    public NetProtoMessage encodeSendMessage() {
        NetProtoMessage netProtoMessage = new NetProtoMessage();
        netProtoMessage.getNetProtoMessageHead().setCmd(this.getCommand());
        try {
            Codec<ParityCollectBonusSuc> parityCollectBonusSucCodec = ProtobufProxy.create(ParityCollectBonusSuc.class);
            byte[] bytes = parityCollectBonusSucCodec.encode(this);
            netProtoMessage.getNetProtoMessageBody().setBody(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return netProtoMessage;
    }
}
