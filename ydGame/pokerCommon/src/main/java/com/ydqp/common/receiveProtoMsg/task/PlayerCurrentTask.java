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

@ReceiveCommandAnnotation(command = 1002004)
@GenProto(modulePro = "lobby")
public class PlayerCurrentTask extends AbstartParaseMessage {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT64, order = 1)
    private long playerId;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 2)
    private int taskType;

    @Override
    public AbstartParaseMessage paraseMessage(NetProtoMessage netProtoMessage) throws Exception {
        byte[] body = netProtoMessage.getNetProtoMessageBody().getBody();

        Codec<PlayerCurrentTask> playerCurrentTaskCodec = ProtobufProxy.create(PlayerCurrentTask.class);

        PlayerCurrentTask playerCurrentTask = playerCurrentTaskCodec.decode(body);
//        setPlayerId(playerCurrentTask.getPlayerId());
//        setTaskType(playerCurrentTask.getTaskType());
        return playerCurrentTask;
    }
}
