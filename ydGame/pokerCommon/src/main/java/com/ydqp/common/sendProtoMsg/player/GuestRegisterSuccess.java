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

import java.io.IOException;
import java.util.Map;

@SendCommandAnnotation(command = 1000015)
@GenProto(modulePro = "lobby")
public class GuestRegisterSuccess extends AbstartCreateMessage {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 1,description = "游客登陆账号")
    private String playerName;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 2,description = "游客登陆密码")
    private String passWord;

    @Override
    public NetProtoMessage encodeSendMessage() {
        NetProtoMessage netProtoMessage = new NetProtoMessage();
        netProtoMessage.getNetProtoMessageHead().setCmd(this.getCommand());
        try {
            Codec<GuestRegisterSuccess> guestRegisterSuccessCodec = ProtobufProxy.create(GuestRegisterSuccess.class);
            byte[] bytes = guestRegisterSuccessCodec.encode(this);
            netProtoMessage.getNetProtoMessageBody().setBody(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return netProtoMessage;
    }
}
