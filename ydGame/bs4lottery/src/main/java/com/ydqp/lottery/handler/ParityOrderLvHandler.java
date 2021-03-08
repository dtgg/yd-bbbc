package com.ydqp.lottery.handler;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.dao.lottery.PlayerPromoteDao;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.data.Total;
import com.ydqp.common.entity.PlayerPromote;
import com.ydqp.common.receiveProtoMsg.lottery.ParityOrderLv;
import com.ydqp.common.sendProtoMsg.lottery.ParityOrderDetailInfo;
import com.ydqp.common.sendProtoMsg.lottery.ParityOrderLvSuc;

import java.util.ArrayList;
import java.util.List;

@ServerHandler(module = "lottery", command = 5000013)
public class ParityOrderLvHandler implements IServerHandler {
    private static final Logger logger = LoggerFactory.getLogger(ParityOrderLvHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        ParityOrderLv parityOrderLv = (ParityOrderLv) abstartParaseMessage;

        PlayerData playerData = PlayerCache.getInstance().getPlayer(parityOrderLv.getConnId());
        if (playerData == null) {
            logger.error("player is not true");
            return;
        }

        int offset = (parityOrderLv.getPage() - 1) * parityOrderLv.getSize();
        List<PlayerPromote> promoteList = PlayerPromoteDao.getInstance().findChildren(
                playerData.getPlayerId(), parityOrderLv.getLv(), offset, parityOrderLv.getSize());
        Total total = PlayerPromoteDao.getInstance().count(playerData.getPlayerId(), parityOrderLv.getLv());

        List<ParityOrderDetailInfo> list = new ArrayList<>();
        for (PlayerPromote playerPromote : promoteList) {
            ParityOrderDetailInfo info = new ParityOrderDetailInfo();
            info.setPlayerId(playerPromote.getPlayerId());
            info.setNickname(playerPromote.getNickname());

            if (playerPromote.getSuperiorId() == playerData.getPlayerId()) {
                info.setContribute(playerPromote.getSuperiorAmount() == null ? 0 : playerPromote.getSuperiorAmount().doubleValue());
            } else {
                info.setContribute(playerPromote.getGrandAmount() == null ? 0 : playerPromote.getGrandAmount().doubleValue());
            }

            list.add(info);
        }

        ParityOrderLvSuc suc = new ParityOrderLvSuc();
        suc.setInfos(list);
        suc.setPlayerId(playerData.getPlayerId());
        suc.setCount(total.getTotal());
        iSession.sendMessageByID(suc, parityOrderLv.getConnId());
    }
}
