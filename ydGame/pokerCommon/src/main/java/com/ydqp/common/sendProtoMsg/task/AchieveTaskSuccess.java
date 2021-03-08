package com.ydqp.common.sendProtoMsg.task;

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

@SendCommandAnnotation(command = 1002103)
@GenProto(modulePro = "lobby")
public class AchieveTaskSuccess extends AbstartCreateMessage {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 1)
    private boolean isAchievement;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 2)
    private String message;

    @Override
    public NetProtoMessage encodeSendMessage() {
        NetProtoMessage netProtoMessage = new NetProtoMessage();
        netProtoMessage.getNetProtoMessageHead().setCmd(this.getCommand());
        try {
            Codec<AchieveTaskSuccess> achieveTaskSuccessCodec = ProtobufProxy.create(AchieveTaskSuccess.class);
            byte[] body = achieveTaskSuccessCodec.encode(this);
            netProtoMessage.getNetProtoMessageBody().setBody(body);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return netProtoMessage;
    }
}
