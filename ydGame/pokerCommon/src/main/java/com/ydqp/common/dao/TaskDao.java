package com.ydqp.common.dao;

import com.cfq.jdbc.JdbcOrm;
import com.ydqp.common.entity.TaskConfig;

import java.util.List;

public class TaskDao {

    private TaskDao() {}

    public static TaskDao instance;

    public static TaskDao getInstance() {
        if (instance == null) {
            instance = new TaskDao();
        }
        return instance;
    }

    private static final String TASKS = "select * from task_config where enabled = 1 and type=";
    public List<TaskConfig> findAllByType(int taskType) {
        String sql = TASKS + taskType + ";";
        return JdbcOrm.getInstance().getListBean(sql, TaskConfig.class);
    }
}
