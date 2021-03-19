package com.ydqp.common.sendProtoMsg;

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

@SendCommandAnnotation(command = 1000801)
@GenProto(modulePro = "lobby")
public class CheckEnterRoomSuc extends AbstartCreateMessage {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32 , order = 1, description = "200 可以进入，400不行")
    private int code;

    @Override
    public NetProtoMessage encodeSendMessage() {
        NetProtoMessage netProtoMessage = new NetProtoMessage();
        netProtoMessage.getNetProtoMessageHead().setCmd(this.getCommand());
        try {
            Codec<CheckEnterRoomSuc> checkEnterRoomSucCodec = ProtobufProxy.create(CheckEnterRoomSuc.class);
            byte[] bytes = checkEnterRoomSucCodec.encode(this);
            netProtoMessage.getNetProtoMessageBody().setBody(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return netProtoMessage;
    }
}
