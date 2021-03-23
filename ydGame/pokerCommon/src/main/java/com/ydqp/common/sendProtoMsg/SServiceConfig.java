package com.ydqp.common.sendProtoMsg;

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

@SendCommandAnnotation(command = 1000083)
@GenProto(modulePro = "lobby")
public class SServiceConfig extends AbstartCreateMessage {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 1)
    private String telegram;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 2)
    private String whatsapp;

    @Override
    public NetProtoMessage encodeSendMessage() {
        NetProtoMessage netProtoMessage = new NetProtoMessage();
        netProtoMessage.getNetProtoMessageHead().setCmd(this.getCommand());
        try {
            Codec<SServiceConfig> sServiceConfigCodec = ProtobufProxy.create(SServiceConfig.class);
            byte[] bytes = sServiceConfigCodec.encode(this);
            netProtoMessage.getNetProtoMessageBody().setBody(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return netProtoMessage;
    }
}
