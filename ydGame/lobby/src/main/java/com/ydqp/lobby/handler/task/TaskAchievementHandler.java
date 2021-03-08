package com.ydqp.lobby.handler.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.entity.PlayerTask;
import com.ydqp.common.entity.TaskConfig;
import com.ydqp.common.receiveProtoMsg.task.AchieveTask;
import com.ydqp.common.sendProtoMsg.task.AchieveTaskSuccess;
import com.ydqp.common.sendProtoMsg.task.DailyTaskSuccess;
import com.ydqp.lobby.constant.TaskType;
import com.ydqp.lobby.service.TaskService;
import com.ydqp.lobby.task.AchievementTask;
import com.ydqp.lobby.task.TaskManager;

import java.util.HashMap;
import java.util.Map;

@ServerHandler(command = 1002003, module = "task")
public class TaskAchievementHandler implements IServerHandler {
    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        AchieveTask achieveTask = (AchieveTask) abstartParaseMessage;
        long playerId = achieveTask.getPlayerId();
        int taskId = achieveTask.getTaskId();

        AchievementTask achievementTask = (AchievementTask) TaskManager.getInstance().getTask(TaskType.ACHIEVEMENT_TASK);
        boolean acceptTask = achievementTask.acceptTask(taskId);
        if (!acceptTask) {
            DailyTaskSuccess dailyTaskSuccess = new DailyTaskSuccess();
            dailyTaskSuccess.setSignSuccess(false);
            dailyTaskSuccess.setMessage("The specified task does not exist");
            iSession.sendMessageByID(dailyTaskSuccess, achieveTask.getConnId());
            return;
        }

        Map<Integer, TaskConfig> taskConfigMap = achievementTask.getTaskConfigMap();
        int firstTaskId = achievementTask.firstTask();
        Map<String, Object> param = new HashMap<String, Object>() {{
            put("playerId", playerId);
            put("taskId", firstTaskId);
            put("acceptTime", new Long(System.currentTimeMillis() / 1000).intValue());
        }};

        PlayerTask playerTask = TaskService.getInstance().getCurrentTask(TaskType.ACHIEVEMENT_TASK, playerId);
        if (playerTask == null) {
            long playerTaskId = TaskService.getInstance().saveTask(param);
            playerTask = JSONObject.parseObject(JSON.toJSONString(param), PlayerTask.class);
            playerTask.setId(playerTaskId);
        }

        boolean doTask = achievementTask.doTask(playerTask.getTaskId());
        if (doTask) {
            playerTask.setProgress(playerTask.getProgress() + 1);
            //达成成就
            if (playerTask.getProgress() == taskConfigMap.get(playerTask.getTaskId()).getTarget()) {
                Object[] updateParam = new Object[]{1, new Long(System.currentTimeMillis() / 1000).intValue(), playerTask.getProgress(), playerTask.getId()};
                TaskService.getInstance().doneTask(updateParam);

                //接受下一个成就任务
                if (taskConfigMap.get(playerTask.getTaskId()).getNextTaskId() != null) {
                    param.put("taskId", taskConfigMap.get(playerTask.getTaskId()).getNextTaskId());
                    param.put("progress", playerTask.getProgress());
                    TaskService.getInstance().saveTask(param);
                }

                AchieveTaskSuccess achieveTaskSuccess = new AchieveTaskSuccess();
                achieveTaskSuccess.setAchievement(true);
                iSession.sendMessageByID(achieveTaskSuccess, achieveTask.getConnId());
            } else {
                Object[] updateParam = new Object[]{0, new Long(System.currentTimeMillis() / 1000).intValue(), playerTask.getProgress(), playerTask.getId()};
                TaskService.getInstance().doneTask(updateParam);
            }
        }
    }
}
