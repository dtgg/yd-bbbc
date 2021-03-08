package com.ydqp.lobby.dao;

import com.cfq.jdbc.JdbcOrm;
import com.ydqp.common.entity.PlayerTask;
import com.ydqp.common.entity.TaskConfig;

import java.util.List;
import java.util.Map;

public class TaskDao {

    private TaskDao() {}

    public static TaskDao instance;

    public static TaskDao getInstance() {
        if (instance == null) {
            instance = new TaskDao();
        }
        return instance;
    }

    private static final String SELECT_PLAYER_TASK = "select * from player_task where isComplete = 0 and taskType=#{taskType} and playerId=#{playerId} order by id desc limit 1";
    public PlayerTask getCurrentTask(int taskType, long playerId) {
        String sql = SELECT_PLAYER_TASK.replace("#{taskType}", String.valueOf(taskType))
                .replace("#{playerId}", String.valueOf(playerId)) + ";";
        Object bean = JdbcOrm.getInstance().getBean(sql, PlayerTask.class);
        return (PlayerTask) bean;
    }

    public long saveTask(Map<String, Object> task) {
        Object primkey = JdbcOrm.getInstance().insertReturnPrimkey("player_task", task);
        return Long.parseLong(String.valueOf(primkey));
    }

    private static final String DONE_TASK = "update player_task set isComplete=?, completeTime=?, progress =? where id=?;";
    public void doneTask(Object[] params) {
        JdbcOrm.getInstance().updateByArray(DONE_TASK, params);
    }

    private static final String COMPLETE_TASK = "update player_task set isReceived=?, receiveTime=? where id=?;";
    public void completeTask(Object[] params) {
        JdbcOrm.getInstance().updateByArray(COMPLETE_TASK, params);
    }

    private static final String DO_DAILY_TASK = "update player_task set isComplete=?, completeTime=?, progress=?, isReceived=?, receiveTime=? where id=?;";
    public void doDailyTask(Object[] params) {
        JdbcOrm.getInstance().updateByArray(DO_DAILY_TASK, params);
    }

    private static final String TASKS = "select * from task_config where enabled = 1 and type=";
    public List<TaskConfig> findAllByType(int taskType) {
        String sql = TASKS + taskType + ";";
        return JdbcOrm.getInstance().getListBean(sql, TaskConfig.class);
    }

    private static final String REWARD_TASK = "select * from player_task where isComplete = 1 and taskId=#{taskId} and playerId=#{playerId}";
    public PlayerTask QueryByTaskIdAndPlayerId(int taskId, long playerId) {
        String sql = SELECT_PLAYER_TASK.replace("#{taskId}", String.valueOf(taskId))
                .replace("#{playerId}", String.valueOf(playerId)) + ";";
        Object bean = JdbcOrm.getInstance().getBean(sql, PlayerTask.class);
        return (PlayerTask) bean;
    }
}
