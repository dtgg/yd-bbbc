package com.ydqp.vspoker.room;

import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.ydqp.common.entity.VsRaceConfig;
import com.ydqp.common.poker.Poker;
import com.ydqp.common.poker.room.BattleRole;
import com.ydqp.common.receiveProtoMsg.vspoker.VsPokerXiazhu;
import com.ydqp.vspoker.cache.RankingCache;
import com.ydqp.vspoker.dao.VsRaceConfigDao;
import com.ydqp.vspoker.room.play.PlayVsPokerManager;
import com.ydqp.vspoker.room.play.VsPokerBasePlay;
import org.apache.commons.collections.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class VsPokerBetHandler implements IRoomStatusHandler {

    private static final Logger logger = LoggerFactory.getLogger(VsPokerBetHandler.class);

    @Override
    public void doHandler(VsPokerRoom vsPokerRoom) {
        if (vsPokerRoom.getCurWaitTime() <= 0) {
            //下注15秒时间到
            vsPokerRoom.setStatus(3);
            logger.info("VsPokerBetHandler end");
        } else {
            vsPokerRoom.setCurWaitTime(vsPokerRoom.getCurWaitTime() - 1);

            Map<Integer, Poker> pokerMap = vsPokerRoom.getPokerMap();
            Poker bankPoker = vsPokerRoom.getPokerMap().get(1);
            List<Integer> winPlayTypes = new ArrayList<>();
            for (Map.Entry<Integer, Poker> pokerEntry : pokerMap.entrySet()) {
                if (pokerEntry.getKey() == 1) continue;
                boolean bankWin = isBankWin(bankPoker, pokerEntry.getValue());
                if (!bankWin) winPlayTypes.add(pokerEntry.getKey());
            }

            //虚拟用户下注
            //配置
            Map<Integer, VsRaceConfig> racConfigMap = vsPokerRoom.getRaceConfigMap();
            //房间中的用户排名
            List<Long> rankPlayerList = vsPokerRoom.getRankPlayerIds();

            //虚拟用户排名
            Map<Long, Integer> rankPlayerMap = new HashMap<>();
            //虚拟用户ID
            List<Long> virPlayerIds = new ArrayList<>();
            //虚拟用户battleRole
            Map<Long, BattleRole> virBattleRoleMap = new HashMap<>();
            //真实用户战绩
            List<Integer> realPlayerZjList = new ArrayList<>();
            setData(rankPlayerList, vsPokerRoom.getBattleRoleMap(), rankPlayerMap, virPlayerIds, virBattleRoleMap, realPlayerZjList);

            if (vsPokerRoom.getBattleRoleMap() != null && vsPokerRoom.getBattleRoleMap().size() > 0) {
                //下注
                for (Map.Entry<Long, BattleRole> entry : vsPokerRoom.getBattleRoleMap().entrySet()) {
                    if (entry.getValue().getIsVir() == 0) continue;

                    //虚拟用户下注间隔时间
                    wait(vsPokerRoom.getBattleRoleMap().size());

                    //未获取到排名
                    if (CollectionUtils.isEmpty(rankPlayerList) || racConfigMap == null || racConfigMap.size() == 0) {
                        xiazhu(vsPokerRoom, getPlayType(), entry.getKey(), getPoint(), entry.getValue());
                        continue;
                    }

                    //下注金额
                    int point = 0;
                    int playType = 0;
                    VsRaceConfig vsRaceConfig = racConfigMap.get(vsPokerRoom.getBasePoint());
                    if (vsRaceConfig != null && vsRaceConfig.getFrequency() != 0 && getFrequencyNum() < vsRaceConfig.getFrequency()) {
                        //判断下注
                        for (int i = 0; i < virPlayerIds.size(); i++) {
                            //虚拟用户前两名
                            if (i < 2) {
                                //前两名已下注
                                if (vsPokerRoom.isVirBet()) continue;
                                vsPokerRoom.setVirBet(true);

                                long playerId = virPlayerIds.get(i);
                                Integer rank = rankPlayerMap.get(playerId);
                                //最后一轮
                                if (vsPokerRoom.getTotalRounds() == 10) {
                                    //A、B、C、D全不中
                                    if (CollectionUtils.isEmpty(winPlayTypes)) {
                                        point = 0;
                                        continue;
                                    }
                                    playType = getWinPlayType(winPlayTypes);

                                    //第二名真实用户当前积分
                                    int realPlayerZj = realPlayerZjList.size() >= 2 ? realPlayerZjList.get(1): realPlayerZjList.get(0);
                                    //虚拟用户当前积分
                                    int virPlayerZj = virBattleRoleMap.get(playerId).getPlayerZJ().intValue();
                                    //小于真是用户积分，全下
                                    if (virPlayerZj <= realPlayerZj) {
                                        point = virPlayerZj;
                                    } else if (virPlayerZj < realPlayerZj * 2){
                                        //介于真实用户积分2倍之间
                                        int differencePoint = realPlayerZj * 2 - virPlayerZj;
                                        point = differencePoint + 10;
                                    } else {
                                        //大于2倍，随机下注
                                        point = randomBet(entry.getValue().getPlayerZJ().intValue());
                                    }
                                } else {
                                    //排名前三名随机下注
                                    if (rank < 3) {
                                        point = randomBet(entry.getValue().getPlayerZJ().intValue());
                                        playType = getPlayType();
                                    } else {
                                        //不在前三名，全下
                                        if (CollectionUtils.isEmpty(winPlayTypes)) {
                                            //A、B、C、D全不中
                                            point = 0;
                                            continue;
                                        }
                                        point = entry.getValue().getPlayerZJ().intValue();
                                        playType = getWinPlayType(winPlayTypes);
                                    }
                                }
                            } else {
                                //随机下注
                                point = randomBet(entry.getValue().getPlayerZJ().intValue());
                                playType = getPlayType();
                            }
                        }
                    } else {
                        //随机下注
                        point = randomBet(entry.getValue().getPlayerZJ().intValue());
                        playType = getPlayType();
                    }
                    if (point == 0 || playType == 0) continue;

                    //循环下注
                    cycleXiazhu(vsPokerRoom, playType, entry.getKey(), point, entry.getValue());
                }
            }
        }
    }

    private int randomBet(int playerZj) {
        if (getDivisor() == 0) return 0;  //是否下注
        int point = getPoint();   //随机下注金额
        if (playerZj < point) return 0;
        return point;
    }

    private int getDivisor() {
        return new Random().nextInt(2);
    }

    private void wait(int playerNum) {
        BigDecimal time = BigDecimal.ONE.divide(new BigDecimal(playerNum), 3, RoundingMode.FLOOR);
        BigDecimal l = time.multiply(new BigDecimal(1000));
        int waitTime = 0;
        if (l.compareTo(BigDecimal.ZERO) != 0) {
            waitTime = new Random().nextInt(l.intValue());
        }

        long startTime = System.currentTimeMillis();
        long timestamp = 0;
        while (timestamp < waitTime) {
            timestamp = System.currentTimeMillis() - startTime;
        }
    }

    private int getPlayType() {
        int[] playType = new int[]{1, 2, 3, 4};
        int index = new Random().nextInt(4);
        return playType[index];
    }

    private int getWinPlayType(List<Integer> winPlayerTypes) {
        int index = new Random().nextInt(winPlayerTypes.size());
        return winPlayerTypes.get(index);
    }

    private int getPoint() {
        int[] points = new int[]{10, 50, 200, 1000};
        int index = new Random().nextInt(4);
        return points[index];
    }

    private boolean isBankWin (Poker bankPoker, Poker playerPoker) {
        return bankPoker.getNum() >= playerPoker.getNum();
    }

    private int getFrequencyNum() {
        Random random = new Random();
        return 1 + random.nextInt(10);
    }

    private void setData(List<Long> rankPlayerList, Map<Long, BattleRole> battleRoleMap, Map<Long, Integer> rankPlayerMap,
                         List<Long> virPlayerIds, Map<Long, BattleRole> virBattleRoleMap, List<Integer> realPlayerZjList) {
        if (CollectionUtils.isEmpty(rankPlayerList)) return;
        for (int i = 0; i < rankPlayerList.size(); i++) {
            long playerId = rankPlayerList.get(1);
            boolean isVir = true;
            for (Map.Entry<Long, BattleRole> entry : battleRoleMap.entrySet()) {
                //过滤真实用户
                if (entry.getKey() == playerId) {
                    if (entry.getValue().getIsVir() == 0) {
                        realPlayerZjList.add(entry.getValue().getPlayerZJ().intValue());
                        isVir = false;
                        break;
                    } else {
                        virBattleRoleMap.put(entry.getKey(), entry.getValue());
                    }
                }
            }
            if (!isVir) continue;
            rankPlayerMap.put(playerId, i + 1);
            virPlayerIds.add(playerId);
        }
    }

    private void xiazhu(VsPokerRoom vsPokerRoom, int playType, long playerId, int point, BattleRole battleRole) {
        VsPokerXiazhu vsPokerXiazhu = new VsPokerXiazhu();
        vsPokerXiazhu.setRoomId(vsPokerRoom.getRoomId());
        vsPokerXiazhu.setPlayType(playType);
        vsPokerXiazhu.setPlayerId(playerId);
        vsPokerXiazhu.setMoney(point);
        vsPokerRoom.playerXiazhu(null, battleRole, vsPokerXiazhu);
    }

    private void cycleXiazhu(VsPokerRoom vsPokerRoom, int playType, long playerId, int point, BattleRole battleRole) {
        int thousandNum = point / 1000;
        int hundredNum = (point - thousandNum * 1000) / 200;
        int fiftyNum = (point - thousandNum * 1000 - hundredNum * 100) / 50;
        int tenNum = (point - thousandNum * 1000 - hundredNum * 100 - fiftyNum * 50) / 10;

        if (thousandNum > 0) {
            for (int i = 0; i < thousandNum; i++) {
                xiazhu(vsPokerRoom, playType, playerId, 1000, battleRole);
            }
        }
        if (hundredNum > 0) {
            for (int i = 0; i < hundredNum; i++) {
                xiazhu(vsPokerRoom, playType, playerId, 200, battleRole);
            }
        }
        if (fiftyNum > 0) {
            for (int i = 0; i < fiftyNum; i++) {
                xiazhu(vsPokerRoom, playType, playerId, 50, battleRole);
            }
        }
        if (tenNum > 0) {
            for (int i = 0; i < tenNum; i++) {
                xiazhu(vsPokerRoom, playType, playerId, 10, battleRole);
            }
        }
    }
}
