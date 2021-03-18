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
import com.ydqp.common.lottery.player.ManageLotteryRoom;
import com.ydqp.common.receiveProtoMsg.lottery.LotteryDrawInfo;
import com.ydqp.common.sendProtoMsg.lottery.LotteryDrawNum;
import com.ydqp.lottery.Cache.LotteryCache;

@ServerHandler(module = "lottery", command = 5000008)
public class LotteryDrawInfoHandler implements IServerHandler {
    private static final Logger logger = LoggerFactory.getLogger(LotteryDrawInfoHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        LotteryDrawInfo lotteryDrawInfo = (LotteryDrawInfo) abstartParaseMessage;
        PlayerData playerData = PlayerCache.getInstance().getPlayer(abstartParaseMessage.getConnId());
        if (playerData == null) {
            logger.error("player is not true");
            return;
        }

        Integer roomId = ManageLotteryRoom.getInstance().getRoomId(lotteryDrawInfo.getType());
        LotteryDrawNum lotteryDrawNum = LotteryCache.getInstance().getDrawInfo(LotteryCache.DRAW_INFO_KEY + roomId);
        if (lotteryDrawNum == null) {
            logger.info("客户端请求开奖数据为空");
            return;
        }

        //判断时间
        int lotteryId = lotteryDrawNum.getDrawNumInfos().get(0).getLotteryId();
        Lottery lottery = LotteryDao.getInstance().findById(lotteryId);

        int nowTime = new Long(System.currentTimeMillis() / 1000L).intValue();
        if (lottery.getOpenTime() - nowTime > 1) {
            logger.info("味道开奖时间");
            return;
        }

        iSession.sendMessageByID(lotteryDrawNum, abstartParaseMessage.getConnId());
    }
}
