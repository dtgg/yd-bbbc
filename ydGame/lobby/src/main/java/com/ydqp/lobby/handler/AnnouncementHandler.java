package com.ydqp.lobby.handler;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.entity.GameAnnouncement;
import com.ydqp.common.sendProtoMsg.SAnnouncement;
import com.ydqp.lobby.dao.GameAnnouncementDao;

@ServerHandler(command = 1000080,module = "lobby")
public class AnnouncementHandler implements IServerHandler {

    private static final Logger logger = LoggerFactory.getLogger(AnnouncementHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        PlayerData playerData = PlayerCache.getInstance().getPlayer(abstartParaseMessage.getConnId());
        if(playerData == null) {
            logger.error("玩家获公共信息失败！");
            return;
        }

        GameAnnouncement gameAnnouncement = GameAnnouncementDao.getInstance().getGameAnnouncement(playerData.getAppId());
        if(gameAnnouncement != null) {
            SAnnouncement sAnnouncement = new SAnnouncement();
            sAnnouncement.setAnnouncement(gameAnnouncement.getAnnouncement());

            iSession.sendMessageByID(sAnnouncement, abstartParaseMessage.getConnId());
        }
    }
}
