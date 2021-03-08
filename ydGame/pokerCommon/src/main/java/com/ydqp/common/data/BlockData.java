package com.ydqp.common.data;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import lombok.Getter;
import lombok.Setter;

public class BlockData {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 1)
    private int id;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 2)
    private long playerId;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 3)
    private int blockPlayerId;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 4)
    private int createTime;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 5)
    private String playerName;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 6)
    private String headUrl;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.BOOL, order = 7)
    private boolean isOnline;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 8)
    private int grade;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.DOUBLE, order = 9)
    private double experience;
}
