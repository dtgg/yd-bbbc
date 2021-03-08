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

@ReceiveCommandAnnotation(command = 1004009)
public class OrderCallback extends AbstartParaseMessage {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 1)
    private String orderId;

    @Override
    public AbstartParaseMessage paraseMessage(NetProtoMessage netProtoMessage) throws Exception {
        byte[] body = netProtoMessage.getNetProtoMessageBody().getBody();

        Codec<OrderCallback> callbackCodec = ProtobufProxy.create(OrderCallback.class);

        OrderCallback callback = callbackCodec.decode(body);
        //setOrderId(callback.getOrderId());
        return callback;
    }
}
