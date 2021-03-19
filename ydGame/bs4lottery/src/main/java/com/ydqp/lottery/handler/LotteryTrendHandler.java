package com.ydqp.lottery.handler;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.dao.lottery.LotteryDao;
import com.ydqp.common.entity.Lottery;
import com.ydqp.common.receiveProtoMsg.lottery.LotteryTrend;
import com.ydqp.common.sendProtoMsg.lottery.LotteryTrendSuc;

import java.util.List;
import java.util.stream.Collectors;

@ServerHandler(module = "lottery", command = 5000016)
public class LotteryTrendHandler implements IServerHandler {
    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        LotteryTrend lotteryTrend = (LotteryTrend) abstartParaseMessage;

        int createTime = new Long(System.currentTimeMillis() / 1000).intValue() - 86400;
        List<Lottery> completeLottery = LotteryDao.getInstance().findCompleteLotteryBy24H(lotteryTrend.getType(), createTime);
        List<String> collect = completeLottery.stream().map(Lottery::getNumber).collect(Collectors.toList());

        LotteryTrendSuc trendSuc = new LotteryTrendSuc();
        trendSuc.setNums(collect);
        iSession.sendMessageByID(trendSuc, lotteryTrend.getConnId());
    }
}
