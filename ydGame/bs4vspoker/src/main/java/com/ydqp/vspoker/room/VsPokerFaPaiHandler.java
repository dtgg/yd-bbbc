package com.ydqp.vspoker.room;

import com.alibaba.fastjson.JSONObject;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.ydqp.common.poker.Poker;
import com.ydqp.common.sendProtoMsg.vspoker.SVsFaPai;
import com.ydqp.vspoker.cache.RankingCache;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class VsPokerFaPaiHandler implements IRoomStatusHandler{

    private static final Logger logger = LoggerFactory.getLogger(VsPokerFaPaiHandler.class);
    @Override
    public void doHandler(VsPokerRoom vsPokerRoom) {
        SVsFaPai sVsFaPai = new SVsFaPai();
        sVsFaPai.setRoomId(vsPokerRoom.getRoomId());
        vsPokerRoom.sendMessageToBattles(sVsFaPai);

        //给五个位子发牌
        //1、生成牌堆
        //2、取前五张为5个位子的牌
        List<Poker> pokerList = vsPokerRoom.getICardPoker().generatePoker();
        vsPokerRoom.getPokerMap().put(1,pokerList.get(0));
        vsPokerRoom.getPokerMap().put(2,pokerList.get(1));
        vsPokerRoom.getPokerMap().put(3,pokerList.get(2));
        vsPokerRoom.getPokerMap().put(4,pokerList.get(3));
        vsPokerRoom.getPokerMap().put(5,pokerList.get(4));

        vsPokerRoom.setStatus(2);
        vsPokerRoom.setCurWaitTime(15);
        logger.info("fapai end");

        //加载当前排名信息
        Set<String> rankInfo = RankingCache.getInstance().getRankInfo(vsPokerRoom.getRaceId(), 0, -1);
        logger.info("排行榜信息：{}", JSONObject.toJSONString(rankInfo));
        if (CollectionUtils.isEmpty(rankInfo)) {
            vsPokerRoom.setRankPlayerIds(new ArrayList<>());
            return;
        }
        List<Long> rankPlayerIds = new ArrayList<>();
        for (String playerIdStr : rankInfo) {
            rankPlayerIds.add(Long.parseLong(playerIdStr));
        }
        vsPokerRoom.setRankPlayerIds(rankPlayerIds);
        vsPokerRoom.setAllIn(allIn());
    }

    private boolean allIn() {
        Random random = new Random();
        int anInt = random.nextInt(10);
        //20%
        return anInt < 2;
    }
}
