package com.ydqp.lobby.handler;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.receiveProtoMsg.player.PlayerLogout;
import com.ydqp.lobby.cache.PlayerCache;
import com.ydqp.lobby.dao.PlayerLoginDao;

@ServerHandler(command = 1000002, module = "playerLogin")
public class PlayerLogoutHandler implements IServerHandler {
    private final static Logger logger = LoggerFactory.getLogger(PlayerLogoutHandler.class);
    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        PlayerLogout playerDownLine = (PlayerLogout) abstartParaseMessage;

        PlayerData playerData = PlayerCache.getInstance().getPlayer(playerDownLine.getConnId());
        if (playerData != null) {
            logger.info("玩家下线, playerId = {}, socketID = {}" , playerData.getPlayerId(), playerDownLine.getConnId());
            PlayerLoginDao.getInstance().updateOnLineTime(playerData.getPlayerId(), (int)(System.currentTimeMillis()/1000));
            //PlayerCache.getInstance().delPlayerByPlayerId(playerData.getPlayerId());
        }
        PlayerCache.getInstance().delPlayerByConnId(playerDownLine.getConnId());


    }
}
