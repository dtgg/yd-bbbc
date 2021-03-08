package com.ydqp.lobby.handler;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.constant.Constant;
import com.ydqp.common.receiveProtoMsg.player.PlayerFbLogin;
import com.ydqp.common.sendProtoMsg.player.LobbyError;
import com.ydqp.common.utils.FbHelper;
import com.ydqp.lobby.cache.PlayerCache;
import com.ydqp.lobby.service.PlayerService;

@ServerHandler(command = 1000020, module = "playerLogin")
public class PlayerFbLoginHandler implements IServerHandler {
    private static final Logger logger = LoggerFactory.getLogger(PlayerFbLoginHandler.class);
    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        PlayerFbLogin fbLogin = (PlayerFbLogin)abstartParaseMessage;
        if (fbLogin != null) {
            logger.info("玩家进行fb登陆. fbUserId={} ", fbLogin.getFbUserId());
            String userId = null;
            String accessToken = PlayerCache.getInstance().getPlayerFbToken(fbLogin.getFbUserId());
            if (accessToken == null || accessToken.isEmpty()) {
                //去fb验证，验证成功，保存下来
                logger.info("玩家进行fb登陆，需要去fb验证. fbUserId={} starttime={}", fbLogin.getFbUserId(),
                        System.currentTimeMillis());
                userId = FbHelper.getUserAcc(fbLogin.getAccessToken());
                logger.info("玩家进行fb登陆，需要去fb验证结束. fbUserId={} endtime={}", fbLogin.getFbUserId(),
                        System.currentTimeMillis());
                if(userId == null) {
                    LobbyError lobbyError = new LobbyError();
                    lobbyError.setErrorCode(406);
                    lobbyError.setErrorMsg("fb login error");
                    iSession.sendMessage(lobbyError, fbLogin);
                    return;
                }

                PlayerCache.getInstance().addPlayerFbToken(userId, fbLogin.getAccessToken());

                PlayerService.getInstance().fbLogin(iSession, fbLogin);

            } else {
                if(!accessToken.equals(fbLogin.getAccessToken())) {
                    logger.info("玩家进行fb登陆,token错误，重新验证，需要去fb验证. fbUserId={} starttime={}", fbLogin.getFbUserId(),
                            System.currentTimeMillis());
                    userId = FbHelper.getUserAcc(fbLogin.getAccessToken());
                    logger.info("玩家进行fb登陆，token错误，重新验证，需要去fb验证结束. fbUserId={} endtime={}", fbLogin.getFbUserId(),
                            System.currentTimeMillis());
                    if (userId == null) {
                        logger.info("玩家accesstoken 错误，fbuserId = {} ", fbLogin.getFbUserId() );
                        LobbyError lobbyError = new LobbyError();
                        lobbyError.setErrorCode(406);
                        lobbyError.setErrorMsg("fb login error");
                        iSession.sendMessage(lobbyError, fbLogin);

                        PlayerCache.getInstance().delPlayerFbToken(fbLogin.getFbUserId());
                        return;
                    } else {
                        PlayerCache.getInstance().addPlayerFbToken(userId, fbLogin.getAccessToken());
                    }
                }

                PlayerService.getInstance().fbLogin(iSession, fbLogin);
            }

        }
    }
}
