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
import com.ydqp.common.receiveProtoMsg.lottery.LotteryQuitRoom;
import com.ydqp.common.sendProtoMsg.lottery.LotteryQuitRoomSuc;
import com.ydqp.common.service.PlayerService;

@ServerHandler(module = "lottery", command = 5000003)
public class LotteryQuitRoomHandler implements IServerHandler {

    private static final Logger logger = LoggerFactory.getLogger(LotteryQuitRoomHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        LotteryQuitRoom lotteryQuitRoom = (LotteryQuitRoom) abstartParaseMessage;

        PlayerData playerData = PlayerCache.getInstance().getPlayer(lotteryQuitRoom.getConnId());
        if (playerData == null) {
            logger.error("player is not true");
            return;
        }

        logger.info("玩家退出抽奖房间，playerId:{}", lotteryQuitRoom.getPlayerId());

        playerData.setRoomId(0);
        PlayerCache.getInstance().addPlayer(lotteryQuitRoom.getConnId(), playerData);
        PlayerService.getInstance().updatePlayerRoomId(playerData.getPlayerId(), playerData.getRoomId());

        //移除房间角色
        ILottery iLottery = ManageLottery.getInstance().getLotteryByRoomIdAndType(playerData.getRoomId(), 1);
        if (iLottery != null) {
            ManageLottery.getLotteryBattleRoleMap().remove(playerData.getPlayerId());
        }

        LotteryQuitRoomSuc lotteryQuitRoomSuc = new LotteryQuitRoomSuc();
        lotteryQuitRoomSuc.setPlayerId(playerData.getPlayerId());

        iSession.sendMessageByID(lotteryQuitRoomSuc, lotteryQuitRoom.getConnId());
    }
}
