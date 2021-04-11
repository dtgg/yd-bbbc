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

@ReceiveCommandAnnotation(command = 7000012)
@GenProto(modulePro = "vsPoker")
public class VsPokerRaceHistory extends AbstartParaseMessage {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT64, order = 1)
    private long playerId;

    @Override
    public AbstartParaseMessage paraseMessage(NetProtoMessage netProtoMessage) throws Exception {
        byte[] body = netProtoMessage.getNetProtoMessageBody().getBody();

        Codec<VsPokerRaceHistory> vsPokerRaceHistoryCodec = ProtobufProxy
                .create(VsPokerRaceHistory.class);

        return vsPokerRaceHistoryCodec.decode(body);
    }
}
