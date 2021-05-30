package com.ydqp.vspoker.room;

import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.dao.PlayerDao;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.entity.Player;
import com.ydqp.common.entity.VsPlayerRace;
import com.ydqp.vspoker.cache.RankingCache;
import com.ydqp.vspoker.dao.VsPlayerRaceDao;
import com.ydqp.vspoker.dao.VsPokerDao;
import com.ydqp.vspoker.room.play.PlayVsPokerManager;
import com.ydqp.vspoker.room.play.VsPokerBasePlay;

import java.util.List;
import java.util.Random;

public class VsPokerBeginHandler implements IRoomStatusHandler{

    private static final Logger logger = LoggerFactory.getLogger(VsPokerFaPaiHandler.class);
    @Override
    public void doHandler(VsPokerRoom vsPokerRoom) {
        if(vsPokerRoom.getCurWaitTime() <= 0) {
            //下注60秒时间到
            vsPokerRoom.setStatus(1);

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
}
