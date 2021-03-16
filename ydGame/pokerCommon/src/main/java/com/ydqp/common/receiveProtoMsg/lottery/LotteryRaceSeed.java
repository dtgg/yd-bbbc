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

@ReceiveCommandAnnotation(command = 5000017)
public class LotteryRaceSeed extends AbstartParaseMessage {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 1)
    private int type;

    @Override
    public AbstartParaseMessage paraseMessage(NetProtoMessage netProtoMessage) throws Exception {
        byte[] body = netProtoMessage.getNetProtoMessageBody().getBody();

        Codec<LotteryRaceSeed> lotteryQuitRoomCodec = ProtobufProxy
                .create(LotteryRaceSeed.class);

        return lotteryQuitRoomCodec.decode(body);
    }
}
