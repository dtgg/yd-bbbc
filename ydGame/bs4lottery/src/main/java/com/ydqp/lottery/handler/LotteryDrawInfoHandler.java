package com.ydqp.lottery.handler;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.dao.lottery.LotteryDao;
import com.ydqp.common.dao.lottery.PlayerLotteryDao;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.entity.Lottery;
import com.ydqp.common.entity.PlayerLottery;
import com.ydqp.common.lottery.player.ManageLotteryRoom;
import com.ydqp.common.receiveProtoMsg.lottery.LotteryDrawInfo;
import com.ydqp.common.sendProtoMsg.lottery.*;
import com.ydqp.common.utils.CommonUtils;
import com.ydqp.lottery.Cache.LotteryCache;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
            logger.info("客户端请求开奖数据为空，playerId:{}", playerData.getPlayerId());
            return;
        }

        int nowTime = new Long(System.currentTimeMillis() / 1000L).intValue();
        //判断时间
        Lottery nowLottery = LotteryDao.getInstance().getNowLottery(nowTime);
        if (nowLottery == null) {
            logger.info("未到开奖时间，playerId:{}", playerData.getPlayerId());
            return;
        }

        List<Integer> collect = new ArrayList<>();
        lotteryDrawNum.getDrawNumInfos().forEach(lotteryDrawNumInfo -> collect.add(lotteryDrawNumInfo.getLotteryId()));
        List<PlayerLottery> playerLotteries = PlayerLotteryDao.getInstance().findPlayerLotteries(CommonUtils.inString(collect), playerData.getPlayerId());

        if (CollectionUtils.isEmpty(playerLotteries)) {
            logger.info("未购买彩票，playerId:{}", playerData.getPlayerId());
            lotteryDrawNum.setLotteryTypeListInfos(new ArrayList<>());
            iSession.sendMessageByID(lotteryDrawNum, abstartParaseMessage.getConnId());
            return;
        }

        List<LotteryTypeListInfo> lotteryTypeListInfos = new ArrayList<>();
        playerLotteries.forEach(playerLottery -> {
            LotteryTypeListInfo lotteryTypeListInfo = new LotteryTypeListInfo();
            lotteryTypeListInfo.setType(playerLottery.getType());
            if (CollectionUtils.isEmpty(lotteryTypeListInfo.getPlayerLotteryInfos())) {
                lotteryTypeListInfo.setPlayerLotteryInfos(new ArrayList<PlayerLotteryInfo>() {{
                    add(new PlayerLotteryInfo(playerLottery));
                }});
            } else {
                lotteryTypeListInfo.getPlayerLotteryInfos().add(new PlayerLotteryInfo(playerLottery));
            }
            lotteryTypeListInfos.add(lotteryTypeListInfo);
        });

        lotteryDrawNum.setLotteryTypeListInfos(lotteryTypeListInfos);
        iSession.sendMessageByID(lotteryDrawNum, abstartParaseMessage.getConnId());
    }

    public static void main(String[] args) {
        int nowTime = new Long(System.currentTimeMillis() / 1000L).intValue();
        Lottery nowLottery = LotteryDao.getInstance().getNowLottery(nowTime);
        System.out.println(nowLottery);
    }
}
