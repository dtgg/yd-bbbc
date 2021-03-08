package com.ydqp.lottery.handler;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.lottery.player.ILottery;
import com.ydqp.common.lottery.player.ManageLottery;
import com.ydqp.common.receiveProtoMsg.lottery.LotteryEnterRoom;

@ServerHandler(module = "lottery", command = 5000001)
public class LotteryEnterRoomHandler implements IServerHandler {
    private static final Logger logger = LoggerFactory.getLogger(LotteryEnterRoomHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        LotteryEnterRoom lotteryEnterRoom = (LotteryEnterRoom) abstartParaseMessage;

        PlayerData playerData = PlayerCache.getInstance().getPlayer(lotteryEnterRoom.getConnId());
        if (playerData == null) {
            logger.error("player is not true");
            return;
        }

        ILottery iLottery = ManageLottery.getInstance().getLotteryByRoomIdAndType(lotteryEnterRoom.getRoomId(), 1);
        iLottery.enterLottery(playerData, iSession);
    }
}
