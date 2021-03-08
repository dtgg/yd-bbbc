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

@ReceiveCommandAnnotation(command = 1000023)
public class PlayerHeadUrl extends AbstartParaseMessage {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT64, order = 1)
    private long playerId;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 2)
    private String headUrl;

    @Override
    public AbstartParaseMessage paraseMessage(NetProtoMessage netProtoMessage) throws Exception {
        byte[] bytes = netProtoMessage.getNetProtoMessageBody().getBody();
        Codec<PlayerHeadUrl> codec = ProtobufProxy.create(PlayerHeadUrl.class);
        PlayerHeadUrl headUrl = codec.decode(bytes);
//        setPlayerId(headUrl.getPlayerId());
//        setHeadUrl(headUrl.getHeadUrl());
        return headUrl;
    }
}
