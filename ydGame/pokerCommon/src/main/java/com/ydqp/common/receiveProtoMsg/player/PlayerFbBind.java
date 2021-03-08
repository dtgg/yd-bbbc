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
@ReceiveCommandAnnotation(command = 1000021)
@GenProto(modulePro = "lobby")
public class PlayerFbBind extends AbstartParaseMessage {
    //用戶名
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 1)
    private String fbUserId;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 2)
    private String accessToken;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 3)
    private String fbNickName;

    @Override
    public AbstartParaseMessage paraseMessage(NetProtoMessage netProtoMessage) throws Exception {
        byte[] body = netProtoMessage.getNetProtoMessageBody().getBody();

        Codec<PlayerFbLogin> simpleTypeCodec = ProtobufProxy
                .create(PlayerFbLogin.class);

        PlayerFbLogin playerFbLogin = simpleTypeCodec.decode(body);

        //byte[] body = netProtoMessage.getNetProtoMessageBody().getBody();
        //LobbyProto.PlayerLogin playerLogin = LobbyProto.PlayerLogin.parseFrom(body);

//        setFbUserId(playerFbLogin.getFbUserId());
//        setAccessToken(playerFbLogin.getAccessToken());
//        setFbNickName(playerFbLogin.getFbNickName());
        return playerFbLogin;
    }
}
