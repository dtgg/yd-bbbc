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

@ReceiveCommandAnnotation(command = 5000002)
public class LotteryBuy extends AbstartParaseMessage {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT64, order = 1)
    private long playerId;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 2)
    private int lotteryId;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 3)
    private Integer select;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 4)
    private String number;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 5)
    private String pay;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 6)
    private int type;

    @Override
    public AbstartParaseMessage paraseMessage(NetProtoMessage netProtoMessage) throws Exception {
        byte[] body = netProtoMessage.getNetProtoMessageBody().getBody();

        Codec<LotteryBuy> lotteryBuyCodec = ProtobufProxy
                .create(LotteryBuy.class);

        LotteryBuy lotteryBuy = lotteryBuyCodec.decode(body);

//        setPlayerId(lotteryBuy.getPlayerId());
//        setLotteryId(lotteryBuy.getLotteryId());
//        setSelect(lotteryBuy.getSelect());
//        setNumber(lotteryBuy.getNumber());
//        setPay(lotteryBuy.getPay());
//        setType(lotteryBuy.getType());
        return lotteryBuy;
    }
}
