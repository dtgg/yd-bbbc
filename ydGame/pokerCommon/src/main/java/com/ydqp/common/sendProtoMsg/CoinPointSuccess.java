package com.ydqp.common.sendProtoMsg;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.cfq.annotation.SendCommandAnnotation;
import com.cfq.message.AbstartCreateMessage;
import com.cfq.message.NetProtoMessage;
import lombok.Getter;
import lombok.Setter;

@SendCommandAnnotation(command = 9010000)
public class CoinPointSuccess extends AbstartCreateMessage {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT64, order = 1, description = "用户ID")
    private long playerId;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.DOUBLE, order = 2, description = "coin")
    private double coinPoint;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 3, description = "类型(1:chip:2:gold)")
    private int coinType;

    @Override
    public NetProtoMessage encodeSendMessage() {
        NetProtoMessage netProtoMessage = new NetProtoMessage();
        netProtoMessage.getNetProtoMessageHead().setCmd(this.getCommand());
        try {
            Codec<CoinPointSuccess> coinPointSuccessCodec = ProtobufProxy.create(CoinPointSuccess.class);
            byte[] bytes = coinPointSuccessCodec.encode(this);
            netProtoMessage.getNetProtoMessageBody().setBody(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return netProtoMessage;
    }
}
