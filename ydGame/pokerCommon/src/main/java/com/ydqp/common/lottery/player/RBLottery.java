package com.ydqp.common.lottery.player;

import com.alibaba.fastjson.JSON;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.ydqp.common.data.LotteryDrawData;
import com.ydqp.common.entity.Lottery;
import com.ydqp.common.entity.LotteryConfig;
import com.ydqp.common.entity.PlayerLottery;
import com.ydqp.common.lottery.LotteryColorConstant;
import com.ydqp.common.utils.DateUtil;
import com.ydqp.common.utils.LotteryUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RBLottery extends AbstractLottery {

    private static final Logger logger = LoggerFactory.getLogger(RBLottery.class);

    @Override
    public LotteryDrawData lotteryDraw(Lottery lottery, List<PlayerLottery> playerLotteries) {
        //计算不同颜色、号码的中奖总金额
        Map<Integer, BigDecimal> drawNumMap = settleAwardPrice(playerLotteries);
        //根据value获取key排序，颜色倒序，数字顺序
        List<Integer> numList = sortMapByValues(drawNumMap);
        logger.info("计算不同号码的中奖总金额：{}", JSON.toJSONString(drawNumMap));
        logger.info("号码根据下注额排序：{}", JSON.toJSONString(numList));

        LotteryConfig config = ManageLottery.getInstance().getConfig(lottery.getType());

        //下注总金额
        BigDecimal amount = BigDecimal.ONE;
        if (CollectionUtils.isNotEmpty(playerLotteries)) {
            for (PlayerLottery playerLottery : playerLotteries) {
                if (playerLottery.getIsVir() == 1) continue;
                amount = amount.add(playerLottery.getPay());
            }
        }

        //判断开奖数字
        Integer drawNum = getDrawNum(numList, config, amount, lottery.getPeriod());

        String period = DateUtil.timestampToStr(lottery.getCreateTime()) + LotteryUtil.intToPeriod(lottery.getPeriod());
        logger.info("{}期开奖{}号码为：{}", period, lottery.getType(), drawNum);

        LotteryDrawData drawData = new LotteryDrawData();
        drawData.setDrawNum(String.valueOf(drawNum));
        drawData.setDrawNumMap(drawNumMap);
        return drawData;
    }

    @Override
    public Map<Integer, BigDecimal> settleAwardPrice(List<PlayerLottery> playerLotteries) {
        Map<Integer, BigDecimal> drawNumMap = new HashMap<Integer, BigDecimal>() {{
            put(0, BigDecimal.ZERO);
            put(1, BigDecimal.ZERO);
            put(2, BigDecimal.ZERO);
            put(3, BigDecimal.ZERO);
            put(4, BigDecimal.ZERO);
            put(5, BigDecimal.ZERO);
            put(6, BigDecimal.ZERO);
            put(7, BigDecimal.ZERO);
            put(8, BigDecimal.ZERO);
            put(9, BigDecimal.ZERO);
        }};

        if (CollectionUtils.isEmpty(playerLotteries)) return drawNumMap;

        for (PlayerLottery playerLottery : playerLotteries) {
            //有效下注
            BigDecimal effectiveBet = playerLottery.getPay().subtract(playerLottery.getFee());

            //下注颜色
            if (playerLottery.getSelected() != null) {
                //绿色
                if (LotteryColorConstant.GREEN.equals(playerLottery.getSelected())) {
                    drawNumMap.put(1, drawNumMap.get(1).add(effectiveBet.multiply(new BigDecimal("2"))));
                    drawNumMap.put(3, drawNumMap.get(3).add(effectiveBet.multiply(new BigDecimal("2"))));
                    drawNumMap.put(5, drawNumMap.get(5).add(effectiveBet.multiply(new BigDecimal("1.5"))));
                    drawNumMap.put(7, drawNumMap.get(7).add(effectiveBet.multiply(new BigDecimal("2"))));
                    drawNumMap.put(9, drawNumMap.get(9).add(effectiveBet.multiply(new BigDecimal("2"))));
                } else if (LotteryColorConstant.RED.equals(playerLottery.getSelected())) {
                    //红色
                    drawNumMap.put(0, drawNumMap.get(0).add(effectiveBet.multiply(new BigDecimal("1.5"))));
                    drawNumMap.put(2, drawNumMap.get(2).add(effectiveBet.multiply(new BigDecimal("2"))));
                    drawNumMap.put(4, drawNumMap.get(4).add(effectiveBet.multiply(new BigDecimal("2"))));
                    drawNumMap.put(6, drawNumMap.get(6).add(effectiveBet.multiply(new BigDecimal("2"))));
                    drawNumMap.put(8, drawNumMap.get(8).add(effectiveBet.multiply(new BigDecimal("2"))));
                } else {
                    //紫色
                    drawNumMap.put(0, drawNumMap.get(0).add(effectiveBet.multiply(new BigDecimal("4.5"))));
                    drawNumMap.put(5, drawNumMap.get(5).add(effectiveBet.multiply(new BigDecimal("4.5"))));
                }
            } else {
                //数字
                int num = Integer.parseInt(playerLottery.getNumber());
                drawNumMap.put(num, drawNumMap.get(num).add(effectiveBet.multiply(new BigDecimal("9"))));
            }
        }
        return drawNumMap;
    }

    @Override
    public PlayerLottery playerLotteryCheck(Lottery lottery, PlayerLottery playerLottery, Integer calculateTime) {
        //未中奖
        int drawNum = Integer.parseInt(lottery.getNumber());
        List<Integer> colorList = drawColorByNum(drawNum);
        if ((playerLottery.getSelected() != null && !colorList.contains(playerLottery.getSelected())) ||
                (StringUtils.isNotBlank(playerLottery.getNumber()) && !playerLottery.getNumber().equals(lottery.getNumber()))) {
            playerLottery.setStatus(2);
            playerLottery.setOpenTime(calculateTime + 20);
            playerLottery.setAward(BigDecimal.ZERO.subtract(playerLottery.getPay()));
        } else {
            BigDecimal effectiveBet = playerLottery.getPay().subtract(playerLottery.getFee());
            BigDecimal award = BigDecimal.ZERO;
            //下注颜色中奖
            if (playerLottery.getSelected() != null && colorList.contains(playerLottery.getSelected())) {
                //紫色
                if (playerLottery.getSelected().equals(LotteryColorConstant.VIOLET)) {
                    award = effectiveBet.multiply(new BigDecimal("4.5"));
                } else {
                    //红绿
                    BigDecimal awardRate = new BigDecimal((drawNum == 0 || drawNum == 5) ? "1.5" : "2");
                    award = effectiveBet.multiply(awardRate);
                }
            } else if (StringUtils.isNotBlank(playerLottery.getNumber()) && playerLottery.getNumber().equals(lottery.getNumber())) {
                //下注数字中奖
                award = effectiveBet.multiply(new BigDecimal("9"));
            }
            playerLottery.setStatus(1);
            playerLottery.setOpenTime(calculateTime + 20);
            playerLottery.setAward(award);
            playerLottery.setResult(lottery.getNumber());
        }
        playerLottery.setResult(lottery.getNumber());
        return playerLottery;
    }

    //根据开奖数字，判断开奖颜色
    private List<Integer> drawColorByNum(int num) {
        List<Integer> colorList = new ArrayList<>();
        if (num == 0 || num == 5) {
            colorList.add(LotteryColorConstant.VIOLET);
            colorList.add(num == 0 ? LotteryColorConstant.RED : LotteryColorConstant.GREEN);
        } else if (num % 2 == 0) {
            colorList.add(LotteryColorConstant.RED);
        } else {
            colorList.add(LotteryColorConstant.GREEN);
        }
        return colorList;
    }

    //    @Override
//    public void lotteryPay(Lottery lottery, List<PlayerLottery> playerLotteries) {
//        Map<Long, LotteryBattleRole> battleRoleMap = new HashMap<>();
//        //开奖通知的map
//        Map<Long, LotteryDrawNotificationSuc> notificationMap = new HashMap<>();
//        //中奖玩家connId
//        Set<Long> playerDataKeys = new HashSet<>();
//        for (PlayerLottery playerLottery : playerLotteries) {
//            LotteryBattleRole lotteryBattleRole = ManageLottery.getLotteryBattleRoleMap().get(playerLottery.getPlayerId());
//            if (lotteryBattleRole == null) continue;
//            long connId = lotteryBattleRole.getConnId();
//
//            LotteryDrawNotificationSuc suc = new LotteryDrawNotificationSuc();
//            suc.setPlayerId(playerLottery.getPlayerId());
//            if (playerLottery.getStatus() == 1) {
//                //中奖
//                playerDataKeys.add(connId);
//                suc.setWin(true);
//            } else {
//                //未中奖
//                suc.setWin(false);
//
//            }
//            notificationMap.put(connId, suc);
//            battleRoleMap.put(connId, lotteryBattleRole);
//        }
//
//        //批量获取中奖用户缓存数据
//        Map<Long, PlayerData> playerDataMap = new HashMap<>();
//        if (CollectionUtils.isNotEmpty(playerDataKeys)) {
//            List<PlayerData> playerDataList = PlayerCache.getInstance().getPlayers(new ArrayList<>(playerDataKeys));
//            playerDataMap = playerDataList.stream().collect(Collectors.toMap(PlayerData::getPlayerId, Function.identity()));
//        }
//
//        //要更新的用户集合、缓存
//        List<Object[]> updatePlayerList = new ArrayList<>();
//        Map<Long, PlayerData> connPlayerDataMap = new HashMap<>();
//        Map<Long, CoinPointSuccess> coinPointSuccessMap = new HashMap<>();
//        for (PlayerLottery playerLottery : playerLotteries) {
//            if (playerLottery.getStatus() == 2) continue;
//            BigDecimal award = playerLottery.getAward();
//
//            Object[] updatePlayer = new Object[]{award, playerLottery.getPlayerId()};
//            updatePlayerList.add(updatePlayer);
//
//            PlayerData playerData = playerDataMap.get(playerLottery.getPlayerId());
//            //在线
//            if (playerData != null) {
//                if (connPlayerDataMap.get(playerData.getSessionId()) == null) {
//                    playerData.setZjPoint(playerData.getZjPoint() + award.doubleValue());
//                    connPlayerDataMap.put(playerData.getSessionId(), playerData);
//                } else {
//                    PlayerData data = connPlayerDataMap.get(playerData.getSessionId());
//                    data.setZjPoint(data.getZjPoint() + award.doubleValue());
//                    connPlayerDataMap.put(playerData.getSessionId(), data);
//                }
//
//                CoinPointSuccess coinPointSuccess = new CoinPointSuccess();
//                coinPointSuccess.setCoinType(2);
//                coinPointSuccess.setCoinPoint(connPlayerDataMap.get(playerData.getSessionId()).getZjPoint());
//                coinPointSuccess.setPlayerId(playerLottery.getPlayerId());
//                coinPointSuccessMap.put(playerData.getSessionId(), coinPointSuccess);
//            }
//        }
//
//        Object[][] params = new Object[updatePlayerList.size()][];
//        for (int i = 0; i < updatePlayerList.size(); i++) {
//            params[i] = updatePlayerList.get(i);
//        }
//
//        //更新数据库
//        if (params.length != 0) {
//            PlayerService.getInstance().batchUpdate(params);
//        }
//
//        //批量更新缓存
//        if (connPlayerDataMap.size() != 0) {
//            PlayerCache.getInstance().addPlayers(connPlayerDataMap);
//        }
//
//        //通知客户端
//        battleRoleMap.forEach((connId, role) -> {
//            if (notificationMap.get(connId) != null) {
//                this.sendMessageToBattle(notificationMap.get(connId), battleRoleMap.get(connId));
//            }
//            if (coinPointSuccessMap.get(connId) != null) {
//                this.sendMessageToBattle(coinPointSuccessMap.get(connId), battleRoleMap.get(connId));
//            }
//        });
//    }
}
