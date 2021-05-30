package com.ydqp.vspoker;

import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.ydqp.common.entity.SysCloseServer;
import com.ydqp.common.lottery.player.ManageLottery;
import com.ydqp.vspoker.dao.SysCloseServerDao;

public class VsPokerMaintainTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(VsPokerMaintainTask.class);

    @Override
    public void run() {
        //关闭服务器
        if (ManageLottery.getInstance().getCloseServer() == 1) {
            return;
        }
        SysCloseServer sysCloseServer = SysCloseServerDao.getInstance().getSysCloseServer(70, 1);
        if (sysCloseServer != null && sysCloseServer.getStatus() == 1) {
            ManageLottery.getInstance().setCloseServer(1);
        }
    }
}
