package com.ydqp.common.sendProtoMsg.mission;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.cfq.annotation.SendCommandAnnotation;
import com.cfq.message.AbstartCreateMessage;
import com.cfq.message.NetProtoMessage;
import lombok.Getter;
import lombok.Setter;

@SendCommandAnnotation(command = 1001075)
public class ParityBonusAmountDrawSuc extends AbstartCreateMessage {
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.BOOL, order = 1)
    private boolean success;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 2)
    private String message;

    @Override
    public NetProtoMessage encodeSendMessage() {
        NetProtoMessage netProtoMessage = new NetProtoMessage();
        netProtoMessage.getNetProtoMessageHead().setCmd(this.getCommand());
        try {
            Codec<ParityBonusAmountDrawSuc> sucCodec = ProtobufProxy.create(ParityBonusAmountDrawSuc.class);
            byte[] bytes = sucCodec.encode(this);
            netProtoMessage.getNetProtoMessageBody().setBody(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return netProtoMessage;
    }
}
