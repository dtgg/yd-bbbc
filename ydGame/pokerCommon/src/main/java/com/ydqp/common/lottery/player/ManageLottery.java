package com.ydqp.common.lottery.player;

import com.ydqp.common.dao.lottery.LotteryConfigDao;
import com.ydqp.common.entity.LotteryConfig;
import com.ydqp.common.lottery.role.LotteryBattleRole;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ManageLottery {

    private ManageLottery() {}

    private static ManageLottery instance;

    public static ManageLottery getInstance() {
        if (instance == null)
            instance = new ManageLottery();
        return instance;
    }

    static {
        //从数据库中加载配置
        registerLottery();
    }

    private static Map<Integer, ILottery> lotteryMap;

    private static Map<Integer, Map<Integer, ILottery>> roomLotteryMap;

    private static final Map<Long, LotteryBattleRole> lotteryBattleRoleMap = new ConcurrentHashMap<>();

    public static void registerLottery() {
        List<LotteryConfig> all = LotteryConfigDao.getInstance().findAll();

        lotteryMap = new HashMap<>();
        for (LotteryConfig lotteryConfig : all) {
            RBLottery lottery = new RBLottery();
            lottery.setRoomId(5000001);
            lotteryMap.put(lotteryConfig.getLotteryType(), lottery);
        }
        roomLotteryMap = new HashMap<>();
        roomLotteryMap.put(5000001, lotteryMap);
    }

    public ILottery getLotteryByRoomIdAndType(int roomId, int type) {
        if (roomLotteryMap == null || roomLotteryMap.get(roomId) == null) return null;
        return roomLotteryMap.get(roomId).get(type);
    }

    public ILottery getLotteryByType(int type) {
        return lotteryMap.get(type);
    }

    public LotteryConfig getConfig(int type) {
        List<LotteryConfig> all = LotteryConfigDao.getInstance().findAll();
        LotteryConfig lotteryConfig = null;
        for (LotteryConfig config : all) {
            if (type == config.getLotteryType()) {
                lotteryConfig = config;
                break;
            }
        }
        return lotteryConfig;
    }

    public static Map<Long, LotteryBattleRole> getLotteryBattleRoleMap() {
        return lotteryBattleRoleMap;
    }

    public void removeLotteryBattleRole(long playerId) {
        lotteryBattleRoleMap.remove(playerId);
    }
}
