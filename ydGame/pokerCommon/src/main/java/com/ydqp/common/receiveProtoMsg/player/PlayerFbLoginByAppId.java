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
@ReceiveCommandAnnotation(command = 1000025)
@GenProto(modulePro = "lobby")
public class PlayerFbLoginByAppId extends AbstartParaseMessage {
    //用戶名
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 1)
    private String fbUserId;
    //登陸密碼
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 2)
    private String accessToken;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 3)
    private String fbNickName;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 4)
    private int appId;

    @Override
    public AbstartParaseMessage paraseMessage(NetProtoMessage netProtoMessage) throws Exception {
        byte[] body = netProtoMessage.getNetProtoMessageBody().getBody();

        Codec<PlayerFbLoginByAppId> simpleTypeCodec = ProtobufProxy
                .create(PlayerFbLoginByAppId.class);

        PlayerFbLoginByAppId playerFbLogin = simpleTypeCodec.decode(body);

        //byte[] body = netProtoMessage.getNetProtoMessageBody().getBody();
        //LobbyProto.PlayerLogin playerLogin = LobbyProto.PlayerLogin.parseFrom(body);

//        setFbUserId(playerFbLogin.getFbUserId());
//        setAccessToken(playerFbLogin.getAccessToken());
//        setFbNickName(playerFbLogin.getFbNickName());
//        setAppId(playerFbLogin.getAppId());
        return playerFbLogin;
    }
}
