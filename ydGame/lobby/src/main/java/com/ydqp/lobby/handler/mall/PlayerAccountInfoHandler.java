package com.ydqp.lobby.handler.mall;

import com.alibaba.fastjson.JSONObject;
import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.data.PlayerAccountData;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.entity.PlayerAccount;
import com.ydqp.common.receiveProtoMsg.mall.PlayerAccountInfo;
import com.ydqp.common.sendProtoMsg.mall.PlayerAccountInfoSuccess;
import com.ydqp.lobby.service.mall.PlayerAccountService;

@ServerHandler(command = 1004004, module = "mall")
public class PlayerAccountInfoHandler implements IServerHandler {

    private static final Logger logger = LoggerFactory.getLogger(PlayerAccountInfoHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        logger.info("Player account info request: {}", JSONObject.toJSONString(abstartParaseMessage));
        PlayerAccountInfo accountInfo = (PlayerAccountInfo) abstartParaseMessage;

        PlayerData playerData = PlayerCache.getInstance().getPlayer(accountInfo.getConnId());
        if (playerData == null) {
            logger.error("player is not true");
            return;
        }

        PlayerAccount playerAccount = PlayerAccountService.getInstance().findByPlayerId(playerData.getPlayerId());
//        int withdrawalCount = PlayerWithdrawalService.getInstance().withdrawalCount(playerData.getPlayerId());

        PlayerAccountInfoSuccess success = new PlayerAccountInfoSuccess();
        success.setPlayerAccountData(
                playerAccount == null ? new PlayerAccountData() : new PlayerAccountData(playerAccount));
        iSession.sendMessageByID(success, accountInfo.getConnId());
    }
}
