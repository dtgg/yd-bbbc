package com.ydqp.common.receiveProtoMsg.mall;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.cfq.annotation.ReceiveCommandAnnotation;
import com.cfq.message.AbstartParaseMessage;
import com.cfq.message.NetProtoMessage;
import lombok.Getter;
import lombok.Setter;

@ReceiveCommandAnnotation(command = 1004005)
public class PlayerWithdrawal extends AbstartParaseMessage {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT64, order = 1)
    private long playerId;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.DOUBLE, order = 2)
    private double amount;

    @Override
    public AbstartParaseMessage paraseMessage(NetProtoMessage netProtoMessage) throws Exception {
        byte[] bytes = netProtoMessage.getNetProtoMessageBody().getBody();

        Codec<PlayerWithdrawal> withdrawalApplyCodec = ProtobufProxy.create(PlayerWithdrawal.class);

        PlayerWithdrawal withdrawalApply = withdrawalApplyCodec.decode(bytes);
//        setPlayerId(withdrawalApply.getPlayerId());
//        setAmount(withdrawalApply.getAmount());
        return withdrawalApply;
    }
}
