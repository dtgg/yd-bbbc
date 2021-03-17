package com.ydqp.lobby.handler.mission;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.dao.PlayerBonusDrawDao;
import com.ydqp.common.dao.lottery.PlayerPromoteDao;
import com.ydqp.common.data.PlayerBonusDrawData;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.entity.PlayerPromote;
import com.ydqp.common.entity.PlayerBonusDraw;
import com.ydqp.common.sendProtoMsg.mission.ParityCollectBonusSuc;
import com.ydqp.lobby.cache.PlayerCache;

import java.util.List;
import java.util.stream.Collectors;

@ServerHandler(command = 1000076, module = "mission")
public class ParityCollectBonusHandler implements IServerHandler {

    private static final Logger logger = LoggerFactory.getLogger(ParityCollectBonusHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        PlayerData playerData = PlayerCache.getInstance().getPlayer(abstartParaseMessage.getConnId());
        if (playerData == null) {
            logger.error("player is not ture");
            iSession.sendMessageByID(new ParityCollectBonusSuc(), abstartParaseMessage.getConnId());
            return;
        }

        PlayerPromote playerPromote = PlayerPromoteDao.getInstance().findByPlayerId(playerData.getPlayerId());

        List<PlayerBonusDraw> bonusDraws = PlayerBonusDrawDao.getInstance().getBonusDrawsByPlayerId(playerData.getPlayerId());
        List<PlayerBonusDrawData> list = bonusDraws.stream().map(PlayerBonusDrawData::new).collect(Collectors.toList());

        ParityCollectBonusSuc suc = new ParityCollectBonusSuc();
        suc.setTotalBonus(playerPromote.getBonusAmount().doubleValue());
        suc.setBonusDrawList(list);
        iSession.sendMessageByID(suc, abstartParaseMessage.getConnId());
    }
}
