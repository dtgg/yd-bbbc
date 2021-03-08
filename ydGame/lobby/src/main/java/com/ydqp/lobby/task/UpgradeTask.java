package com.ydqp.lobby.task;

import com.ydqp.common.entity.TaskConfig;
import com.ydqp.common.task.Task;

import java.util.Map;

public class UpgradeTask extends Task {
    public UpgradeTask(int taskType, Map<Integer, TaskConfig> taskConfigMap) {
        super(taskType, taskConfigMap);
    }

    @Override
    public boolean doTask(int taskId) {
        return true;
    }
}
