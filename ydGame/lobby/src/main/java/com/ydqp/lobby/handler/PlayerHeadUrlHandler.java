package com.ydqp.lobby.handler;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.receiveProtoMsg.player.PlayerHeadUrl;
import com.ydqp.common.sendProtoMsg.player.PlayerHeadUrlSuccess;
import com.ydqp.lobby.cache.PlayerCache;
import com.ydqp.lobby.constant.HeadUrlConstant;
import com.ydqp.lobby.service.PlayerService;

@ServerHandler(command = 1000023, module = "lobby")
public class PlayerHeadUrlHandler implements IServerHandler {

    private static final Logger logger = LoggerFactory.getLogger(PlayerHeadUrlHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        PlayerHeadUrl headUrl = (PlayerHeadUrl) abstartParaseMessage;

        PlayerHeadUrlSuccess headUrlSuccess = new PlayerHeadUrlSuccess();
        if (!HeadUrlConstant.headUrls.contains(headUrl.getHeadUrl())) {
            logger.error("玩家{}更换头像失败,头像不在官方指定头像内,headUrl:{}", headUrl.getPlayerId(), headUrl.getHeadUrl());
            headUrlSuccess.setSuccess(false);
            headUrlSuccess.setMessage("Avatar is not available");
            iSession.sendMessageByID(headUrlSuccess, headUrl.getConnId());
            return;
        }

        PlayerData playerData = PlayerCache.getInstance().getPlayer(headUrl.getConnId());
        if (playerData == null) {
            logger.error("玩家{}更换头像失败,用户不在线", headUrl.getPlayerId());
            headUrlSuccess.setSuccess(false);
            headUrlSuccess.setMessage("Player is not true");
            iSession.sendMessageByID(headUrlSuccess, headUrl.getConnId());
            return;
        }

        Object[] params = new Object[]{headUrl.getHeadUrl(), playerData.getPlayerId()};
        PlayerService.getInstance().updateHeadUrl(params);

        logger.info("玩家{}更换头像成功,更换前headUrl:{},更换后headUrl:{}", playerData.getPlayerId(), playerData.getHeadUrl(), headUrl.getHeadUrl());

        playerData.setHeadUrl(headUrl.getHeadUrl());
        PlayerCache.getInstance().addPlayer(headUrl.getConnId(), playerData);

        headUrlSuccess.setSuccess(true);
        headUrlSuccess.setMessage("Avatar changed successfully");
        headUrlSuccess.setPlayerId(playerData.getPlayerId());
        headUrlSuccess.setHeadUrl(headUrl.getHeadUrl());
        iSession.sendMessageByID(headUrlSuccess, headUrl.getConnId());
    }
}
