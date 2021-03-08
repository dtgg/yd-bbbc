package com.ydqp.common.sendProtoMsg.mall;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.cfq.annotation.SendCommandAnnotation;
import com.cfq.message.AbstartCreateMessage;
import com.cfq.message.NetProtoMessage;
import lombok.Getter;
import lombok.Setter;

@SendCommandAnnotation(command = 1004301)
public class PlayerOrderSuccess extends AbstartCreateMessage {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.BOOL, order = 1)
    private boolean success;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 2)
    private String message;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 3)
    private String payChannel;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 4)
    private String data;

//    @Getter
//    @Setter
//    @Protobuf(fieldType = FieldType.STRING, order = 3)
//    private String token;
//
//    @Getter
//    @Setter
//    @Protobuf(fieldType = FieldType.STRING, order = 4)
//    private String stage;
//
//    @Getter
//    @Setter
//    @Protobuf(fieldType = FieldType.STRING, order = 5)
//    private String appId;
//
//    @Getter
//    @Setter
//    @Protobuf(fieldType = FieldType.STRING, order = 6)
//    private String orderId;
//
//    @Getter
//    @Setter
//    @Protobuf(fieldType = FieldType.STRING, order = 7)
//    private String orderCurrency;
//
//    @Getter
//    @Setter
//    @Protobuf(fieldType = FieldType.DOUBLE, order = 8)
//    private double orderAmount;
//
//    @Getter
//    @Setter
//    @Protobuf(fieldType = FieldType.STRING, order = 9)
//    private String notifyUrl;

//    @Getter
//    @Setter
//    @Protobuf(fieldType = FieldType.STRING, order = 10)
//    private String txnOrderId;
//
//    @Getter
//    @Setter
//    @Protobuf(fieldType = FieldType.STRING, order = 11)
//    private String createTime;

    @Override
    public NetProtoMessage encodeSendMessage() {
        NetProtoMessage netProtoMessage = new NetProtoMessage();
        netProtoMessage.getNetProtoMessageHead().setCmd(this.getCommand());
        try {
            Codec<PlayerOrderSuccess> playerShoppingSuccessCodec = ProtobufProxy.create(PlayerOrderSuccess.class);
            byte[] bytes = playerShoppingSuccessCodec.encode(this);
            netProtoMessage.getNetProtoMessageBody().setBody(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return netProtoMessage;
    }
}
