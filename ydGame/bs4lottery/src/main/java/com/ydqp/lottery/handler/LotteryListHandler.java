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
import com.ydqp.common.data.Total;
import com.ydqp.common.entity.Lottery;
import com.ydqp.common.receiveProtoMsg.lottery.LotteryList;
import com.ydqp.common.sendProtoMsg.lottery.LotteryInfo;
import com.ydqp.common.sendProtoMsg.lottery.LotteryListSuc;
import com.ydqp.common.utils.LotteryUtil;
import com.ydqp.lottery.util.DateUtil;

import java.util.List;
import java.util.stream.Collectors;

@ServerHandler(module = "lottery", command = 5000005)
public class LotteryListHandler implements IServerHandler {
    private static final Logger logger = LoggerFactory.getLogger(LotteryListHandler.class);

    private static final int ONE_DAY = 86400;

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        LotteryList lotteryList = (LotteryList) abstartParaseMessage;

        PlayerData playerData = PlayerCache.getInstance().getPlayer(lotteryList.getConnId());
        if (playerData == null) {
            logger.error("player is not true");
            return;
        }

        int dateTime = new Long(System.currentTimeMillis() / 1000L).intValue() - ONE_DAY;
        int offset = (lotteryList.getPage() - 1) * lotteryList.getSize();
        List<Lottery> page = LotteryDao.getInstance().page(lotteryList.getType(), offset, lotteryList.getSize(), dateTime);
        List<LotteryInfo> lotteryInfos = page.stream().map(lottery -> {
            return new LotteryInfo(lottery, DateUtil.timestampToStr(lottery.getCreateTime()) + LotteryUtil.intToPeriod(lottery.getPeriod()));
        }).collect(Collectors.toList());
        Total total = LotteryDao.getInstance().count(lotteryList.getType(), dateTime);

        LotteryListSuc lotteryListSuc = new LotteryListSuc();
        lotteryListSuc.setLotteryList(lotteryInfos);
        lotteryListSuc.setCount(total.getTotal());
        iSession.sendMessageByID(lotteryListSuc, lotteryList.getConnId());
    }
}
