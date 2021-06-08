package com.ydqp.vspoker.room;

import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.dao.PlayerDao;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.entity.Player;
import com.ydqp.common.entity.VsPlayerRace;
import com.ydqp.common.entity.VsRaceConfig;
import com.ydqp.common.poker.room.BattleRole;
import com.ydqp.vspoker.cache.RankingCache;
import com.ydqp.vspoker.dao.VsPlayerRaceDao;
import com.ydqp.vspoker.dao.VsPokerDao;
import com.ydqp.vspoker.dao.VsRaceConfigDao;
import com.ydqp.vspoker.room.play.PlayVsPokerManager;
import com.ydqp.vspoker.room.play.VsPokerBasePlay;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class VsPokerBeginHandler implements IRoomStatusHandler{

    private static final Logger logger = LoggerFactory.getLogger(VsPokerFaPaiHandler.class);
    @Override
    public void doHandler(VsPokerRoom vsPokerRoom) {
        if(vsPokerRoom.getCurWaitTime() <= 0) {
            //下注60秒时间到
            vsPokerRoom.setStatus(1);

            if (vsPokerRoom.getRoomType() == 3) {
                VsRaceConfig raceConfig = getRaceConfig();
                vsPokerRoom.setHarvest(raceConfig != null && getFrequencyNum() <= raceConfig.getFrequency());
                vsPokerRoom.setVsRaceConfig(raceConfig);

                addVir(vsPokerRoom);
                return;
            }

            //添加机器人
            if (vsPokerRoom.getBattleRoleMap().size() < vsPokerRoom.getMaxPlayerNum()) {
                List<Player> players = PlayerDao.getInstance().getVirPlayer();
                int remainNum = vsPokerRoom.getMaxPlayerNum() - vsPokerRoom.getBattleRoleMap().size();
                Random random = new Random();
                int idx = random.nextInt(players.size() - 10);
                for (int i = 0; i < remainNum; i++) {
                    PlayerData playerData = new PlayerData(players.get(idx));

                    //战绩排名
                    RankingCache.getInstance().addRank(vsPokerRoom.getRaceId(), 0D, playerData.getPlayerId());
                    vsPokerRoom.vsEnterRoom(playerData, null);
                    //vs_player_race
                    VsPlayerRace vsPlayerRace = new VsPlayerRace();
                    vsPlayerRace.setPlayerId(playerData.getPlayerId());
                    vsPlayerRace.setRaceId(vsPokerRoom.getRaceId());
                    vsPlayerRace.setRaceType(2);
                    vsPlayerRace.setBasePoint(vsPokerRoom.getBasePoint());
                    vsPlayerRace.setRank(0);
                    int nowTime = new Long(System.currentTimeMillis() / 1000L).intValue();
                    vsPlayerRace.setCreateTime(nowTime);
                    vsPlayerRace.setAppId(playerData.getAppId());
                    vsPlayerRace.setKfId(playerData.getKfId());
                    vsPlayerRace.setIsVir(playerData.getIsVir());
                    VsPlayerRaceDao.getInstance().insert(vsPlayerRace.getParameterMap());
                    idx++;
                }
                //报名人数加1
                VsPokerDao.getInstance().updateCurPlayerNum(vsPokerRoom.getRaceId(), remainNum);
            }
        } else {
            vsPokerRoom.setCurWaitTime(vsPokerRoom.getCurWaitTime() - 1);
        }
    }

    private void addVir(VsPokerRoom vsPokerRoom) {
        int j = 0;
        int k = 0;
        for (Map.Entry<Long, BattleRole> entry : vsPokerRoom.getBattleRoleMap().entrySet()) {
            if (entry.getValue().getIsVir() == 1) {
                j += 1;
            } else {
                k += 1;
            }
        }
        logger.info("现金场真实用户人数：{}", k);
        if (j >= 6) {
            return;
        }

        List<Player> players = PlayerDao.getInstance().getVirPlayer();
        //10个机器人
        for (int i = 0; i < 6; i++) {
            PlayerData playerData = new PlayerData(players.get(i));

            vsPokerRoom.vsEnterRoom(playerData, null);
        }
    }

    private VsRaceConfig getRaceConfig() {
        return VsRaceConfigDao.getInstance().getZjRaceConfig();
    }

    private int getFrequencyNum() {
        Random random = new Random();
        return 1 + random.nextInt(10);
    }
}
