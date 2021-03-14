package com.ydqp.lottery.task;

import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.ydqp.common.dao.lottery.LotteryDao;
import com.ydqp.common.entity.Lottery;
import com.ydqp.common.entity.SysCloseServer;
import com.ydqp.common.lottery.player.ManageLottery;
import com.ydqp.common.sendProtoMsg.lottery.LotterySysCloseServer;
import com.ydqp.lottery.dao.SysCloseServerDao;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

public class LotteryMaintainTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(LotteryMaintainTask.class);

    @Override
    public void run() {
        //关闭服务器
        SysCloseServer sysCloseServer = SysCloseServerDao.getInstance().getSysCloseServer(50, 1);
        if (sysCloseServer != null && sysCloseServer.getStatus() == 1) {
            //下一期的开始时间
            List<Lottery> lotteries = LotteryDao.getInstance().findNextLottery("(1)", 1);
            int nextTime = 0;
            if (CollectionUtils.isNotEmpty(lotteries)) {
                nextTime = lotteries.get(0).getCreateTime();
            }
            logger.info("预计关闭服务器时间：{}", nextTime);
            int nowTime = new Long(System.currentTimeMillis() / 1000).intValue();
            if (nextTime > 0 && nowTime >= nextTime) {
                logger.info("关闭服务器");
                LotterySysCloseServer lotterySysCloseServer = new LotterySysCloseServer();
                lotterySysCloseServer.setClose(true);
                ManageLottery.getLotteryBattleRoleMap().forEach((key, value) -> {
                    value.getISession().sendMessageByID(lotterySysCloseServer, value.getConnId());
                });
            }
        }
    }
}
