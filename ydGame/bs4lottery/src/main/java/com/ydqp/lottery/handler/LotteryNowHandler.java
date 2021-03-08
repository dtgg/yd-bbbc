package com.ydqp.lottery.handler;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.dao.lottery.LotteryDao;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.entity.Lottery;
import com.ydqp.common.sendProtoMsg.lottery.LotteryNowSuc;
import com.ydqp.common.sendProtoMsg.lottery.LotteryTypeInfo;
import com.ydqp.common.utils.LotteryUtil;
import com.ydqp.lottery.util.DateUtil;

import java.util.List;
import java.util.stream.Collectors;

@ServerHandler(module = "lottery", command = 5000009)
public class LotteryNowHandler implements IServerHandler {
    private static final Logger logger = LoggerFactory.getLogger(LotteryNowHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        PlayerData playerData = PlayerCache.getInstance().getPlayer(abstartParaseMessage.getConnId());
        if (playerData == null) {
            logger.error("player is not true");
            return;
        }

        List<Lottery> lotteries = LotteryDao.getInstance().findCurrentLottery();

        List<LotteryTypeInfo> infos = lotteries.stream().map(lottery -> {
            LotteryTypeInfo info = new LotteryTypeInfo();
            info.setLotteryId(lottery.getId());
            info.setType(lottery.getType());
            info.setPeriod(DateUtil.timestampToStr(lottery.getCreateTime()) + LotteryUtil.intToPeriod(lottery.getPeriod()));
            info.setCreateTime(lottery.getCreateTime());
            return info;
        }).collect(Collectors.toList());

        LotteryNowSuc lotteryNowSuc = new LotteryNowSuc();
        lotteryNowSuc.setLotteryTypeInfos(infos);
        iSession.sendMessageByID(lotteryNowSuc, abstartParaseMessage.getConnId());
    }
}
