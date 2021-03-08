package com.ydqp.common.receiveProtoMsg.player;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.cfq.annotation.ReceiveCommandAnnotation;
import com.cfq.message.AbstartParaseMessage;
import com.cfq.message.NetProtoMessage;
import lombok.Getter;
import lombok.Setter;

@ReceiveCommandAnnotation(command = 2020000)
public class PlayerDownLine extends AbstartParaseMessage {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT64, order = 1, description = "用户id")
    private long playerId;
    @Override
    public AbstartParaseMessage paraseMessage(NetProtoMessage netProtoMessage) throws Exception {
        byte[] body = netProtoMessage.getNetProtoMessageBody().getBody();
        // LobbyProto.PlayerLogout playerLogout = LobbyProto.PlayerLogout.parseFrom(body);

        Codec<PlayerDownLine> playerDownLineCodec = ProtobufProxy.create(PlayerDownLine.class);
        PlayerDownLine playerDownLine = playerDownLineCodec.decode(body);

        //setPlayerId(playerDownLine.getPlayerId());
        return playerDownLine;
    }
}
