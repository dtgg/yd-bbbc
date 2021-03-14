package com.ydqp.common.receiveProtoMsg.lottery;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.cfq.annotation.ReceiveCommandAnnotation;
import com.cfq.message.AbstartParaseMessage;
import com.cfq.message.NetProtoMessage;
import lombok.Getter;
import lombok.Setter;

@ReceiveCommandAnnotation(command = 5000001)
public class LotteryEnterRoom extends AbstartParaseMessage {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT64, order = 1)
    private long playerId;

    @Setter
    @Getter
    @Protobuf(fieldType = FieldType.INT32, order = 2)
    private int roomId;

    @Setter
    @Getter
    @Protobuf(fieldType = FieldType.INT32, order = 3)
    private int type;

    @Override
    public AbstartParaseMessage paraseMessage(NetProtoMessage netProtoMessage) throws Exception {
        byte[] body = netProtoMessage.getNetProtoMessageBody().getBody();

        Codec<LotteryEnterRoom> lotteryEnterRoomCodec = ProtobufProxy
                .create(LotteryEnterRoom.class);

        LotteryEnterRoom lotteryEnterRoom = lotteryEnterRoomCodec.decode(body);

//        setPlayerId(lotteryEnterRoom.getPlayerId());
//        setRoomId(lotteryEnterRoom.getRoomId());
        return lotteryEnterRoom;
    }
}
