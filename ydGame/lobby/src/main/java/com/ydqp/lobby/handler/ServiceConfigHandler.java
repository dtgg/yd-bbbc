package com.ydqp.lobby.handler;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.entity.ServiceConfig;
import com.ydqp.common.sendProtoMsg.SServiceConfig;
import com.ydqp.lobby.dao.ServiceConfigDao;

@ServerHandler(command = 1000082,module = "lobby")
public class ServiceConfigHandler implements IServerHandler {

    private static final Logger logger = LoggerFactory.getLogger(ServiceConfigHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        PlayerData playerData = PlayerCache.getInstance().getPlayer(abstartParaseMessage.getConnId());
        if(playerData == null) {
            logger.error("玩家获客服信息失败！");
            return;
        }

        ServiceConfig serviceConfig = ServiceConfigDao.getInstance().getServiceConfig(playerData.getAppId());
        if(serviceConfig != null) {
            SServiceConfig sServiceConfig = new SServiceConfig();
            sServiceConfig.setTelegram(serviceConfig.getTelegram());
            sServiceConfig.setWhatsapp(serviceConfig.getWhatsapp());

            iSession.sendMessageByID(sServiceConfig, abstartParaseMessage.getConnId());
        }
    }
}
