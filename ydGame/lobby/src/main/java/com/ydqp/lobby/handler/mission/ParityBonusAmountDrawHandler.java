package com.ydqp.lobby.handler.mission;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.dao.PlayerBonusDrawDao;
import com.ydqp.common.dao.lottery.PlayerPromoteDao;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.entity.PlayerBonusDraw;
import com.ydqp.common.entity.PlayerPromote;
import com.ydqp.common.receiveProtoMsg.mission.ParityBonusAmountDraw;
import com.ydqp.common.sendProtoMsg.mission.ParityBonusAmountDrawSuc;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

@ServerHandler(module = "mission", command = 1000075)
public class ParityBonusAmountDrawHandler implements IServerHandler {

    private static final Logger logger = LoggerFactory.getLogger(ParityBonusAmountDrawHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        ParityBonusAmountDraw bonusAmountDraw = (ParityBonusAmountDraw) abstartParaseMessage;
        ParityBonusAmountDrawSuc suc = new ParityBonusAmountDrawSuc();
        PlayerData playerData = PlayerCache.getInstance().getPlayer(abstartParaseMessage.getConnId());
        if (playerData == null) {
            logger.error("player is not ture");
            suc.setSuccess(false);
            suc.setMessage("player is not ture");
            iSession.sendMessageByID(suc, abstartParaseMessage.getConnId());
            return;
        }

        PlayerPromote playerPromote = PlayerPromoteDao.getInstance().findByPlayerId(playerData.getPlayerId());
        if (playerPromote == null) {
            logger.error("playerPromote is not ture");
            suc.setSuccess(false);
            suc.setMessage("Account exception");
            iSession.sendMessageByID(suc, abstartParaseMessage.getConnId());
            return;
        }

        if (playerPromote.getBonusAmount().doubleValue() < bonusAmountDraw.getBonusAmount()) {
            logger.error("playerPromote bonusAmount is not enough");
            suc.setSuccess(false);
            suc.setMessage("Insufficient balance");
            iSession.sendMessageByID(suc, abstartParaseMessage.getConnId());
            return;
        }
        Object[] params = new Object[]{0 - bonusAmountDraw.getBonusAmount(), playerData.getPlayerId()};
        int row = PlayerPromoteDao.getInstance().updateBonusAmount(params);
        if (row == 0) {
            logger.error("playerPromote bonusAmount is not enough");
            suc.setSuccess(false);
            suc.setMessage("Insufficient balance");
            iSession.sendMessageByID(suc, abstartParaseMessage.getConnId());
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmsssss");
        PlayerBonusDraw playerBonusDraw = new PlayerBonusDraw();
        playerBonusDraw.setPlayerId(playerData.getPlayerId());
        playerBonusDraw.setOrderId(sdf.format(new Date()));
        playerBonusDraw.setAmount(BigDecimal.valueOf(bonusAmountDraw.getBonusAmount()));
        playerBonusDraw.setStatus(0);
        playerBonusDraw.setCreateTime(new Long(System.currentTimeMillis() / 1000L).intValue());
        PlayerBonusDrawDao.getInstance().insert(playerBonusDraw.getParameterMap());

        suc.setSuccess(true);
        suc.setMessage("Submitted successfully, waiting for review");
        iSession.sendMessageByID(suc, abstartParaseMessage.getConnId());
    }
}
