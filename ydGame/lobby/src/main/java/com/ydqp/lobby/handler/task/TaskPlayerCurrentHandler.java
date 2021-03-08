package com.ydqp.lobby.handler.task;

import com.alibaba.fastjson.JSONObject;
import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.data.PlayerTaskData;
import com.ydqp.common.entity.PlayerTask;
import com.ydqp.common.entity.TaskConfig;
import com.ydqp.common.receiveProtoMsg.task.PlayerCurrentTask;
import com.ydqp.common.sendProtoMsg.task.PlayerCurrentTaskSuccess;
import com.ydqp.lobby.service.TaskService;
import com.ydqp.lobby.task.DailyBonusTask;
import com.ydqp.lobby.task.TaskManager;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

@ServerHandler(command = 1002004, module = "lobby")
public class TaskPlayerCurrentHandler implements IServerHandler {
    private static Logger logger = LoggerFactory.getLogger(TaskPlayerCurrentHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        logger.info("Get player current task request: {}", JSONObject.toJSONString(abstartParaseMessage));
        PlayerCurrentTask currentTask = (PlayerCurrentTask) abstartParaseMessage;

        DailyBonusTask dailyBonusTask = (DailyBonusTask) TaskManager.getInstance().getTask(currentTask.getTaskType());
        int firstTaskId = dailyBonusTask.firstTask();

        Map<Integer, TaskConfig> taskConfigMap = dailyBonusTask.getTaskConfigMap();
        List<Integer> taskIds = new ArrayList<>();
        taskConfigMap.forEach((key, value) -> taskIds.add(key));
        Collections.sort(taskIds);

        PlayerTask playerTask = TaskService.getInstance().getCurrentTask(currentTask.getTaskType(), currentTask.getPlayerId());
        PlayerTaskData playerTaskData = new PlayerTaskData();
        if (null != playerTask && TaskService.getDayDiffer(new Date(playerTask.getAcceptTime() * 1000L), new Date()) <= 1
                && taskIds.contains(playerTask.getTaskId())) {
            try {
                BeanUtils.copyProperties(playerTaskData, playerTask);

                if (playerTaskData.getTaskId() == firstTaskId) {
                    playerTaskData.setStarted(true);
                } else {
                    playerTaskData.setStarted(TaskService.getDayDiffer(new Date(playerTask.getAcceptTime() * 1000L), new Date()) == 1);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            int acceptTime = new Long(System.currentTimeMillis() / 1000).intValue();
            Map<String, Object> param = new HashMap<String, Object>() {{
                put("playerId", currentTask.getPlayerId());
                put("taskType", currentTask.getTaskType());
                put("taskId", firstTaskId);
                put("acceptTime", acceptTime);
            }};
            long playerTaskId = TaskService.getInstance().saveTask(param);

            playerTaskData.setId(new Long(playerTaskId).intValue());
            playerTaskData.setPlayerId(currentTask.getPlayerId());
            playerTaskData.setTaskType(currentTask.getTaskType());
            playerTaskData.setTaskId(firstTaskId);
            playerTaskData.setAcceptTime(acceptTime);
            playerTaskData.setStarted(true);
        }

        playerTaskData.setNo(taskIds.indexOf(playerTaskData.getTaskId()) + 1);

        PlayerCurrentTaskSuccess playerCurrentTaskSuccess = new PlayerCurrentTaskSuccess();
        playerCurrentTaskSuccess.setPlayerTaskData(playerTaskData);

        iSession.sendMessageByID(playerCurrentTaskSuccess, currentTask.getConnId());
    }

    public static void main(String[] args) {
        PlayerCurrentTask playerCurrentTask = new PlayerCurrentTask();
        playerCurrentTask.setPlayerId(10);
        playerCurrentTask.setTaskType(1);
        new TaskPlayerCurrentHandler().process(null, playerCurrentTask);
    }
}
