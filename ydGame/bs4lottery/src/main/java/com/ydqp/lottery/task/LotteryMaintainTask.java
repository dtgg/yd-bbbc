package com.ydqp.lottery.task;

import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.ydqp.common.dao.lottery.LotteryDao;
import com.ydqp.common.entity.Lottery;
import com.ydqp.common.entity.SysCloseServer;
import com.ydqp.common.lottery.player.ManageLottery;
import com.ydqp.common.sendProtoMsg.lottery.LotterySysCloseServer;
import com.ydqp.lottery.Cache.LotteryCache;
import com.ydqp.lottery.dao.SysCloseServerDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;

public class LotteryMaintainTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(LotteryMaintainTask.class);

    @Override
    public void run() {
        //关闭服务器
        if (ManageLottery.getInstance().getCloseServer() == 1) {
            return;
        }
        SysCloseServer sysCloseServer = SysCloseServerDao.getInstance().getSysCloseServer(50, 1);
        if (sysCloseServer != null && sysCloseServer.getStatus() == 1) {
            String maintainTimeStr = LotteryCache.getInstance().getMaintainTime();
            int maintainTime = 0;
            if (StringUtils.isBlank(maintainTimeStr)) {
                //下一期
                List<Lottery> lotteries = LotteryDao.getInstance().findNextLottery("(1)", 1);
                if (CollectionUtils.isNotEmpty(lotteries)) {
                    maintainTime = lotteries.get(0).getCreateTime();
                    LotteryCache.getInstance().setMaintainTime(String.valueOf(maintainTime));
                }
            } else {
                maintainTime = Integer.parseInt(maintainTimeStr);
            }
            logger.info("预计关闭服务器时间：{}", maintainTime);
            int nowTime = new Long(System.currentTimeMillis() / 1000).intValue();
            if (maintainTime > 0 && nowTime >= maintainTime) {
                logger.info("关闭服务器");
                LotterySysCloseServer lotterySysCloseServer = new LotterySysCloseServer();
                lotterySysCloseServer.setClose(true);
                ManageLottery.getLotteryBattleRoleMap().forEach((key, value) -> {
                    value.getISession().sendMessageByID(lotterySysCloseServer, value.getConnId());
                });
            }

            ManageLottery.getInstance().setCloseServer(1);
        }
    }
}
