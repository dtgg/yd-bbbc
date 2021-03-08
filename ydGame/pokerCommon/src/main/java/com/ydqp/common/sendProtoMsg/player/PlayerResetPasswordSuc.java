package com.ydqp.common.sendProtoMsg.player;

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

@SendCommandAnnotation(command = 1001033)
@GenProto(modulePro = "lobby")
public class PlayerResetPasswordSuc extends AbstartCreateMessage {
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
    @Protobuf(fieldType = FieldType.STRING, order = 3)
    private String playerName;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 4)
    private String password;

    @Override
    public NetProtoMessage encodeSendMessage() {
        NetProtoMessage netProtoMessage = new NetProtoMessage();
        netProtoMessage.getNetProtoMessageHead().setCmd(this.getCommand());
        try {
            Codec<PlayerResetPasswordSuc> playerResetPasswordSucCodec = ProtobufProxy.create(PlayerResetPasswordSuc.class);
            byte[] bytes = playerResetPasswordSucCodec.encode(this);
            netProtoMessage.getNetProtoMessageBody().setBody(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return netProtoMessage;
    }
}
