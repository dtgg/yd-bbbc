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

@ReceiveCommandAnnotation(command = 1004007)
public class PlayerAccountSet extends AbstartParaseMessage {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 1)
    private String payMobile;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 2)
    private String email;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 3)
    private String depositName;

    @Override
    public AbstartParaseMessage paraseMessage(NetProtoMessage netProtoMessage) throws Exception {
        byte[] body = netProtoMessage.getNetProtoMessageBody().getBody();

        Codec<PlayerAccountSet> payInfoSetCodec = ProtobufProxy.create(PlayerAccountSet.class);

        return payInfoSetCodec.decode(body);
    }
}
