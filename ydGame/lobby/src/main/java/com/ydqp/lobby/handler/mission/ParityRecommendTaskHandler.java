package com.ydqp.lobby.handler.mission;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.ManagePlayerPromote;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.dao.PlayerRewardHistoryDao;
import com.ydqp.common.dao.lottery.PlayerPromoteDao;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.data.TaskConfigData;
import com.ydqp.common.entity.PlayerPromote;
import com.ydqp.common.entity.PlayerRewardHistory;
import com.ydqp.common.entity.TaskConfig;
import com.ydqp.common.receiveProtoMsg.mission.ParityRecommendTask;
import com.ydqp.common.sendProtoMsg.mission.ParityRecommendTaskSuc;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ServerHandler(module = "mission", command = 1000073)
public class ParityRecommendTaskHandler implements IServerHandler {
    private static final Logger logger = LoggerFactory.getLogger(ParityRecommendTaskHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        ParityRecommendTask recommendTask = (ParityRecommendTask) abstartParaseMessage;

        PlayerData playerData = PlayerCache.getInstance().getPlayer(recommendTask.getConnId());
        if (playerData == null) {
            logger.error("player is not true");
            return;
        }

        PlayerPromote playerPromote = PlayerPromoteDao.getInstance().findByPlayerId(playerData.getPlayerId());
        int effectiveNum = playerPromote.getSubNum();

        List<TaskConfig> configs = ManagePlayerPromote.getInstance().getTaskConfigList();
        List<TaskConfigData> taskConfigDataList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(configs))
            taskConfigDataList = configs.stream().map(TaskConfigData::new).collect(Collectors.toList());

        List<PlayerRewardHistory> rewardHistories = PlayerRewardHistoryDao.getInstance().findBySourceAndPlayerId(playerData.getPlayerId());
        List<Integer> taskIds = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(rewardHistories))
            taskIds = rewardHistories.stream().map(PlayerRewardHistory::getTaskId).collect(Collectors.toList());
        String taskIdStr = "";
        if (CollectionUtils.isNotEmpty(taskIds))
            taskIdStr = StringUtils.join(taskIds, ",");

        ParityRecommendTaskSuc suc = new ParityRecommendTaskSuc();
        suc.setEffectiveNum(effectiveNum);
        suc.setTaskConfigDataList(taskConfigDataList);
        suc.setRewardTaskIds(taskIdStr);
        iSession.sendMessageByID(suc, recommendTask.getConnId());
    }
}
