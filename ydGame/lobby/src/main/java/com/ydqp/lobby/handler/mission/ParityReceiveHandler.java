package com.ydqp.lobby.handler.mission;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.ManagePlayerPromote;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.constant.TaskRewardSource;
import com.ydqp.common.dao.PlayerRewardHistoryDao;
import com.ydqp.common.dao.lottery.PlayerPromoteDao;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.entity.PlayerPromote;
import com.ydqp.common.entity.PlayerRewardHistory;
import com.ydqp.common.entity.TaskConfig;
import com.ydqp.common.receiveProtoMsg.mission.ParityReceive;
import com.ydqp.common.sendProtoMsg.CoinPointSuccess;
import com.ydqp.common.sendProtoMsg.mission.ParityReceiveSuc;
import com.ydqp.common.service.PlayerService;

@ServerHandler(module = "mission", command = 1000074)
public class ParityReceiveHandler implements IServerHandler {
    private static final Logger logger = LoggerFactory.getLogger(ParityReceiveHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        ParityReceive parityReceive = (ParityReceive) abstartParaseMessage;

        PlayerData playerData = PlayerCache.getInstance().getPlayer(parityReceive.getConnId());
        if (playerData == null) {
            logger.error("player is not true");
            return;
        }

        ParityReceiveSuc suc = new ParityReceiveSuc();

        PlayerRewardHistory taskRewardHistory = PlayerRewardHistoryDao.getInstance().findByTaskIdAndPlayerId(parityReceive.getTaskId(), parityReceive.getPlayerId());
        if (taskRewardHistory != null) {
            suc.setSuccess(false);
            suc.setMessage("Task reward has been collected");
            iSession.sendMessageByID(suc, parityReceive.getConnId());
            logger.error("推广奖励已领取，重复操作");
            return;
        }

            TaskConfig taskConfig = ManagePlayerPromote.getInstance().getTaskConfig(parityReceive.getTaskId());
        if (taskConfig == null) {
            suc.setSuccess(false);
            suc.setMessage("Invalid task ID");
            iSession.sendMessageByID(suc, parityReceive.getConnId());
            return;
        }
        logger.info("领取推广任务{}的奖励需要的有效用户数为{}", taskConfig.getId(), taskConfig.getTarget());

        PlayerPromote playerPromote = PlayerPromoteDao.getInstance().findByPlayerId(playerData.getPlayerId());
        logger.info("{}的有效用户数量为{}", playerData.getPlayerId(), playerPromote.getSubNum());

        if (playerPromote.getSubNum() < taskConfig.getTarget()) {
            suc.setSuccess(false);
            suc.setMessage("Failed to meet the conditions for receiving rewards");
            iSession.sendMessageByID(suc, parityReceive.getConnId());
            return;
        }

        //达到条件,更新
        PlayerService.getInstance().updatePlayerZjPoint(taskConfig.getReward(), playerData.getPlayerId());
        double zj = playerData.getZjPoint();
        playerData.setZjPoint(playerData.getZjPoint() + taskConfig.getReward());
        PlayerCache.getInstance().addPlayer(parityReceive.getConnId(), playerData);

        logger.info("{}领取了推广任务奖励，领取前金币：{}，领取后金币：{}", playerData.getPlayerId(), zj, playerData.getZjPoint());

        //记录奖励记录
        PlayerRewardHistory playerRewardHistory = new PlayerRewardHistory();
        playerRewardHistory.setPlayerId(playerData.getPlayerId());
        playerRewardHistory.setSource(TaskRewardSource.RECOMMEND_TASK);
        playerRewardHistory.setReward(taskConfig.getReward());
        playerRewardHistory.setTaskId(parityReceive.getTaskId());
        playerRewardHistory.setCreateTime(new Long(System.currentTimeMillis() / 1000).intValue());
        playerRewardHistory.setAppId(playerData.getAppId());
        PlayerRewardHistoryDao.getInstance().insert(playerRewardHistory.getParameterMap());

        //通知客户端
        suc.setSuccess(true);
        suc.setAmount(taskConfig.getReward());
        iSession.sendMessageByID(suc, parityReceive.getConnId());

        CoinPointSuccess coinPointSuccess = new CoinPointSuccess();
        coinPointSuccess.setPlayerId(playerData.getPlayerId());
        coinPointSuccess.setCoinPoint(playerData.getZjPoint());
        iSession.sendMessageByID(coinPointSuccess, parityReceive.getConnId());
    }
}
