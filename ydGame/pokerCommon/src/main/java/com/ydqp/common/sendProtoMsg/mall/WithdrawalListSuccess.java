package com.ydqp.common.sendProtoMsg.mall;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.cfq.annotation.SendCommandAnnotation;
import com.cfq.message.AbstartCreateMessage;
import com.cfq.message.NetProtoMessage;
import com.ydqp.common.data.WithdrawalData;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@SendCommandAnnotation(command = 1004108)
public class WithdrawalListSuccess extends AbstartCreateMessage {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.OBJECT, order = 1)
    private List<WithdrawalData> withdrawalData;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.BOOL, order = 2)
    private boolean success;

    @Override
    public NetProtoMessage encodeSendMessage() {
        NetProtoMessage netProtoMessage = new NetProtoMessage();
        netProtoMessage.getNetProtoMessageHead().setCmd(this.getCommand());
        try {
            Codec<WithdrawalListSuccess> withdrawalListSuccessCodec = ProtobufProxy.create(WithdrawalListSuccess.class);
            byte[] body = withdrawalListSuccessCodec.encode(this);
            netProtoMessage.getNetProtoMessageBody().setBody(body);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return netProtoMessage;
    }
}
