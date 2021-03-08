package com.ydqp.common.lottery.player;

import com.alibaba.fastjson.JSON;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.dao.lottery.PlayerLotteryDao;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.entity.Lottery;
import com.ydqp.common.entity.LotteryConfig;
import com.ydqp.common.entity.PlayerLottery;
import com.ydqp.common.lottery.LotteryColorConstant;
import com.ydqp.common.lottery.role.LotteryBattleRole;
import com.ydqp.common.sendProtoMsg.CoinPointSuccess;
import com.ydqp.common.sendProtoMsg.lottery.LotteryDrawNotificationSuc;
import com.ydqp.common.service.PlayerService;
import com.ydqp.common.utils.DateUtil;
import com.ydqp.common.utils.LotteryUtil;
import org.apache.commons.collections.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ALottery extends AbstractLottery {

    private static final Logger logger = LoggerFactory.getLogger(ALottery.class);

    @Override
    public PlayerLottery lotteryBuy(Lottery lottery, PlayerLottery playerLottery) {
        LotteryConfig config = ManageLottery.getInstance().getConfig(lottery.getType());

        BigDecimal pay = playerLottery.getPay();
        //下注值少于100卢比收取10%手续费;下注值大等于100卢比收取2%手续费
        BigDecimal fee = pay.multiply(pay.compareTo(new BigDecimal("100")) < 0 ? config.getFeeRateMax() : config.getFeeRateMin());
        playerLottery.setFee(fee);
        playerLottery.setCreateTime(new Long(System.currentTimeMillis() / 1000).intValue());

        long primaryKey = PlayerLotteryDao.getInstance().insert(playerLottery.getParameterMap());
        playerLottery.setId(primaryKey);
        return playerLottery;
    }

    @Override
    public String lotteryDraw(Lottery lottery, List<PlayerLottery> playerLotteries) {
        //计算不同颜色、号码的中奖总金额
        Map<Integer, BigDecimal> drawNumMap = settleAwardPrice(playerLotteries);
        //根据value获取key排序，颜色倒序，数字顺序
        List<Integer> numList = sortMapByValues(drawNumMap);
        logger.info("计算不同号码的中奖总金额：{}", JSON.toJSONString(drawNumMap));
        logger.info("号码根据下注额排序：{}", JSON.toJSONString(numList));

        LotteryConfig config = ManageLottery.getInstance().getConfig(lottery.getType());

        //判断开奖数字
        Integer drawNum = getDrawNum(numList, config, drawNumMap, lottery.getPeriod());


        String period = DateUtil.timestampToStr(lottery.getCreateTime()) + LotteryUtil.intToPeriod(lottery.getPeriod());
        logger.info("{}期开奖{}号码为：{}", period, lottery.getType(), drawNum);
        return String.valueOf(drawNum);
    }

    @Override
    public void lotteryPay(Lottery lottery, List<PlayerLottery> playerLotteries) {
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
                switch (playerLottery.getNumber()) {
                    case "0":
                        drawNumMap.put(0, drawNumMap.get(0).add(effectiveBet.multiply(new BigDecimal("9"))));
                        break;
                    case "1":
                        drawNumMap.put(1, drawNumMap.get(1).add(effectiveBet.multiply(new BigDecimal("9"))));
                        break;
                    case "2":
                        drawNumMap.put(2, drawNumMap.get(2).add(effectiveBet.multiply(new BigDecimal("9"))));
                        break;
                    case "3":
                        drawNumMap.put(3, drawNumMap.get(3).add(effectiveBet.multiply(new BigDecimal("9"))));
                        break;
                    case "4":
                        drawNumMap.put(4, drawNumMap.get(4).add(effectiveBet.multiply(new BigDecimal("9"))));
                        break;
                    case "5":
                        drawNumMap.put(5, drawNumMap.get(5).add(effectiveBet.multiply(new BigDecimal("9"))));
                        break;
                    case "6":
                        drawNumMap.put(6, drawNumMap.get(6).add(effectiveBet.multiply(new BigDecimal("9"))));
                        break;
                    case "7":
                        drawNumMap.put(7, drawNumMap.get(7).add(effectiveBet.multiply(new BigDecimal("9"))));
                        break;
                    case "8":
                        drawNumMap.put(8, drawNumMap.get(8).add(effectiveBet.multiply(new BigDecimal("9"))));
                        break;
                    case "9":
                        drawNumMap.put(9, drawNumMap.get(9).add(effectiveBet.multiply(new BigDecimal("9"))));
                        break;
                }
            }
        }
        return drawNumMap;
    }

    //根据value排序
    public static List<Integer> sortMapByValues(Map<Integer, BigDecimal> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public Integer getDrawNum(List<Integer> numList, LotteryConfig config, Map<Integer, BigDecimal> drawNumMap, int period) {
        //下注总金额
        BigDecimal sum = BigDecimal.ZERO;
        for (Map.Entry<Integer, BigDecimal> entry : drawNumMap.entrySet()) {
            sum = sum.add(entry.getValue());
        }
        if (sum.compareTo(BigDecimal.ZERO) == 0) {
            return randomDrawNum(numList);
        }

        if (config.getEnabled() == 1 || (sum.intValue() > config.getBalance()) || period % config.getFrequency() != 0) {
            String[] range = config.getDrawRange().split("-");
            numList = numList.subList(Integer.parseInt(range[0]), Integer.parseInt(range[1]));
        }

        return randomDrawNum(numList);
    }

    public Integer randomDrawNum(List<Integer> numList) {
        //没有下注数字
        int drawNum;
        Random random = new Random();
        int nextInt = random.nextInt(numList.size());
        drawNum = numList.get(nextInt);
        return drawNum;
    }
}
