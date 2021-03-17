package com.ydqp.common.receiveProtoMsg.mall;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.cfq.annotation.ReceiveCommandAnnotation;
import com.cfq.message.AbstartParaseMessage;
import com.cfq.message.NetProtoMessage;
import lombok.Getter;
import lombok.Setter;

@ReceiveCommandAnnotation(command = 1004008)
public class PlayerAccountResetPassword extends AbstartParaseMessage {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT64, order = 1)
    private long playerId;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 2)
    private String verificationCode;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 3)
    private String password;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 4)
    private String repeatedPassword;

    @Override
    public AbstartParaseMessage paraseMessage(NetProtoMessage netProtoMessage) throws Exception {
        byte[] body = netProtoMessage.getNetProtoMessageBody().getBody();

        Codec<PlayerAccountResetPassword> codec = ProtobufProxy.create(PlayerAccountResetPassword.class);

        return codec.decode(body);
    }
}
