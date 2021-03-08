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
import com.ydqp.common.entity.PlayerLottery;
import com.ydqp.common.lottery.role.LotteryBattleRole;
import com.ydqp.common.sendProtoMsg.lottery.*;
import com.ydqp.common.utils.DateUtil;
import com.ydqp.common.utils.LotteryUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AbstractLottery implements ILottery {

    private static final Logger logger = LoggerFactory.getLogger(AbstractLottery.class);

    @Setter
    @Getter
    public int roomId;

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

        //不同彩种当前期数
        List<Lottery> newestLottery = LotteryDao.getInstance().findCurrentLottery();
        //不同彩种上一期
        List<Lottery> lotteries = LotteryDao.getInstance().findLastLottery();
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
                lotteryTypeInfo.setDrawNum(lottery.getNumber());
                lotteryTypeInfo.setDraw(StringUtils.isNotBlank(lottery.getNumber()));

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
            //不同彩种当前期数
            List<Lottery> newestLottery = LotteryDao.getInstance().findCurrentLottery();
            //不同彩种上一期
            List<Lottery> lotteries = LotteryDao.getInstance().findLastLottery();
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
                    lotteryTypeInfo.setDrawNum(lottery.getNumber());
                    lotteryTypeInfo.setDraw(StringUtils.isNotBlank(lottery.getNumber()));

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
}
