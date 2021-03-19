package com.ydqp.lobby.handler;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.entity.SysCloseServer;
import com.ydqp.common.receiveProtoMsg.CheckEnterRoom;
import com.ydqp.common.sendProtoMsg.CheckEnterRoomSuc;
import com.ydqp.lobby.dao.SysCloseServerDao;

@ServerHandler(command = 1000800,module = "lobby")
public class EnterRoomCheckHandler implements IServerHandler {

    private static final Logger logger = LoggerFactory.getLogger(EnterRoomCheckHandler.class);
    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        CheckEnterRoom checkEnterRoom = (CheckEnterRoom) abstartParaseMessage;
        PlayerData playerData = PlayerCache.getInstance().getPlayer(checkEnterRoom.getConnId());
        if (playerData == null) {
            logger.error("player is not true");
            return;
        }
        //先判断下服务器是否维护状态
        CheckEnterRoomSuc checkEnterRoomSuc = new CheckEnterRoomSuc();

        //@TODO 暂时写死，只有50
        SysCloseServer sysCloseServer = SysCloseServerDao.getInstance().getSysCloseServer(50, 1);
        if (sysCloseServer != null) {
            if (sysCloseServer.getCloseServer() == 1) {
                //服务器维护状态
                checkEnterRoomSuc.setCode(400);
                iSession.sendMessageByID(checkEnterRoomSuc, abstartParaseMessage.getConnId());
                return;
            }
        }

        checkEnterRoomSuc.setCode(200);
        iSession.sendMessageByID(checkEnterRoomSuc, abstartParaseMessage.getConnId());
    }
}
