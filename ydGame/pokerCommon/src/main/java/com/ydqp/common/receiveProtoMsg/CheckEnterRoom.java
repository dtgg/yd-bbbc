package com.ydqp.common.receiveProtoMsg;

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

@ReceiveCommandAnnotation(command = 1000800)
@GenProto(modulePro = "lobby")
public class CheckEnterRoom extends AbstartParaseMessage {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 1)
    private int roomId;

    @Override
    public AbstartParaseMessage paraseMessage(NetProtoMessage netProtoMessage) throws Exception {
        byte[] body = netProtoMessage.getNetProtoMessageBody().getBody();

        Codec<CheckEnterRoom> checkEnterRoomCodec = ProtobufProxy.create(CheckEnterRoom.class);
        CheckEnterRoom checkEnterRoom = checkEnterRoomCodec.decode(body);

        //setPlayerId(getGameVersion.getPlayerId());
        return checkEnterRoom;
    }
}
