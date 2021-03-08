package com.ydqp.common.task;

import com.ydqp.common.entity.TaskConfig;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Task implements ITask {

    @Getter
    @Setter
    private int taskType;

    // taskId/taskConfig
    @Getter
    @Setter
    private Map<Integer, TaskConfig> taskConfigMap;

    public Task(int taskType, Map<Integer, TaskConfig> taskConfigMap) {
        this.taskType = taskType;
        this.taskConfigMap = taskConfigMap;
    }

    @Override
    public boolean acceptTask(int taskId) {
        AtomicBoolean exist = new AtomicBoolean(false);
        taskConfigMap.forEach((id, taskConfig) -> {
            if (id == taskId) exist.set(true);
        });
        return exist.get();
    }

    @Override
    public int getReward(int taskId) {
        return taskConfigMap.get(taskId).getReward();
    }

    public Integer firstTask() {
        Integer firstTaskId = null;

        Set<Integer> nextTaskIds = new HashSet<>();
        taskConfigMap.forEach((taskId, taskConfig) -> nextTaskIds.add(taskConfig.getNextTaskId()));

        for (Map.Entry<Integer, TaskConfig> entry : taskConfigMap.entrySet()) {
            if (!nextTaskIds.contains(entry.getKey())) {
                firstTaskId = entry.getKey();
                break;
            }
        }
        return firstTaskId;
    }
}
