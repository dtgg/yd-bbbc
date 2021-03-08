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

@ReceiveCommandAnnotation(command = 1004201)
public class PlayerOrders extends AbstartParaseMessage {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT64, order = 1)
    private long playerId;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 2)
    private int productId;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.DOUBLE, order = 3)
    private double amount;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 4)
    private String mobile;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 5)
    private String name;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 6)
    private String payerVA;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 7)
    private int payChannel;

    @Override
    public AbstartParaseMessage paraseMessage(NetProtoMessage netProtoMessage) throws Exception {
        byte[] bytes = netProtoMessage.getNetProtoMessageBody().getBody();

        Codec<PlayerOrders> playerShoppingCodec = ProtobufProxy.create(PlayerOrders.class);

        PlayerOrders playerShopping = playerShoppingCodec.decode(bytes);
//        setPlayerId(playerShopping.getPlayerId());
//        setProductId(playerShopping.getProductId());
//        setAmount(playerShopping.getAmount());
//        setMobile(playerShopping.getMobile());
//        setName(playerShopping.getName());
//        setPayerVA(playerShopping.getPayerVA());
//        setPayChannel(playerShopping.getPayChannel());
        return playerShopping;
    }
}
