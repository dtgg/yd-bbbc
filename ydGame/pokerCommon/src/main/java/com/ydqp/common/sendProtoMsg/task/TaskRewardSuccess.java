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

@SendCommandAnnotation(command = 1002105)
@GenProto(modulePro = "lobby")
public class TaskRewardSuccess extends AbstartCreateMessage {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 1)
    private boolean isReward;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 2)
    private String message;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 3)
    private int taskId;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 4)
    private int taskType;

    @Override
    public NetProtoMessage encodeSendMessage() {
        NetProtoMessage netProtoMessage = new NetProtoMessage();
        netProtoMessage.getNetProtoMessageHead().setCmd(this.getCommand());
        try {
            Codec<TaskRewardSuccess> taskRewardSuccessCodec = ProtobufProxy.create(TaskRewardSuccess.class);
            byte[] body = taskRewardSuccessCodec.encode(this);
            netProtoMessage.getNetProtoMessageBody().setBody(body);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return netProtoMessage;
    }
}
