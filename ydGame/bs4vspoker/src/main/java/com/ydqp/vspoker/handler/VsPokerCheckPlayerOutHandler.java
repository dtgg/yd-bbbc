package com.ydqp.vspoker.handler;

import com.alibaba.fastjson.JSONObject;
import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.entity.VsRace;
import com.ydqp.common.receiveProtoMsg.vspoker.VsPokerCheckPlayerOut;
import com.ydqp.common.sendProtoMsg.vspoker.SVsPokerCheckPlayerOut;
import com.ydqp.vspoker.dao.VsPokerDao;
import com.ydqp.vspoker.room.play.PlayVsPokerManager;
import com.ydqp.vspoker.room.play.VsPokerBasePlay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ServerHandler(module = "vsPoker", command = 7000017)
public class VsPokerCheckPlayerOutHandler implements IServerHandler {
    private static final Logger logger = LoggerFactory.getLogger(VsPokerCheckPlayerOutHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        VsPokerCheckPlayerOut vsPokerCheckPlayerOut = (VsPokerCheckPlayerOut) abstartParaseMessage;

        SVsPokerCheckPlayerOut sVsPokerCheckPlayerOut = new SVsPokerCheckPlayerOut();

        PlayerData playerData = PlayerCache.getInstance().getPlayer(vsPokerCheckPlayerOut.getConnId());
        if (playerData == null) {
            logger.error("player is not true");

            sVsPokerCheckPlayerOut.setEnterEnabled(false);
            sVsPokerCheckPlayerOut.setStatus(4);
            iSession.sendMessageByID(sVsPokerCheckPlayerOut, vsPokerCheckPlayerOut.getConnId());
            return;
        }

        VsPokerBasePlay vsPokerBasePlay = PlayVsPokerManager.getInstance().getPlayObject(vsPokerCheckPlayerOut.getRoomType(),
                1, vsPokerCheckPlayerOut.getRaceId());
        if (vsPokerBasePlay == null) {
            logger.error("无对应的玩法类型，{} ", JSONObject.toJSONString(vsPokerCheckPlayerOut));

            sVsPokerCheckPlayerOut.setEnterEnabled(false);
            sVsPokerCheckPlayerOut.setStatus(4);
            iSession.sendMessageByID(sVsPokerCheckPlayerOut, vsPokerCheckPlayerOut.getConnId());
            return;
        }

        VsRace race = VsPokerDao.getInstance().getRaceById(vsPokerCheckPlayerOut.getRaceId());
        if (race.getStatus() == 0) {
            sVsPokerCheckPlayerOut.setEnterEnabled(false);
            sVsPokerCheckPlayerOut.setStatus(1);
        }
        if (race.getStatus() == 2) {
            sVsPokerCheckPlayerOut.setEnterEnabled(false);
            sVsPokerCheckPlayerOut.setStatus(3);
        }

        boolean b = vsPokerBasePlay.checkPlayerOut(playerData);
        if (!b) {
            logger.error("玩家已被淘汰，playerId:{} ", playerData.getPlayerId());
            sVsPokerCheckPlayerOut.setStatus(2);
        }
        sVsPokerCheckPlayerOut.setEnterEnabled(b);
        iSession.sendMessageByID(sVsPokerCheckPlayerOut, vsPokerCheckPlayerOut.getConnId());
    }
}
