package com.ydqp.lobby.handler;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.entity.GameVersion;
import com.ydqp.common.sendProtoMsg.SGameVersion;
import com.ydqp.lobby.dao.GetVersionDao;

@ServerHandler(command = 1000060,module = "lobby")
public class GameVersionHandler implements IServerHandler {

    private static final Logger logger = LoggerFactory.getLogger(GameVersionHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {

        PlayerData playerData = PlayerCache.getInstance().getPlayer(abstartParaseMessage.getConnId());
        if(playerData == null) {
            logger.error("玩家获取更新版本信息失败！");
            return;
        }

        GameVersion gameVersion = GetVersionDao.getInstance().getGameVersion(playerData.getAppId());
        if(gameVersion != null) {
            SGameVersion sGameVersion = new SGameVersion();
            sGameVersion.setApkUrl(gameVersion.getApkUrl());
            sGameVersion.setApkVersion(gameVersion.getApkVersion());
            sGameVersion.setJsVersion(gameVersion.getJsVersion());
            sGameVersion.setMandatoryApk(gameVersion.getApkMandatory() == 1);

            iSession.sendMessageByID(sGameVersion, abstartParaseMessage.getConnId());
        }

    }
}
