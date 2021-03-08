package com.ydqp.common.sendProtoMsg.mall;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.cfq.annotation.SendCommandAnnotation;
import com.cfq.message.AbstartCreateMessage;
import com.cfq.message.NetProtoMessage;
import com.ydqp.common.data.PlayerAccountData;
import lombok.Getter;
import lombok.Setter;

@SendCommandAnnotation(command = 1004106)
public class PlayerAccountInfoSuccess extends AbstartCreateMessage {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.OBJECT, order = 1)
    private PlayerAccountData playerAccountData;

    @Override
    public NetProtoMessage encodeSendMessage() {
        NetProtoMessage netProtoMessage = new NetProtoMessage();
        netProtoMessage.getNetProtoMessageHead().setCmd(this.getCommand());
        try {
            Codec<PlayerAccountInfoSuccess> accountInfoSuccessCodec = ProtobufProxy.create(PlayerAccountInfoSuccess.class);
            byte[] body = accountInfoSuccessCodec.encode(this);
            netProtoMessage.getNetProtoMessageBody().setBody(body);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return netProtoMessage;
    }
}
