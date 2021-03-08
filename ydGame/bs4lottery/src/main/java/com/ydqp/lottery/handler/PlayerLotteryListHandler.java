package com.ydqp.lottery.handler;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.dao.lottery.PlayerLotteryDao;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.data.Total;
import com.ydqp.common.entity.PlayerLottery;
import com.ydqp.common.receiveProtoMsg.lottery.PlayerLotteryList;
import com.ydqp.common.sendProtoMsg.lottery.PlayerLotteryInfo;
import com.ydqp.common.sendProtoMsg.lottery.PlayerLotteryListSuc;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@ServerHandler(module = "lottery", command = 5000006)
public class PlayerLotteryListHandler implements IServerHandler {

    private static final Logger logger = LoggerFactory.getLogger(PlayerLotteryListHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        PlayerLotteryList playerLotteryList = (PlayerLotteryList) abstartParaseMessage;

        PlayerData playerData = PlayerCache.getInstance().getPlayer(playerLotteryList.getConnId());
        if (playerData == null) {
            logger.error("player is not true");
            return;
        }

        int offset = (playerLotteryList.getPage() - 1) * playerLotteryList.getSize();
        List<PlayerLottery> page = PlayerLotteryDao.getInstance().page(
                playerData.getPlayerId(), playerLotteryList.getType(), offset, playerLotteryList.getSize());
        int nowTime = new Long(System.currentTimeMillis() / 1000).intValue();
        List<PlayerLotteryInfo> infos = page.stream().map(playerLottery -> {
            if (playerLottery.getOpenTime() != 0 && nowTime < playerLottery.getOpenTime()) {
                playerLottery.setStatus(0);
                playerLottery.setAward(BigDecimal.ZERO);
                playerLottery.setOpenTime(0);
            }
            return new PlayerLotteryInfo(playerLottery);
        }).collect(Collectors.toList());
        Total total = PlayerLotteryDao.getInstance().count(playerData.getPlayerId(), playerLotteryList.getType());

        PlayerLotteryListSuc playerLotteryListSuc = new PlayerLotteryListSuc();
        playerLotteryListSuc.setPlayerLotteryInfoList(infos);
        playerLotteryListSuc.setCount(total.getTotal());

        iSession.sendMessageByID(playerLotteryListSuc, playerLotteryList.getConnId());
    }
}
