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

@SendCommandAnnotation(command = 1000022)
@GenProto(modulePro = "lobby")
public class PlayerFbBindSuccess extends AbstartCreateMessage {
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT64, order = 1, description = "用户id")
    private long playerId;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 2, description = "返回状态 0 成功 1 token失败 2 已经绑定过  其他错误")
    private int status;

    @Override
    public NetProtoMessage encodeSendMessage() {
        NetProtoMessage netProtoMessage = new NetProtoMessage();
        netProtoMessage.getNetProtoMessageHead().setCmd(this.getCommand());
//        LobbyProto.loginSuccess.Builder builder = LobbyProto.loginSuccess.newBuilder();
//        builder.setCode(this.getCode());
//        byte[] bytes = builder.build().toByteArray();
        try {
            Codec<PlayerFbBindSuccess> playerFbBindSuccessCodec = ProtobufProxy.create(PlayerFbBindSuccess.class);
//            setCode(this.getCode());
            byte[] bytes = playerFbBindSuccessCodec.encode(this);
            netProtoMessage.getNetProtoMessageBody().setBody(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return netProtoMessage;
    }
}
