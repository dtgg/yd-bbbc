package com.ydqp.common.sendProtoMsg.vspoker;

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

@SendCommandAnnotation(command = 7000009)
@GenProto(modulePro = "vsPoker")
public class SVsTaoTai extends AbstartCreateMessage {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32 , order = 1, description = "roomId")
    private int roomId;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT64 , order = 2, description = "用户id")
    private long playerId;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32 , order = 3, description = "roomId")
    private int rank;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.DOUBLE , order = 4, description = "roomId")
    private double bonus;

    @Override
    public NetProtoMessage encodeSendMessage() {
        NetProtoMessage netProtoMessage = new NetProtoMessage();
        netProtoMessage.getNetProtoMessageHead().setCmd(this.getCommand());
        try {
            Codec<SVsTaoTai> sVsTaoTaiCodec = ProtobufProxy.create(SVsTaoTai.class);
            byte[] bytes = sVsTaoTaiCodec.encode(this);
            netProtoMessage.getNetProtoMessageBody().setBody(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return netProtoMessage;
    }
}
