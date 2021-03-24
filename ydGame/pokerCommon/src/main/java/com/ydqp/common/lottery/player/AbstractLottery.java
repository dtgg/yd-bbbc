package com.ydqp.common.lottery.player;

import com.cfq.connection.ISession;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartCreateMessage;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.dao.PlayerDao;
import com.ydqp.common.dao.lottery.LotteryDao;
import com.ydqp.common.dao.lottery.PlayerLotteryDao;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.entity.Lottery;
import com.ydqp.common.entity.LotteryConfig;
import com.ydqp.common.entity.PlayerLottery;
import com.ydqp.common.lottery.role.LotteryBattleRole;
import com.ydqp.common.sendProtoMsg.lottery.*;
import com.ydqp.common.utils.CommonUtils;
import com.ydqp.common.utils.DateUtil;
import com.ydqp.common.utils.LotteryUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AbstractLottery implements ILottery {

    private static final Logger logger = LoggerFactory.getLogger(AbstractLottery.class);

    @Setter
    @Getter
    public int roomId;

    @Setter
    @Getter
    public int type;

    @Getter
    @Setter
    public Map<Long, LotteryBattleRole> lotteryBattleRoleMap = new ConcurrentHashMap<>();

    @Override
    public void enterLottery(PlayerData playerData, ISession session) {
        logger.info("玩家进入抽奖房间，生成battleRole信息，playerId:{}, roomId:{}", playerData.getPlayerId(), this.getRoomId());
        //获取battleRole
        LotteryBattleRole lotteryBattleRole = ManageLottery.getLotteryBattleRoleMap().get(playerData.getPlayerId());
        if (lotteryBattleRole == null) {
            //进入房间
            lotteryBattleRole = new LotteryBattleRole(playerData, session);
            ManageLottery.getLotteryBattleRoleMap().put(playerData.getPlayerId(), lotteryBattleRole);
        } else {
            //在房间中
            lotteryBattleRole.setISession(session);
            lotteryBattleRole.setConnId(playerData.getSessionId());
            lotteryBattleRole.setPlayerId(playerData.getPlayerId());
            lotteryBattleRole.setPlayerZJ(playerData.getZjPoint());
        }

        //更新数据库、缓存
        playerData.setRoomId(this.getRoomId());
        PlayerDao.getInstance().setUpdatePlayerRoomId(playerData.getPlayerId(), this.getRoomId());
        PlayerCache.getInstance().addPlayer(lotteryBattleRole.getConnId(), playerData);

        //房间信息
        LotteryRoomInfo lotteryRoomInfo = new LotteryRoomInfo();
        lotteryRoomInfo.setRoomId(this.getRoomId());
        LotteryBattleRoleInfo lotteryBattleRoleInfo = new LotteryBattleRoleInfo(lotteryBattleRole);
        lotteryRoomInfo.setLotteryBattleRoleInfo(lotteryBattleRoleInfo);

        List<Integer> roomLotteryTypes = ManageLotteryRoom.getInstance().getType(this.roomId);
        String types = CommonUtils.inString(roomLotteryTypes);
        //不同彩种当前期数
        List<Lottery> newestLottery = LotteryDao.getInstance().findCurrentLottery(types, roomLotteryTypes.size());
        //不同彩种上一期
        List<Lottery> lotteries = LotteryDao.getInstance().findLastLottery(types, roomLotteryTypes.size());
        Map<Integer, Lottery> lastLotteryMap = lotteries.stream().collect(Collectors.toMap(Lottery::getType, Function.identity()));
        //玩家不同彩种购买过的最后一期
        List<PlayerLottery> newestPlayerLottery = PlayerLotteryDao.getInstance().findNewestLottery(playerData.getPlayerId());
        List<LotteryTypeInfo> lotteryTypeInfos = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(newestLottery)) {
            for (Lottery lottery : newestLottery) {
                LotteryTypeInfo lotteryTypeInfo = new LotteryTypeInfo();
                lotteryTypeInfo.setType(lottery.getType());
                lotteryTypeInfo.setLotteryId(lottery.getId());
                lotteryTypeInfo.setPeriod(DateUtil.timestampToStr(lottery.getCreateTime()) + LotteryUtil.intToPeriod(lottery.getPeriod()));
                lotteryTypeInfo.setCreateTime(lottery.getCreateTime());
                lotteryTypeInfo.setDrawNum("");
                lotteryTypeInfo.setDraw(false);

                Lottery lastLottery = lastLotteryMap.get(lottery.getType());
                String period = DateUtil.timestampToStr(lastLottery.getCreateTime()) + LotteryUtil.intToPeriod(lastLottery.getPeriod());
                lotteryTypeInfo.setLotteryInfo(new LotteryInfo(lastLottery, period));
                if (CollectionUtils.isNotEmpty(newestPlayerLottery)) {
                    for (PlayerLottery playerLottery : newestPlayerLottery) {
                        if (playerLottery.getType() == lottery.getType()) {
                            lotteryTypeInfo.setPlayerLotteryInfo(new PlayerLotteryInfo(playerLottery));
                        }
                    }
                }
                lotteryTypeInfos.add(lotteryTypeInfo);
            }
        }
        lotteryRoomInfo.setLotteryTypeInfos(lotteryTypeInfos);

        this.sendMessageToBattle(lotteryRoomInfo, lotteryBattleRole);
        logger.info("玩家进入抽奖房间，playerId:{}", playerData.getPlayerId());
    }

    @Override
    public boolean comebackLottery(PlayerData playerData, ISession iSession) {
        logger.info("玩家回到抽奖房间，生成battleRole信息，playerId:{}, roomId:{}", playerData.getPlayerId(), this.getRoomId());
        LotteryBattleRole lotteryBattleRole = ManageLottery.getLotteryBattleRoleMap().get(playerData.getPlayerId());
        if (lotteryBattleRole != null) {
            lotteryBattleRole.setISession(iSession);
            lotteryBattleRole.setConnId(playerData.getSessionId());
            lotteryBattleRole.setPlayerId(playerData.getPlayerId());
            lotteryBattleRole.setPlayerZJ(playerData.getZjPoint());

            LotteryRoomInfo lotteryRoomInfo = new LotteryRoomInfo();
            lotteryRoomInfo.setRoomId(this.getRoomId());
            LotteryBattleRoleInfo lotteryBattleRoleInfo = new LotteryBattleRoleInfo(lotteryBattleRole);
            lotteryRoomInfo.setLotteryBattleRoleInfo(lotteryBattleRoleInfo);

            int nowTime = new Long(System.currentTimeMillis() / 1000).intValue();
            List<Integer> roomLotteryTypes = ManageLotteryRoom.getInstance().getType(this.roomId);
            String types = CommonUtils.inString(roomLotteryTypes);
            //不同彩种当前期数
            List<Lottery> newestLottery = LotteryDao.getInstance().findCurrentLottery(types, roomLotteryTypes.size());
            //不同彩种上一期
            List<Lottery> lotteries = LotteryDao.getInstance().findLastLottery(types, roomLotteryTypes.size());
            Map<Integer, Lottery> lastLotteryMap = lotteries.stream().collect(Collectors.toMap(Lottery::getType, Function.identity()));
            //玩家不同彩种购买过的最后一期
            List<PlayerLottery> newestPlayerLottery = PlayerLotteryDao.getInstance().findNewestLottery(nowTime);
            List<LotteryTypeInfo> lotteryTypeInfos = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(newestLottery)) {
                for (Lottery lottery : newestLottery) {
                    LotteryTypeInfo lotteryTypeInfo = new LotteryTypeInfo();
                    lotteryTypeInfo.setType(lottery.getType());
                    lotteryTypeInfo.setLotteryId(lottery.getId());
                    lotteryTypeInfo.setPeriod(DateUtil.timestampToStr(lottery.getCreateTime()) + LotteryUtil.intToPeriod(lottery.getPeriod()));
                    lotteryTypeInfo.setCreateTime(lottery.getCreateTime());
                    lotteryTypeInfo.setDrawNum("");
                    lotteryTypeInfo.setDraw(false);

                    Lottery lastLottery = lastLotteryMap.get(lottery.getType());
                    String period = DateUtil.timestampToStr(lastLottery.getCreateTime()) + LotteryUtil.intToPeriod(lottery.getPeriod());
                    lotteryTypeInfo.setLotteryInfo(new LotteryInfo(lastLottery, period));
                    if (CollectionUtils.isNotEmpty(newestPlayerLottery)) {
                        for (PlayerLottery playerLottery : newestPlayerLottery) {
                            if (playerLottery.getType() == lottery.getType()) {
                                lotteryTypeInfo.setPlayerLotteryInfo(new PlayerLotteryInfo(playerLottery));
                            }
                        }
                    }
                    lotteryTypeInfos.add(lotteryTypeInfo);
                }
            }
            lotteryRoomInfo.setLotteryTypeInfos(lotteryTypeInfos);

            this.sendMessageToBattle(lotteryRoomInfo, lotteryBattleRole);
            return true;
        }
        return false;
    }

//    @Override
//    public LotteryBattleRole getLotteryBattleRole(long playerId) {
//        return this.getLotteryBattleRoleMap().get(playerId);
//    }
//
//    @Override
//    public void removeLotteryBattleRole(long playerId) {
//        this.getLotteryBattleRoleMap().remove(playerId);
//    }

    @Override
    public void sendMessageToBattle(AbstartCreateMessage abstartCreateMessage, LotteryBattleRole lotteryBattleRole) {
        ISession iSession = lotteryBattleRole.getISession();
        iSession.sendMessageByID(abstartCreateMessage, lotteryBattleRole.getConnId());
    }

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

    //根据value排序
    public static List<Integer> sortMapByValues(Map<Integer, BigDecimal> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public Integer getDrawNum(List<Integer> numList, LotteryConfig config, BigDecimal sum, int period) {
        if (sum.compareTo(BigDecimal.ZERO) == 0) {
            return randomDrawNum(numList);
        }

        Random random = new Random();
        int nextInt = random.nextInt(10);

        String[] range = config.getDrawRange().split("-");
        if (config.getEnabled() == 1) {
            logger.info("enabled开启");
            numList = numList.subList(Integer.parseInt(range[0]), Integer.parseInt(range[1]));
        } else if (sum.intValue() > config.getBalance()) {
            logger.info("达到指定阈值");
            numList = numList.subList(Integer.parseInt(range[0]), Integer.parseInt(range[1]));
        } else if (nextInt < config.getProbability()) {
            logger.info("达到指定概率");
            numList = numList.subList(Integer.parseInt(range[0]), Integer.parseInt(range[1]));
        } else if (period % config.getFrequency() == 0) {
            logger.info("达到指定频率");
            numList = numList.subList(Integer.parseInt(range[0]), Integer.parseInt(range[1]));
        }

        logger.info("随机开奖, 期号：{}", period);
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
