package com.ydqp.common.receiveProtoMsg.player;

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

@ReceiveCommandAnnotation(command = 1000033)
@GenProto(modulePro = "lobby")
public class PlayerResetPassword extends AbstartParaseMessage {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 1)
    private String mobile;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 2)
    private String password;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 3)
    private String repeatPassword;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 4)
    private String verificationCode;

    @Override
    public AbstartParaseMessage paraseMessage(NetProtoMessage netProtoMessage) throws Exception {
        byte[] body = netProtoMessage.getNetProtoMessageBody().getBody();

        Codec<PlayerResetPassword> playerResetPasswordCodec = ProtobufProxy.create(PlayerResetPassword.class);
        PlayerResetPassword playerResetPassword = playerResetPasswordCodec.decode(body);
        return playerResetPassword;
    }
}
