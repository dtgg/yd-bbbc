package com.ydqp.lottery.handler;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.sendProtoMsg.lottery.LotteryDrawNum;
import com.ydqp.lottery.Cache.LotteryCache;

@ServerHandler(module = "lottery", command = 5000008)
public class LotteryDrawInfoHandler implements IServerHandler {
    private static final Logger logger = LoggerFactory.getLogger(LotteryDrawInfoHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        PlayerData playerData = PlayerCache.getInstance().getPlayer(abstartParaseMessage.getConnId());
        if (playerData == null) {
            logger.error("player is not true");
            return;
        }

        LotteryDrawNum lotteryDrawNum = LotteryCache.getInstance().getDrawInfo();
        if (lotteryDrawNum == null) {
            logger.info("客户端请求开奖数据为空");
            return;
        }
        iSession.sendMessageByID(lotteryDrawNum, abstartParaseMessage.getConnId());
    }
}
