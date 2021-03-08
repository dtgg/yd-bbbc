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

@SendCommandAnnotation(command = 1002102)
@GenProto(modulePro = "lobby")
public class DailyTaskSuccess extends AbstartCreateMessage {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.BOOL, order = 1)
    private boolean isSignSuccess;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 2)
    private String message;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 3)
    private int taskId;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 4)
    private int taskType;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 5)
    private int no;

    @Override
    public NetProtoMessage encodeSendMessage() {
        NetProtoMessage netProtoMessage = new NetProtoMessage();
        netProtoMessage.getNetProtoMessageHead().setCmd(this.getCommand());
        try {
            Codec<DailyTaskSuccess> dailyBonusSuccessCodec = ProtobufProxy.create(DailyTaskSuccess.class);
            byte[] body = dailyBonusSuccessCodec.encode(this);
            netProtoMessage.getNetProtoMessageBody().setBody(body);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return netProtoMessage;
    }
}
