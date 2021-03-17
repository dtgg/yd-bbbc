package com.ydqp.common.sendProtoMsg.mall;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.cfq.annotation.SendCommandAnnotation;
import com.cfq.message.AbstartCreateMessage;
import com.cfq.message.NetProtoMessage;
import com.ydqp.common.data.PlayerAccountData;
import lombok.Getter;
import lombok.Setter;

@SendCommandAnnotation(command = 1004107)
public class PlayerAccountUpdateSuccess extends AbstartCreateMessage {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.BOOL, order = 1)
    private boolean success;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 2)
    private String message;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.OBJECT, order = 3)
    private PlayerAccountData playerAccountData;

    @Override
    public NetProtoMessage encodeSendMessage() {
        NetProtoMessage netProtoMessage = new NetProtoMessage();
        netProtoMessage.getNetProtoMessageHead().setCmd(this.getCommand());
        try {
            Codec<PlayerAccountUpdateSuccess> beneInfoSuccessCodec = ProtobufProxy.create(PlayerAccountUpdateSuccess.class);
            byte[] body = beneInfoSuccessCodec.encode(this);
            netProtoMessage.getNetProtoMessageBody().setBody(body);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return netProtoMessage;
    }
}
