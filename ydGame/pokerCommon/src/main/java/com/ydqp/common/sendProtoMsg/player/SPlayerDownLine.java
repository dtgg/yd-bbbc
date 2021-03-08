package com.ydqp.common.sendProtoMsg.player;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.cfq.annotation.ReceiveCommandAnnotation;
import com.cfq.annotation.SendCommandAnnotation;
import com.cfq.message.AbstartCreateMessage;
import com.cfq.message.AbstartParaseMessage;
import com.cfq.message.NetProtoMessage;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

@SendCommandAnnotation(command = 1000007)
public class SPlayerDownLine extends AbstartCreateMessage {

//    @Getter
//    @Setter
//    @Protobuf(fieldType = FieldType.INT32, order = 1, description = "房间id")
//    private int roomId;


    @Override
    public NetProtoMessage encodeSendMessage() {
        NetProtoMessage netProtoMessage = new NetProtoMessage();
        netProtoMessage.getNetProtoMessageHead().setCmd(this.getCommand());
//        LobbyProto.loginSuccess.Builder builder = LobbyProto.loginSuccess.newBuilder();
//        builder.setCode(this.getCode());
//        byte[] bytes = builder.build().toByteArray();
        try {
            Codec<SPlayerDownLine> sPlayerDownLineCodec = ProtobufProxy.create(SPlayerDownLine.class);
//            setCode(this.getCode());
            byte[] bytes = sPlayerDownLineCodec.encode(this);
            netProtoMessage.getNetProtoMessageBody().setBody(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return netProtoMessage;
    }

}
