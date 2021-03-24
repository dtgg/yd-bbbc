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
import java.util.ArrayList;
import java.util.List;

@SendCommandAnnotation(command = 1000005)
@GenProto(modulePro = "lobby")
public class LoginSuccess extends AbstartCreateMessage {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 1,description = "房间id")
    private int roomId;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.DOUBLE, order = 2, description = "用户的zj")
    private Double playerZJPoint;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 3,description = "用户的手机号")
    private String playerName;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 4, description = "用户的昵称")
    private String playerNickName;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 5, description = "用户的头像地址")
    private String playerUrl;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT64, order = 6, description = "用户id")
    private long playerId;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 7, description = "开启的游戏")
    private String openGames;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 8, description = "是否模拟号")
    private int isVir;

    @Override
    public NetProtoMessage encodeSendMessage() {
        NetProtoMessage netProtoMessage = new NetProtoMessage();
        netProtoMessage.getNetProtoMessageHead().setCmd(this.getCommand());
//        LobbyProto.loginSuccess.Builder builder = LobbyProto.loginSuccess.newBuilder();
//        builder.setCode(this.getCode());
//        byte[] bytes = builder.build().toByteArray();
        try {
            Codec<LoginSuccess> loginSuccessCodec = ProtobufProxy.create(LoginSuccess.class);
//            setCode(this.getCode());
            byte[] bytes = loginSuccessCodec.encode(this);
            netProtoMessage.getNetProtoMessageBody().setBody(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return netProtoMessage;
    }
}
