package com.ydqp.vspoker.handler;

import com.alibaba.fastjson.JSONObject;
import com.cfq.annotation.GenProto;
import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.receiveProtoMsg.vspoker.VsPokerRanking;
import com.ydqp.common.sendProtoMsg.vspoker.SVsPlayerRankData;
import com.ydqp.common.sendProtoMsg.vspoker.SVsPokerRaking;
import com.ydqp.vspoker.cache.RankingCache;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@ServerHandler(command = 7000013, module = "vsPoker")
public class VsPokerRankingHandler implements IServerHandler {
    private static final Logger logger = LoggerFactory.getLogger(VsPokerRankingHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        VsPokerRanking rank = (VsPokerRanking) abstartParaseMessage;

        PlayerData playerData = PlayerCache.getInstance().getPlayer(rank.getConnId());
        if (playerData == null) {
            logger.error("player is not true");
            return;
        }

        Set<String> rankInfo = RankingCache.getInstance().getRankInfo(rank.getRaceId());
        List<SVsPlayerRankData> list = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(rankInfo)) {
            for (String data : rankInfo) {
                SVsPlayerRankData SPlayerRankData = JSONObject.parseObject(data, SVsPlayerRankData.class);
                list.add(SPlayerRankData);
            }
        }

        SVsPokerRaking sVsPokerRaking = new SVsPokerRaking();
        sVsPokerRaking.setSPlayerRankData(list);
        iSession.sendMessageByID(sVsPokerRaking, rank.getConnId());
    }
}
