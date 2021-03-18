package com.ydqp.lobby.handler.mission;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.ManagePlayerPromote;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.dao.lottery.PlayerPromoteDao;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.data.Total;
import com.ydqp.common.entity.PlayerPromote;
import com.ydqp.common.entity.PlayerPromoteConfig;
import com.ydqp.common.receiveProtoMsg.mission.ParityOrder;
import com.ydqp.common.sendProtoMsg.mission.ParityOrderSuc;
import com.ydqp.common.utils.ShortCodeKit;

@ServerHandler(module = "mission", command = 1000070)
public class ParityOrderHandler implements IServerHandler {
    private static final Logger logger = LoggerFactory.getLogger(ParityOrderHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        ParityOrder parityOrder = (ParityOrder) abstartParaseMessage;

        PlayerData playerData = PlayerCache.getInstance().getPlayer(parityOrder.getConnId());
        if (playerData == null) {
            logger.error("player is not true");
            iSession.sendMessageByID(new ParityOrderSuc(), parityOrder.getConnId());
            return;
        }

        PlayerPromote playerPromote = PlayerPromoteDao.getInstance().findByPlayerId(playerData.getPlayerId());
        if (playerPromote == null) {
            logger.error("playerPromote is not true");
            iSession.sendMessageByID(new ParityOrderSuc(), parityOrder.getConnId());
            return;
        }

        ParityOrderSuc suc = new ParityOrderSuc();
        suc.setPlayerId(playerData.getPlayerId());
        suc.setEffectiveNum(playerPromote.getSubNum());
        suc.setEffectiveNumLv1(playerPromote.getSubNum());
        suc.setEffectiveNumLv2(playerPromote.getSonNum());
        //referralCode
        String referralCode = ShortCodeKit.convertDecimalToBase62(ShortCodeKit.permutedId(playerData.getPlayerId()), 8);
        suc.setReferralCode(referralCode);
        //referralLink
        PlayerPromoteConfig config = ManagePlayerPromote.getInstance().getConfig();
        suc.setReferralLink(config.getReferralLink() + referralCode);
        //bonus
        Total totalLv1 = PlayerPromoteDao.getInstance().sumBonus(playerData.getPlayerId(), 1);
        Total totalLv2 = PlayerPromoteDao.getInstance().sumBonus(playerData.getPlayerId(), 2);
        suc.setBonusLv1(totalLv1.getSum());
        suc.setBonusLv2(totalLv2.getSum());
        suc.setPlayerZJ(totalLv1.getSum() + totalLv2.getSum());

        iSession.sendMessageByID(suc, parityOrder.getConnId());
    }
}
