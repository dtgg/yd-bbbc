package com.ydqp.vspoker;

import com.alibaba.fastjson.JSONObject;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.ydqp.common.entity.VsRace;
import com.ydqp.vspoker.dao.VsPokerDao;
import com.ydqp.vspoker.room.play.PlayVsPokerManager;

import java.util.List;
import java.util.TimerTask;

public class GeneratorRaceTask extends TimerTask {
    private static final Logger logger = LoggerFactory.getLogger(GeneratorRaceTask.class);
    private int status;

    public GeneratorRaceTask () {
        status = 0;
    }

    @Override
    public void run() {
        //数据库获取是否要生成赛事
        if (status == 0) {
            status = 1;
            int beginTime = new Long(System.currentTimeMillis() / 1000).intValue();
            List<VsRace> vsRaceList = VsPokerDao.getInstance().getVsRaceByCreateTime(beginTime);
            logger.info("获取赛事信息， {}", JSONObject.toJSONString(vsRaceList));
            for (VsRace vsRace : vsRaceList) {
                PlayVsPokerManager.getInstance().generaPlayObject(vsRace.getRaceType(), vsRace.getBasePoint(), vsRace.getId(),
                        vsRace.getTotalRound());

                //更新状态
                VsPokerDao.getInstance().updateRaceStatus(vsRace.getId(), 1);
            }
        }

        status = 0;
    }
}
