package com.ydqp.lottery.handler;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.entity.Player;
import com.ydqp.common.lottery.player.ILottery;
import com.ydqp.common.lottery.player.ManageLottery;
import com.ydqp.common.receiveProtoMsg.lottery.LotteryComebackRoom;
import com.ydqp.common.sendProtoMsg.lottery.LotteryQuitRoomSuc;

@ServerHandler(module = "lottery", command = 5000004)
public class LotteryComebackHandler implements IServerHandler {
    private static final Logger logger = LoggerFactory.getLogger(LotteryComebackHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        LotteryComebackRoom lotteryComebackRoom = (LotteryComebackRoom) abstartParaseMessage;

        PlayerData playerData = PlayerCache.getInstance().getPlayer(lotteryComebackRoom.getConnId());
        if (playerData == null) {
            logger.error("player is not true");
            return;
        }

        ILottery iLottery = ManageLottery.getInstance().getLotteryByRoomIdAndType(playerData.getRoomId(), 1);
        if (iLottery == null) {
            LotteryQuitRoomSuc lotteryQuitRoomSuc = new LotteryQuitRoomSuc();
            lotteryQuitRoomSuc.setPlayerId(playerData.getPlayerId());
            iSession.sendMessageByID(lotteryQuitRoomSuc, lotteryComebackRoom.getConnId());
            return;
        }
        boolean b = iLottery.comebackLottery(playerData, iSession);
        if (!b) {
            LotteryQuitRoomSuc lotteryQuitRoomSuc = new LotteryQuitRoomSuc();
            lotteryQuitRoomSuc.setPlayerId(playerData.getPlayerId());
            iSession.sendMessageByID(lotteryQuitRoomSuc, lotteryComebackRoom.getConnId());
        }
    }
}
