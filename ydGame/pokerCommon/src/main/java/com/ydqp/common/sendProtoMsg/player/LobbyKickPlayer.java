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

@SendCommandAnnotation(command = 1000002)
@GenProto(modulePro = "lobby")
public class LobbyKickPlayer extends AbstartCreateMessage {

    @Setter
    @Getter
    @Protobuf(fieldType = FieldType.INT64, order = 1, description = "玩家Id")
    private long playerId;

    @Override
    public NetProtoMessage encodeSendMessage() {
        NetProtoMessage netProtoMessage = new NetProtoMessage();
        netProtoMessage.getNetProtoMessageHead().setCmd(this.getCommand());
        try {
            Codec<LobbyKickPlayer> lobbyKickPlayerCodec = ProtobufProxy.create(LobbyKickPlayer.class);
            byte[] bytes = lobbyKickPlayerCodec.encode(this);
            netProtoMessage.getNetProtoMessageBody().setBody(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return netProtoMessage;
    }
}
