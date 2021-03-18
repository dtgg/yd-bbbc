package com.ydqp.common.data;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.ydqp.common.entity.TaskConfig;
import lombok.Getter;
import lombok.Setter;

public class TaskConfigData {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 1)
    private int id;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 2)
    private String name;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 3)
    private int type;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 4)
    private int reward;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 5)
    private Integer target;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 6)
    private Integer enabled;

    public TaskConfigData() {
    }

    public TaskConfigData(TaskConfig taskConfig) {
        this.id = taskConfig.getId();
        this.name = taskConfig.getName();
        this.type = taskConfig.getType();
        this.reward = taskConfig.getReward();
        this.target = taskConfig.getTarget();
        this.enabled = taskConfig.getEnabled();
    }
}
