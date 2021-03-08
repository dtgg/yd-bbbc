package com.ydqp.lobby.handler.task;

import com.alibaba.fastjson.JSONObject;
import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.data.TaskConfigData;
import com.ydqp.common.receiveProtoMsg.task.TaskCommon;
import com.ydqp.common.sendProtoMsg.task.TaskCommonSuccess;
import com.ydqp.lobby.service.TaskService;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

@ServerHandler(command = 1002001, module = "task")
public class TaskHandler implements IServerHandler {
    private static Logger logger = LoggerFactory.getLogger(TaskHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        logger.info("Get task config request: {}", JSONObject.toJSONString(abstartParaseMessage));
        TaskCommon taskCommon = (TaskCommon) abstartParaseMessage;

        List<TaskConfigData> tasks = TaskService.getInstance().getTasks(taskCommon.getTaskType()).stream()
                .map(taskConfig -> {
                    TaskConfigData taskConfigData = new TaskConfigData();
                    try {
                        BeanUtils.copyProperties(taskConfigData, taskConfig);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    return taskConfigData;
                }).collect(Collectors.toList());

        TaskCommonSuccess taskCommonSuccess = new TaskCommonSuccess();
        taskCommonSuccess.setData(tasks);

        iSession.sendMessageByID(taskCommonSuccess, taskCommon.getConnId());
    }
}
