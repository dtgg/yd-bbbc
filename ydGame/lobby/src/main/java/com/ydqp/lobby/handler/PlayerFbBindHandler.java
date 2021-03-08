package com.ydqp.lobby.handler;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.entity.Player;
import com.ydqp.common.receiveProtoMsg.player.PlayerFbBind;
import com.ydqp.common.sendProtoMsg.player.LobbyError;
import com.ydqp.common.sendProtoMsg.player.PlayerFbBindSuccess;
import com.ydqp.common.utils.FbHelper;
import com.ydqp.lobby.cache.PlayerCache;
import com.ydqp.lobby.dao.PlayerLoginDao;

@ServerHandler(command = 1000021, module = "lobby")
public class PlayerFbBindHandler implements IServerHandler {

    private static final Logger logger = LoggerFactory.getLogger(PlayerFbBindHandler.class);
    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        PlayerFbBind fbBind = (PlayerFbBind) abstartParaseMessage;

        PlayerData playerData = PlayerCache.getInstance().getPlayer(fbBind.getConnId());
        if(playerData == null) {
            logger.error("游客绑定id，玩家不在线! fbUserId={}",fbBind.getFbUserId());
            return;
        }
        logger.info("游客进行FB绑定账号，playerId = {}  fbUserId={}",playerData.getPlayerId(),fbBind.getFbUserId());
        PlayerFbBindSuccess playerFbBindSuccess = new PlayerFbBindSuccess();
        Player playerB = PlayerLoginDao.getInstance().selectPlayerById(playerData.getPlayerId());
        if (playerB.getFbBind() == 1) {
            //游客账号已经绑定过
            logger.info("游客进行FB绑定账号，游客账号已经绑定过了,playerId = {}  fbUserId={}",playerData.getPlayerId(),fbBind.getFbUserId());
            playerFbBindSuccess.setStatus(3);
            playerFbBindSuccess.setPlayerId(playerData.getPlayerId());
            iSession.sendMessageByID(playerFbBindSuccess, fbBind.getConnId());
            return;
        }

        Player player = PlayerLoginDao.getInstance().selectPlayerByFb(fbBind.getFbUserId());
        if(player != null) {
            //fb账号已经绑定过了
            logger.info("游客进行FB绑定账号，FB账号已经绑定过了,playerId = {}  fbUserId={}",playerData.getPlayerId(),fbBind.getFbUserId());
            playerFbBindSuccess.setStatus(2);
            playerFbBindSuccess.setPlayerId(playerData.getPlayerId());
            iSession.sendMessageByID(playerFbBindSuccess, fbBind.getConnId());
            return;
        }
        //去fb验证，验证成功，保存下来
        String userId = FbHelper.getUserAcc(fbBind.getAccessToken());
        if(userId == null) {
            logger.error("玩家fb绑定失败!! playerID = {}", playerData.getPlayerId());
            playerFbBindSuccess.setStatus(1);
            playerFbBindSuccess.setPlayerId(playerData.getPlayerId());
            iSession.sendMessageByID(playerFbBindSuccess, fbBind.getConnId());
            return;
        }

        PlayerLoginDao.getInstance().bindFb(playerData.getPlayerId(), fbBind.getFbUserId(), fbBind.getFbNickName());
        playerFbBindSuccess.setStatus(0);
        playerFbBindSuccess.setPlayerId(playerData.getPlayerId());
        iSession.sendMessageByID(playerFbBindSuccess, fbBind.getConnId());

        logger.info("游客进行FB绑定账号，绑定成功,playerId = {}  fbUserId={}",playerData.getPlayerId(),fbBind.getFbUserId());
    }
}
