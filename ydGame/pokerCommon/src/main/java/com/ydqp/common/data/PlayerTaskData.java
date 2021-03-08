package com.ydqp.common.data;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import lombok.Getter;
import lombok.Setter;

public class PlayerTaskData {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 1)
    private int id;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT64, order = 2)
    private long playerId;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 3)
    private int taskId;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 4)
    private int acceptTime;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 5)
    private int progress;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 6)
    private int isComplete;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 7)
    private Integer completeTime;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 8)
    private int isReceived;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 9)
    private Integer receiveTime;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 10)
    private int taskType;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.BOOL, order = 11)
    private boolean started;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 12)
    private int no;
}
