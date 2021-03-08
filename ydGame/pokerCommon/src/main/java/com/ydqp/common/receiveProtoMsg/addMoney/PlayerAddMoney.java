package com.ydqp.common.receiveProtoMsg.addMoney;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.cfq.annotation.ReceiveCommandAnnotation;
import com.cfq.message.AbstartParaseMessage;
import com.cfq.message.NetProtoMessage;
import lombok.Getter;
import lombok.Setter;

@ReceiveCommandAnnotation(command = 1000050)
public class PlayerAddMoney extends AbstartParaseMessage {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT64, order = 1)
    private long playerId;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 2)
    private int roomId;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 3)
    private int type;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.DOUBLE, order = 4)
    private double money;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT64, order = 5)
    private long tableId;

    @Override
    public AbstartParaseMessage paraseMessage(NetProtoMessage netProtoMessage) throws Exception {
        byte[] body = netProtoMessage.getNetProtoMessageBody().getBody();

        Codec<PlayerAddMoney> playerAddMoneyCodec = ProtobufProxy.create(PlayerAddMoney.class);
        PlayerAddMoney playerAddMoney = playerAddMoneyCodec.decode(body);
//        setPlayerId(playerAddMoney.getPlayerId());
//        setRoomId(playerAddMoney.getRoomId());
//        setType(playerAddMoney.getType());
//        setMoney(playerAddMoney.getMoney());
//        setTableId(playerAddMoney.getTableId());
        return playerAddMoney;
    }
}
