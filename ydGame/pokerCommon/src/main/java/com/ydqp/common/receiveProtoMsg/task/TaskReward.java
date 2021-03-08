package com.ydqp.common.receiveProtoMsg.task;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.cfq.annotation.GenProto;
import com.cfq.annotation.ReceiveCommandAnnotation;
import com.cfq.message.AbstartParaseMessage;
import com.cfq.message.NetProtoMessage;
import lombok.Getter;
import lombok.Setter;

@ReceiveCommandAnnotation(command = 1002005)
@GenProto(modulePro = "lobby")
public class TaskReward extends AbstartParaseMessage {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT64, order = 1)
    private long playerId;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 2)
    private int taskId;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 3)
    private int taskType;

    @Override
    public AbstartParaseMessage paraseMessage(NetProtoMessage netProtoMessage) throws Exception {
        byte[] body = netProtoMessage.getNetProtoMessageBody().getBody();

        Codec<TaskReward> taskRewardCodec = ProtobufProxy.create(TaskReward.class);

        TaskReward taskReward = taskRewardCodec.decode(body);
//        setPlayerId(taskReward.getPlayerId());
//        setTaskId(taskReward.getTaskId());
//        setTaskType(taskReward.getTaskType());
        return taskReward;
    }
}
