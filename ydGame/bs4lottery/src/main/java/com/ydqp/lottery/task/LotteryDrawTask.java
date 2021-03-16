package com.ydqp.lottery.task;

import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.ydqp.common.dao.lottery.LotteryDao;
import com.ydqp.common.dao.lottery.PlayerLotteryDao;
import com.ydqp.common.data.LotteryDrawData;
import com.ydqp.common.entity.Lottery;
import com.ydqp.common.entity.PlayerLottery;
import com.ydqp.common.lottery.LotteryColorConstant;
import com.ydqp.common.lottery.player.ILottery;
import com.ydqp.common.lottery.player.ManageLottery;
import com.ydqp.common.lottery.player.ManageLotteryRoom;
import com.ydqp.common.lottery.role.LotteryBattleRole;
import com.ydqp.common.sendProtoMsg.lottery.LotteryDrawNum;
import com.ydqp.common.sendProtoMsg.lottery.LotteryDrawNumInfo;
import com.ydqp.common.sendProtoMsg.lottery.LotteryTypeInfo;
import com.ydqp.common.utils.CommonUtils;
import com.ydqp.common.utils.LotteryUtil;
import com.ydqp.lottery.Cache.LotteryCache;
import com.ydqp.lottery.util.DateUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 根据下注情况开奖
 */
public class LotteryDrawTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(LotteryDrawTask.class);

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();

        int time = new Long(System.currentTimeMillis() / 1000).intValue();
        //两分半前未开奖的期数
        List<Lottery> lotteries = LotteryDao.getInstance().findByStatus(0, time - 160);
        if (CollectionUtils.isEmpty(lotteries)) return;

        List<Integer> lotteryIds = lotteries.stream().map(Lottery::getId).collect(Collectors.toList());
        //未开奖的下注
        List<PlayerLottery> playerLotteryList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(lotteryIds)) {
            playerLotteryList = PlayerLotteryDao.getInstance().findByLotteryIds(lotteryIds);
        }

        Map<Integer, List<PlayerLottery>> playerLotteriesMap = new HashMap<>();
        playerLotteryList.forEach(playerLottery -> {
            if (playerLotteriesMap.get(playerLottery.getLotteryId()) == null) {
                playerLotteriesMap.put(playerLottery.getLotteryId(), new ArrayList<PlayerLottery>(){{add(playerLottery);}});
            } else {
                playerLotteriesMap.get(playerLottery.getLotteryId()).add(playerLottery);
            }
        });

        int dbQueryTime = new Long(System.currentTimeMillis() / 1000).intValue();
        if (dbQueryTime - startTime > 1000) {
            logger.warn("lottery查询慢日志，执行时间：{}", dbQueryTime - startTime);
        }

        List<Lottery> updateLotteryList = new ArrayList<>();
        List<PlayerLottery> updatePlayerLotteryList = new ArrayList<>();
        Map<Long, LotteryBattleRole> lotteryBattleRoleMap = ManageLottery.getLotteryBattleRoleMap();
        List<LotteryDrawNumInfo> drawNumInfos = new ArrayList<>();
        for (Lottery lottery : lotteries) {
            //达到开奖时间
            if (time - lottery.getCreateTime() >= 160) {
                ILottery iLottery = ManageLottery.getInstance().getLotteryByType(lottery.getType());

                List<PlayerLottery> playerLotteries = playerLotteriesMap.get(lottery.getId());
                //计算开奖号码
                //各个号码开奖金额
//                Map<Integer, BigDecimal> drawNumMap = iLottery.settleAwardPrice(playerLotteries);
                LotteryDrawData drawData = iLottery.lotteryDraw(lottery, playerLotteries);
                if (!StringUtils.isBlank(lottery.getNumber())) {
                    drawData.setDrawNum(lottery.getNumber());
                }
                //更新lottery数据，已计算
                lottery.setNumber(drawData.getDrawNum());
                lottery.setStatus(1);
                lottery.setOpenTime(time + 20);
                lottery.setPrice(LotteryUtil.getPrice() + lottery.getPeriod());

                int drawNum = Integer.parseInt(drawData.getDrawNum());

                BigDecimal totalPay = BigDecimal.ZERO;
                BigDecimal totalFee = BigDecimal.ZERO;
                if (CollectionUtils.isNotEmpty(playerLotteries)) {
                    for (PlayerLottery playerLottery : playerLotteries) {
                        totalPay = totalPay.add(playerLottery.getPay());
                        totalFee = totalFee.add(playerLottery.getFee());

                        iLottery.playerLotteryCheck(lottery, playerLottery, time);

                        updatePlayerLotteryList.add(playerLottery);
                    }
                }

                lottery.setTotalPay(totalPay);
                lottery.setTotalFee(totalFee);
                lottery.setTotalAward(drawData.getDrawNumMap().get(drawNum));
                lottery.setTotalProfit(lottery.getTotalPay().subtract(lottery.getTotalAward()));
                updateLotteryList.add(lottery);

                //开奖结果通知客户端
                if (time - lottery.getCreateTime() <= 180) {
                    LotteryDrawNumInfo lotteryDrawNum = new LotteryDrawNumInfo();
                    lotteryDrawNum.setLotteryId(lottery.getId());
                    lotteryDrawNum.setType(lottery.getType());
                    lotteryDrawNum.setPeriod(DateUtil.timestampToStr(lottery.getCreateTime()) + LotteryUtil.intToPeriod(lottery.getPeriod()));
                    lotteryDrawNum.setDrawNum(drawNum);
                    drawNumInfos.add(lotteryDrawNum);
                }
            }
        }

        long dbUpdateStartTime = System.currentTimeMillis();
        //更新lottery
        Object[][] lotteryParams = new Object[updateLotteryList.size()][4];
        for (int i = 0; i < updateLotteryList.size(); i++) {
            Lottery lottery = updateLotteryList.get(i);
            lotteryParams[i] = new Object[]{lottery.getPrice(), lottery.getNumber(), lottery.getStatus(),
                    lottery.getOpenTime(), lottery.getTotalPay(), lottery.getTotalAward(), lottery.getTotalProfit(),
                    lottery.getTotalFee(), lottery.getId()};
        }
        if (lotteryParams.length > 0)
            LotteryDao.getInstance().batchUpdate(lotteryParams);

        //更新用户彩票状态
        Object[][] playerLotteryParams = new Object[updatePlayerLotteryList.size()][4];
        for (int i = 0; i < updatePlayerLotteryList.size(); i++) {
            PlayerLottery playerLottery = updatePlayerLotteryList.get(i);
            playerLotteryParams[i] = new Object[]{playerLottery.getStatus(), playerLottery.getAward(),
                    playerLottery.getOpenTime(), playerLottery.getResult(), playerLottery.getId()};
        }
        if (playerLotteryParams.length > 0)
            PlayerLotteryDao.getInstance().batchUpdate(playerLotteryParams);

        long dbUpdateEndTime = System.currentTimeMillis();
        if (dbUpdateEndTime - dbUpdateStartTime > 500) {
            logger.warn("lottery更新慢日志， 执行时间：{}", dbUpdateEndTime - dbUpdateStartTime);
        }

        //下一期
        List<Integer> rbRoomLotteryTypes = ManageLotteryRoom.getInstance().getType(5000001);
        List<Integer> bjRoomLotteryTypes = ManageLotteryRoom.getInstance().getType(6000001);
        rbRoomLotteryTypes.addAll(bjRoomLotteryTypes);
        String rbTypes = CommonUtils.inString(rbRoomLotteryTypes);

        Map<Integer, List<LotteryTypeInfo>> lotteryTypeInfoMap = new HashMap<>(16);
        List<Lottery> nextLotteries = LotteryDao.getInstance().findNextLottery(rbTypes, rbRoomLotteryTypes.size());
        nextLotteries.forEach(lottery -> {
            LotteryTypeInfo info = new LotteryTypeInfo();
            info.setLotteryId(lottery.getId());
            info.setType(lottery.getType());
            info.setPeriod(DateUtil.timestampToStr(lottery.getCreateTime()) + LotteryUtil.intToPeriod(lottery.getPeriod()));
            info.setCreateTime(lottery.getCreateTime());
            Integer roomId = ManageLotteryRoom.getInstance().getRoomId(lottery.getType());
            if (lotteryTypeInfoMap.get(roomId) == null) {
                lotteryTypeInfoMap.put(roomId, new ArrayList<LotteryTypeInfo>() {{add(info);}});
            } else {
                lotteryTypeInfoMap.get(roomId).add(info);
            }
        });

        Map<Integer, List<LotteryDrawNumInfo>> lotteryDrawInfoMap = new HashMap<>(16);
        drawNumInfos.forEach(lotteryDrawNumInfo -> {
            Integer roomId = ManageLotteryRoom.getInstance().getRoomId(lotteryDrawNumInfo.getType());
            if (lotteryDrawInfoMap.get(roomId) == null) {
                lotteryDrawInfoMap.put(roomId, new ArrayList<LotteryDrawNumInfo>() {{add(lotteryDrawNumInfo);}});
            } else {
                lotteryDrawInfoMap.get(roomId).add(lotteryDrawNumInfo);
            }
        });

        //发送开奖号码
//        lotteryBattleRoleMap.forEach((key, value) -> {
//            LotteryDrawNum lotteryDrawNum = new LotteryDrawNum();
//            lotteryDrawNum.setDrawNumInfos(lotteryDrawInfoMap.get(value.getRoomId()));
//            lotteryDrawNum.setLotteryTypeInfos(lotteryTypeInfoMap.get(value.getRoomId()));
//            if (value.getRoomId() == 6000001) {
//                lotteryDrawNum.setSeed(RandomUtils.nextInt(10));
//            }
//            value.getISession().sendMessageByID(lotteryDrawNum, value.getConnId());
//        });
        //缓存开奖结果
        lotteryDrawInfoMap.forEach((roomId, lotteryDrawInfo) -> {
            LotteryDrawNum lotteryDrawNum = new LotteryDrawNum();
            lotteryDrawNum.setDrawNumInfos(lotteryDrawInfoMap.get(roomId));
            lotteryTypeInfoMap.forEach((roomId2, lotteryTypeInfo) -> {
                if (roomId.equals(roomId2)) {
                    lotteryDrawNum.setLotteryTypeInfos(lotteryTypeInfoMap.get(roomId2));
                    LotteryCache.getInstance().addDrawInfo(LotteryCache.DRAW_INFO_KEY + roomId, lotteryDrawNum);
                }
            });
        });

        long endTime = System.currentTimeMillis();
        if (endTime - startTime > 2000) {
            logger.warn("开奖慢日志，执行时间：{}", endTime - startTime);
        }
    }
}
