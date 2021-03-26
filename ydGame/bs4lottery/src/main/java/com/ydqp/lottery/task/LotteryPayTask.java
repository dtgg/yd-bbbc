package com.ydqp.lottery.task;

import com.alibaba.fastjson.JSON;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.dao.lottery.LotteryDao;
import com.ydqp.common.dao.lottery.PlayerLotteryDao;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.entity.Lottery;
import com.ydqp.common.entity.PlayerLottery;
import com.ydqp.common.lottery.player.ILottery;
import com.ydqp.common.lottery.player.ManageLottery;
import com.ydqp.common.lottery.role.LotteryBattleRole;
import com.ydqp.common.sendProtoMsg.CoinPointSuccess;
import com.ydqp.common.sendProtoMsg.lottery.LotteryDrawNotificationSuc;
import com.ydqp.common.sendProtoMsg.lottery.PlayerLotteryInfo;
import com.ydqp.common.service.PlayerService;
import com.ydqp.common.utils.CommonUtils;
import org.apache.commons.collections.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 赔付
 */
public class LotteryPayTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(LotteryPayTask.class);

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();

        //查询已计算的彩票期数
        int createTime = new Long(System.currentTimeMillis() / 1000).intValue() - 179;
        List<Lottery> lotteries = LotteryDao.getInstance().findByStatus(1, createTime);
        if (CollectionUtils.isEmpty(lotteries)) return;

        //彩票对应的下注
        List<Integer> lotteryIds = new ArrayList<>();
        for (Lottery lottery : lotteries) {
            lotteryIds.add(lottery.getId());
        }
        logger.info("查询已计算的彩票期数:{}", JSON.toJSONString(lotteryIds));
        List<PlayerLottery> playerLotteries = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(lotteryIds)) {
            String lotteryIdsStr = CommonUtils.inString(lotteryIds);
            playerLotteries = PlayerLotteryDao.getInstance().findByLotteryIds(lotteryIdsStr);
        }

        //房间中的用户连接ID
        Set<Long> connIds = new HashSet<>();
        if (CollectionUtils.isNotEmpty(playerLotteries)) {
            for (PlayerLottery playerLottery : playerLotteries) {
                LotteryBattleRole lotteryBattleRole = ManageLottery.getLotteryBattleRoleMap().get(playerLottery.getPlayerId());
                if (lotteryBattleRole != null) {
                    connIds.add(lotteryBattleRole.getConnId());
                }
            }
        }

        //批量获取中奖用户缓存数据
        long rdQueryStartTime = System.currentTimeMillis();
        Map<Long, PlayerData> playerDataMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(connIds)) {
            List<PlayerData> playerDataList = PlayerCache.getInstance().getPlayers(new ArrayList<>(connIds));
            if (CollectionUtils.isNotEmpty(playerDataList)) {
                for (PlayerData playerData : playerDataList) {
                    try {
                        playerDataMap.put(playerData.getPlayerId(), playerData);
                    } catch (Exception e) {
                        logger.error("用户缓存数据转map报错，playerData:{}", JSON.toJSONString(playerData));
                        e.printStackTrace();
                    }
                }
            } else {
                logger.info("未获取到用户缓存数据，连接ID：{}，", JSON.toJSONString(connIds));
            }
        }
        long rdQueryEndTime = System.currentTimeMillis();
        if (rdQueryEndTime - rdQueryStartTime > 500) {
            logger.warn("redis查询慢日志， 执行时间：{}", rdQueryEndTime - rdQueryStartTime);
        }

        //更新lottery集合
        List<Object[]> updateLotteryList = new ArrayList<>();
        //更新用户的集合
        Map<Long, BigDecimal> updatePlayerMap = new HashMap<>();
        //更新缓存的集合
        Map<Long, PlayerData> connPlayerDataMap = new HashMap<>();
        //通知金币变化的map
        Map<Long, CoinPointSuccess> coinPointSuccessMap = new HashMap<>();

        //本期下注集合
        Map<Long, Map<Integer, List<PlayerLotteryInfo>>> playerNotificationMap = new HashMap<>();
        for (Lottery lottery : lotteries) {
            Object[] object = new Object[]{2, lottery.getId()};
            updateLotteryList.add(object);

            if (CollectionUtils.isEmpty(playerLotteries)) continue;
            for (PlayerLottery playerLottery : playerLotteries) {
                try {
                    if (playerLottery.getLotteryId() != lottery.getId()) continue;
                    long playerId = playerLottery.getPlayerId();

                    LotteryDrawNotificationSuc suc = new LotteryDrawNotificationSuc();
                    suc.setPlayerId(playerLottery.getPlayerId());
                    suc.setWin(playerLottery.getStatus() == 1);

                    if (playerNotificationMap.get(playerId) == null) {
                        List<PlayerLotteryInfo> list = new ArrayList<>();
                        list.add(new PlayerLotteryInfo(playerLottery));
                        playerNotificationMap.put(playerId, new HashMap<Integer, List<PlayerLotteryInfo>>() {{
                            put(lottery.getType(), list);
                        }});
                    } else {
                        List<PlayerLotteryInfo> playerLotteryInfo = playerNotificationMap.get(playerId).get(lottery.getType());
                        if (playerLotteryInfo == null) {
                            playerNotificationMap.get(playerId).put(lottery.getType(), new ArrayList<PlayerLotteryInfo>() {{
                                add(new PlayerLotteryInfo(playerLottery));
                            }});
                        } else {
                            playerNotificationMap.get(playerId).get(lottery.getType()).add(new PlayerLotteryInfo(playerLottery));
                        }
                    }

                    if (playerLottery.getStatus() == 1) {
                        BigDecimal award = playerLottery.getAward();
                        updatePlayerMap.merge(playerId, award, BigDecimal::add);
                        logger.info("玩家{}第{}期下注中奖,下注ID:{},获得奖励:{},", playerId, playerLottery.getPeriod(), playerLottery.getId(), award);
                        //playerData
                        PlayerData playerData = playerDataMap.get(playerId);
                        //在线
                        if (playerData != null) {
                            if (connPlayerDataMap.get(playerData.getSessionId()) == null) {
                                playerData.setZjPoint(playerData.getZjPoint() + award.doubleValue());
                                connPlayerDataMap.put(playerData.getSessionId(), playerData);
                            } else {
                                PlayerData data = connPlayerDataMap.get(playerData.getSessionId());
                                data.setZjPoint(data.getZjPoint() + award.doubleValue());
                                connPlayerDataMap.put(playerData.getSessionId(), data);
                            }

                            CoinPointSuccess coinPointSuccess = new CoinPointSuccess();
                            coinPointSuccess.setCoinPoint(connPlayerDataMap.get(playerData.getSessionId()).getZjPoint());
                            coinPointSuccess.setPlayerId(playerLottery.getPlayerId());
                            coinPointSuccessMap.put(playerData.getSessionId(), coinPointSuccess);
                        }
                    }
                } catch (Exception e) {
                    logger.error("用户派奖错误：playerLottery:{}", JSON.toJSONString(playerLottery));
                    e.printStackTrace();
                }
            }
        }

        long dbUpdateStartTime = System.currentTimeMillis();
        //更新lottery
        Object[][] params = new Object[updateLotteryList.size()][];
        if (CollectionUtils.isNotEmpty(updateLotteryList)) {
            for (int i = 0; i < updateLotteryList.size(); i++) {
                params[i] = updateLotteryList.get(i);
            }
        }
        if (params.length > 0) {
            LotteryDao.getInstance().batchUpdateStatus(params);
        }

        //更新player
        Object[][] playerParams = new Object[updatePlayerMap.size()][];
        AtomicInteger j = new AtomicInteger();
        updatePlayerMap.forEach((key, value) -> {
            playerParams[j.get()] = new Object[]{value, key};
            j.getAndIncrement();
        });
        if (playerParams.length != 0) {
            PlayerService.getInstance().batchUpdate(playerParams);
        }

        long dbUpdateEndTime = System.currentTimeMillis();
        if (dbUpdateEndTime - dbUpdateStartTime > 500) {
            logger.warn("派奖更新慢日志，执行时间：{}", dbUpdateEndTime - dbUpdateStartTime);
        }

        long rdUpdateStartTime = System.currentTimeMillis();
        //批量更新缓存
        if (connPlayerDataMap.size() != 0) {
            PlayerCache.getInstance().addPlayers(connPlayerDataMap);
        }
        long rdUpdateEndTime = System.currentTimeMillis();
        if (rdUpdateEndTime - rdUpdateStartTime > 500) {
            logger.warn("redis更新慢日志，执行时间：{}", rdUpdateEndTime - rdUpdateStartTime);
        }

        //通知客户端
        ILottery iLottery = ManageLottery.getInstance().getLotteryByRoomIdAndType(5000001, 1);
        ManageLottery.getLotteryBattleRoleMap().forEach((connId, role) -> {
            if (coinPointSuccessMap.get(role.getConnId()) != null) {
                iLottery.sendMessageToBattle(coinPointSuccessMap.get(role.getConnId()), role);
            }
        });

        long endTime = System.currentTimeMillis();
        if (endTime - startTime > 2000) {
            logger.warn("派奖慢日志，执行时间：{}", endTime - startTime);
        }
    }
}
