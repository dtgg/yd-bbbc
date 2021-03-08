package com.ydqp.common.receiveProtoMsg.task;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.cfq.annotation.ReceiveCommandAnnotation;
import com.cfq.message.AbstartParaseMessage;
import com.cfq.message.NetProtoMessage;
import lombok.Getter;
import lombok.Setter;

@ReceiveCommandAnnotation(command = 1002006)
public class TaskUpgrade extends AbstartParaseMessage {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT64, order = 1)
    private long playerId;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 2)
    private int originGrade;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 3)
    private int grade;

    @Override
    public AbstartParaseMessage paraseMessage(NetProtoMessage netProtoMessage) throws Exception {
        byte[] body = netProtoMessage.getNetProtoMessageBody().getBody();

        Codec<TaskUpgrade> taskUpgradeCodec = ProtobufProxy.create(TaskUpgrade.class);

        TaskUpgrade taskUpgrade = taskUpgradeCodec.decode(body);
//        setPlayerId(taskUpgrade.getPlayerId());
//        setOriginGrade(taskUpgrade.getOriginGrade());
//        setGrade(taskUpgrade.getGrade());
        return taskUpgrade;
    }
}
