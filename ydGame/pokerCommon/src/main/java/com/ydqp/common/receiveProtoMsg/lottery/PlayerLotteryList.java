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

@ReceiveCommandAnnotation(command = 5000006)
public class PlayerLotteryList extends AbstartParaseMessage {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT64, order = 1)
    private long playerId;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 2)
    private int type;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 3)
    private int page;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 4)
    private int size;

    @Override
    public AbstartParaseMessage paraseMessage(NetProtoMessage netProtoMessage) throws Exception {
        byte[] body = netProtoMessage.getNetProtoMessageBody().getBody();

        Codec<PlayerLotteryList> playerLotteryListCodec = ProtobufProxy
                .create(PlayerLotteryList.class);

        PlayerLotteryList lotteryList = playerLotteryListCodec.decode(body);

//        setPlayerId(lotteryList.getPlayerId());
//        setType(lotteryList.getType());
//        setPage(lotteryList.getPage());
//        setSize(lotteryList.getSize());
        return lotteryList;
    }
}
