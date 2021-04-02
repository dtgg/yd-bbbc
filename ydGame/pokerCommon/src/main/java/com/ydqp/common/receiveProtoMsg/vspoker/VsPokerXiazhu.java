package com.ydqp.common.receiveProtoMsg.vspoker;

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

@ReceiveCommandAnnotation(command = 7000007)
@GenProto(modulePro = "vsPoker")
public class VsPokerXiazhu extends AbstartParaseMessage {

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
    private int playType;

    @Setter
    @Getter
    @Protobuf(fieldType = FieldType.INT32, order = 4)
    private double money;

    @Override
    public AbstartParaseMessage paraseMessage(NetProtoMessage netProtoMessage) throws Exception {
        byte[] body = netProtoMessage.getNetProtoMessageBody().getBody();

        Codec<VsPokerXiazhu> vsPokerXiazhuCodec = ProtobufProxy
                .create(VsPokerXiazhu.class);

        VsPokerXiazhu vsPokerXiazhu = vsPokerXiazhuCodec.decode(body);

//        setPlayerId(lotteryEnterRoom.getPlayerId());
//        setRoomId(lotteryEnterRoom.getRoomId());
        return vsPokerXiazhu;
    }
}
