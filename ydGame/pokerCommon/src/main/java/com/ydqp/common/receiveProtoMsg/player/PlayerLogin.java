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

@ReceiveCommandAnnotation(command = 1000001)
@GenProto(modulePro = "lobby")
public class PlayerLogin extends AbstartParaseMessage {
    //用戶名
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 1)
    private String playerName;
    //登陸密碼
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 2)
    private String passWord;

    @Override
    public AbstartParaseMessage paraseMessage(NetProtoMessage netProtoMessage) throws Exception {
        byte[] body = netProtoMessage.getNetProtoMessageBody().getBody();

        Codec<PlayerLogin> simpleTypeCodec = ProtobufProxy
                .create(PlayerLogin.class);

        PlayerLogin playerLogin = simpleTypeCodec.decode(body);

        //byte[] body = netProtoMessage.getNetProtoMessageBody().getBody();
        //LobbyProto.PlayerLogin playerLogin = LobbyProto.PlayerLogin.parseFrom(body);

//        setPlayerName(playerLogin.getPlayerName());
//        setPassWord(playerLogin.getPassWord());
        return playerLogin;
    }


}
