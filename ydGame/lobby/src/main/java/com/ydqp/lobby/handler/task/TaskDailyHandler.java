package com.ydqp.lobby.handler.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.constant.RewardSourceConstant;
import com.ydqp.common.dao.PlayerRewardHistoryDao;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.entity.Player;
import com.ydqp.common.entity.PlayerRewardHistory;
import com.ydqp.common.entity.PlayerTask;
import com.ydqp.common.entity.TaskConfig;
import com.ydqp.common.receiveProtoMsg.task.DailyTask;
import com.ydqp.common.sendProtoMsg.CoinPointSuccess;
import com.ydqp.common.sendProtoMsg.task.DailyTaskSuccess;
import com.ydqp.lobby.ThreadManager;
import com.ydqp.lobby.cache.PlayerCache;
import com.ydqp.lobby.constant.TaskType;
import com.ydqp.lobby.service.PlayerService;
import com.ydqp.lobby.service.TaskService;
import com.ydqp.lobby.task.DailyBonusTask;
import com.ydqp.lobby.task.TaskManager;

import java.util.*;

@ServerHandler(command = 1002002, module = "task")
public class TaskDailyHandler implements IServerHandler {
    private static Logger logger = LoggerFactory.getLogger(TaskDailyHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        logger.info("Execute sign in task request: {}", JSONObject.toJSONString(abstartParaseMessage));
        DailyTask dailyTask = (DailyTask) abstartParaseMessage;
        long playerId = dailyTask.getPlayerId();
        int taskId = dailyTask.getTaskId();

        DailyBonusTask dailyBonusTask = (DailyBonusTask) TaskManager.getInstance().getTask(TaskType.DAILY_TASK);
        boolean acceptTask = dailyBonusTask.acceptTask(taskId);
        if (!acceptTask) {
            DailyTaskSuccess dailyTaskSuccess = new DailyTaskSuccess();
            dailyTaskSuccess.setSignSuccess(false);
            dailyTaskSuccess.setMessage("The specified task does not exist");
            iSession.sendMessageByID(dailyTaskSuccess, dailyTask.getConnId());
            logger.error("需要执行的任务不存在,playerId:{},task:{}", playerId, taskId);
            return;
        }

        Map<Integer, TaskConfig> taskConfigMap = dailyBonusTask.getTaskConfigMap();
        int firstTaskId = dailyBonusTask.firstTask();
        Map<String, Object> param = new HashMap<String, Object>() {{
            put("playerId", playerId);
            put("taskType", TaskType.DAILY_TASK);
            put("taskId", firstTaskId);
            put("acceptTime", new Long(System.currentTimeMillis() / 1000).intValue());
        }};

        PlayerTask playerTask = TaskService.getInstance().getCurrentTask(TaskType.DAILY_TASK, playerId);
        if (null == playerTask || TaskService.getDayDiffer(new Date(playerTask.getAcceptTime() * 1000L), new Date()) > 1) {
            long playerTaskId = TaskService.getInstance().saveTask(param);
            playerTask = JSONObject.parseObject(JSON.toJSONString(param), PlayerTask.class);
            playerTask.setId(playerTaskId);
        }

        boolean isDone = true;
        String message = "";
        double coinPoint = 0;
        if (TaskService.getDayDiffer(new Date(playerTask.getAcceptTime() * 1000L), new Date()) == 0 && (firstTaskId != playerTask.getTaskId()
                || (firstTaskId == playerTask.getTaskId() && playerTask.getProgress() != 0))) {
            isDone = false;
            message = "You had been Checked in, please continue tomorrow";
            logger.error("重复操作,已完成当日的签到任务,playerId:{},task:{}", playerId, taskId);
        } else {
            boolean doTask = dailyBonusTask.doTask(playerTask.getTaskId());
            if (doTask) {
                int time = new Long(System.currentTimeMillis() / 1000).intValue();
                playerTask.setProgress(playerTask.getProgress() + 1);
                Object[] updateDailyParam = new Object[]{1, time, playerTask.getProgress(), 1, time, playerTask.getId()};
                TaskService.getInstance().doDailyTask(updateDailyParam);

                int reward = dailyBonusTask.getReward(playerTask.getTaskId());
                logger.info("完成签到任务,获得奖励：{},playerId:{},task:{}", reward, playerId, taskId);

                //更新玩家数据
                PlayerService.getInstance().updatePlayerZjPoint(reward, playerId);
                //更新缓存数据
                PlayerData playerData = PlayerCache.getInstance().getPlayer(dailyTask.getConnId());
                if (playerData != null) {
                    playerData.setZjPoint(playerData.getZjPoint() + reward);
                    PlayerCache.getInstance().addPlayer(dailyTask.getConnId(), playerData);

                    coinPoint = playerData.getZjPoint();
                } else {
                    Player player = PlayerService.getInstance().queryByCondition(String.valueOf(playerId));
                    coinPoint = player.getZjPoint();
                }
                PlayerRewardHistory rewardHistory = new PlayerRewardHistory();
                rewardHistory.setPlayerId(playerId);
                rewardHistory.setReward(reward);
                rewardHistory.setRewardSource(RewardSourceConstant.TASK);
                rewardHistory.setRewardType(taskConfigMap.get(playerTask.getTaskId()).getRewardType());
                rewardHistory.setTaskId(taskId);
                rewardHistory.setCreateTime(new Long(System.currentTimeMillis() / 1000).intValue());
                PlayerRewardHistoryDao.getInstance().insert(rewardHistory.getParameterMap());

//                Object[] completeParam = new Object[]{1, new Long(System.currentTimeMillis() / 1000).intValue(), playerTask.getId()};
//                TaskService.getInstance().completeTask(completeParam);
                //自动接下个任务
                Integer nextTaskId = taskConfigMap.get(playerTask.getTaskId()).getNextTaskId();
                if (nextTaskId != null) {
                    param.put("taskId", nextTaskId);
                    param.put("progress", playerTask.getProgress());
                }
                TaskService.getInstance().saveTask(param);
                logger.info("完成签到任务,下一个签到任务,playerId:{},nextTaskId:{}", playerId, nextTaskId);
            }
        }

        List<Integer> taskIds = new ArrayList<>();
        taskConfigMap.forEach((key, value) -> taskIds.add(key));
        Collections.sort(taskIds);
        int no = taskIds.indexOf(taskId) + 1;

        DailyTaskSuccess dailyTaskSuccess = new DailyTaskSuccess();
        dailyTaskSuccess.setSignSuccess(isDone);
        dailyTaskSuccess.setMessage(message);
        dailyTaskSuccess.setTaskId(taskId);
        dailyTaskSuccess.setTaskType(TaskType.DAILY_TASK);
        dailyTaskSuccess.setNo(no);
        iSession.sendMessageByID(dailyTaskSuccess, dailyTask.getConnId());

        CoinPointSuccess coinPointSuccess = new CoinPointSuccess();
        coinPointSuccess.setPlayerId(playerId);
        coinPointSuccess.setCoinPoint(coinPoint);
        coinPointSuccess.setCoinType(taskConfigMap.get(playerTask.getTaskId()).getRewardType());
        iSession.sendMessageByID(coinPointSuccess, dailyTask.getConnId());
    }
}
