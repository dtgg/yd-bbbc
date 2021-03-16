package com.ydqp.common.sendProtoMsg.mission;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.cfq.annotation.GenProto;
import com.cfq.annotation.SendCommandAnnotation;
import com.cfq.message.AbstartCreateMessage;
import com.cfq.message.NetProtoMessage;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@SendCommandAnnotation(command = 1001071)
@GenProto(modulePro = "lottery")
public class ParityOrderDetailSuc extends AbstartCreateMessage {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.OBJECT, order = 1)
    private List<ParityOrderDetailInfo> parityOrderDetailInfos;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT64, order = 2)
    private long playerId;

    @Override
    public NetProtoMessage encodeSendMessage() {
        NetProtoMessage netProtoMessage = new NetProtoMessage();
        netProtoMessage.getNetProtoMessageHead().setCmd(this.getCommand());
        try {
            Codec<ParityOrderDetailSuc> partyOrderDetailSucCodec = ProtobufProxy.create(ParityOrderDetailSuc.class);
            byte[] bytes = partyOrderDetailSucCodec.encode(this);
            netProtoMessage.getNetProtoMessageBody().setBody(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return netProtoMessage;
    }
}
