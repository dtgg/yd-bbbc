package com.ydqp.lottery.handler;

import com.alibaba.fastjson.JSONObject;
import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.dao.lottery.LotteryDao;
import com.ydqp.common.entity.Lottery;
import com.ydqp.common.lottery.player.ManageLotteryRoom;
import com.ydqp.common.receiveProtoMsg.lottery.LotteryRaceSeed;
import com.ydqp.common.sendProtoMsg.lottery.LotteryRaceSeedSuc;
import com.ydqp.common.utils.CommonUtils;
import com.ydqp.lottery.Cache.LotteryCache;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ServerHandler(module = "lottery", command = 5000017)
public class LotteryRaceSeedHandler implements IServerHandler {
    private static final Logger logger = LoggerFactory.getLogger(LotteryRaceSeedHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        LotteryRaceSeed lotteryRaceSeed = (LotteryRaceSeed) abstartParaseMessage;

        List<Integer> type = ManageLotteryRoom.getInstance().getType(6000002);
        String types = CommonUtils.inString(type);

        int time = new Long(System.currentTimeMillis() / 1000L).intValue() - 180;
        List<Lottery> racingLotteries = LotteryDao.getInstance().findRacingLotteries(types, time);
        if (CollectionUtils.isEmpty(racingLotteries)) {
            logger.info("未查询到彩票信息");
            return;
        }

        Map<Integer, Integer> seedMap = new HashMap<>();

        String racingSeed = LotteryCache.getInstance().getRacingSeed();
        if (StringUtils.isBlank(racingSeed)) {
            for (Lottery lottery : racingLotteries) {
                seedMap.put(lottery.getId(), RandomUtils.nextInt(10));
            }
            LotteryCache.getInstance().setRacingSeed(JSONObject.toJSONString(seedMap));
        } else {
            JSONObject data = JSONObject.parseObject(racingSeed);
            if (data.get(String.valueOf(racingLotteries.get(0).getId())) == null) {
                for (Lottery lottery : racingLotteries) {
                    seedMap.put(lottery.getId(), RandomUtils.nextInt(10));
                }
                LotteryCache.getInstance().setRacingSeed(JSONObject.toJSONString(seedMap));
            } else {
                data.forEach((k, v) -> {
                    seedMap.put(Integer.parseInt(k), Integer.parseInt(String.valueOf(v)));
                });
            }
        }

        LotteryRaceSeedSuc raceSeedSuc = new LotteryRaceSeedSuc();
        raceSeedSuc.setSeedMap(seedMap);
        iSession.sendMessageByID(raceSeedSuc, lotteryRaceSeed.getConnId());
    }
}
