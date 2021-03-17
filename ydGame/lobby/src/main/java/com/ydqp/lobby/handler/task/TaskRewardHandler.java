package com.ydqp.lobby.handler.task;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.entity.Player;
import com.ydqp.common.entity.PlayerTask;
import com.ydqp.common.receiveProtoMsg.task.TaskReward;
import com.ydqp.common.sendProtoMsg.CoinPointSuccess;
import com.ydqp.common.sendProtoMsg.task.TaskRewardSuccess;
import com.ydqp.lobby.cache.PlayerCache;
import com.ydqp.lobby.service.PlayerService;
import com.ydqp.lobby.service.TaskService;
import com.ydqp.lobby.task.AchievementTask;
import com.ydqp.lobby.task.TaskManager;


@ServerHandler(command = 1002005, module = "task")
public class TaskRewardHandler implements IServerHandler {
    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        TaskReward taskReward = (TaskReward) abstartParaseMessage;
        long playerId = taskReward.getPlayerId();
        AchievementTask achievementTask = (AchievementTask) TaskManager.getInstance().getTask(taskReward.getTaskType());

        boolean isReward = true;
        String message = "";
        double coinPoint = 0;
        PlayerTask playerTask = TaskService.getInstance().QueryByTaskIdAndPlayerId(taskReward.getTaskId(), playerId);
        if (playerTask == null) {
            isReward = false;
            message = "The task was not accepted by you";
        } else if (playerTask.getIsComplete() == 0) {
            isReward = false;
            message = "The task is not completed";
        } else if (playerTask.getIsReceived() == 1) {
            isReward = false;
            message = "The reward had been received";
        } else {
            int reward = achievementTask.getReward(taskReward.getTaskId());

            //更新玩家数据
            PlayerService.getInstance().updatePlayerCoinPoint(reward, playerId);
            //更新缓存数据
            PlayerData playerData = PlayerCache.getInstance().getPlayer(taskReward.getConnId());
            if (playerData != null) {
                PlayerCache.getInstance().addPlayer(taskReward.getConnId(), playerData);

            } else {
                Player player = PlayerService.getInstance().queryByCondition(String.valueOf(playerId));
            }

            Object[] completeParam = new Object[]{1, new Long(System.currentTimeMillis() / 1000).intValue(), playerTask.getId()};
            TaskService.getInstance().completeTask(completeParam);
        }

        TaskRewardSuccess taskRewardSuccess = new TaskRewardSuccess();
        taskRewardSuccess.setReward(isReward);
        taskRewardSuccess.setMessage(message);
        taskRewardSuccess.setTaskId(taskReward.getTaskId());
        taskRewardSuccess.setTaskType(taskReward.getTaskType());
        iSession.sendMessageByID(taskRewardSuccess, taskReward.getConnId());

        CoinPointSuccess coinPointSuccess = new CoinPointSuccess();
        coinPointSuccess.setPlayerId(playerId);
        coinPointSuccess.setCoinPoint(coinPoint);
        coinPointSuccess.setCoinType(1);
        iSession.sendMessageByID(coinPointSuccess, taskReward.getConnId());
    }
}
