package com.ydqp.lobby.task;

import com.ydqp.common.entity.TaskConfig;
import com.ydqp.common.task.Task;
import com.ydqp.lobby.constant.TaskType;
import com.ydqp.lobby.service.TaskService;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TaskManager {

    private TaskManager() {
        Map<Integer, TaskConfig> taskConfigMap = loadTaskConfig(TaskType.DAILY_TASK);
        taskMap = new HashMap<Integer, Task>() {{
            put(TaskType.DAILY_TASK, new DailyBonusTask(TaskType.DAILY_TASK, taskConfigMap));
        }};
    }

    public static TaskManager instance = new TaskManager();

    public static TaskManager getInstance() {
        if (instance == null) {
            instance = new TaskManager();
        }
        return instance;
    }

    private Map<Integer, Task> taskMap;

    public Task getTask(int taskType) {
        return taskMap.get(taskType);
    }

    private static Map<Integer, TaskConfig> loadTaskConfig(int taskType) {
        return TaskService.getInstance().getTasks(taskType).stream().collect(Collectors.toMap(TaskConfig::getId, Function.identity()));
    }
}
