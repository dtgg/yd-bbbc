package com.ydqp.vspoker.handler;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.dao.VsRacePromoteDao;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.data.Total;
import com.ydqp.common.receiveProtoMsg.vspoker.VsPokerRebateChange;
import com.ydqp.common.sendProtoMsg.vspoker.SVsPokerRebateChange;

@ServerHandler(module = "vsPoker", command = 7000023)
public class VsPokerRebateChangeHandler implements IServerHandler {

    private static final Logger logger = LoggerFactory.getLogger(VsPokerRebateChangeHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        VsPokerRebateChange rebateChange = (VsPokerRebateChange) abstartParaseMessage;

        PlayerData playerData = PlayerCache.getInstance().getPlayer(rebateChange.getConnId());
        if (playerData == null) {
            logger.error("player is not true");
            return;
        }

        Total rebateAmount = VsRacePromoteDao.getInstance().sumRebateAmount(rebateChange.getPlayerId());

        SVsPokerRebateChange sVsPokerRebateChange = new SVsPokerRebateChange();
        sVsPokerRebateChange.setPlayerId(rebateChange.getPlayerId());
        sVsPokerRebateChange.setRebate((float) rebateAmount.getSum());
        iSession.sendMessageByID(sVsPokerRebateChange, rebateChange.getConnId());
    }
}
