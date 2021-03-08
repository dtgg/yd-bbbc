package com.ydqp.lobby.handler.mall;

import com.alibaba.fastjson.JSONObject;
import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.entity.PlayerAccount;
import com.ydqp.common.receiveProtoMsg.mall.PlayerPayInfoSet;
import com.ydqp.common.sendProtoMsg.mall.PlayerPayInfoSetSuccess;
import com.ydqp.lobby.cache.PlayerCache;
import com.ydqp.lobby.service.mall.PlayerAccountService;
import com.ydqp.lobby.utils.MessageCheckUtil;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

@ServerHandler(command = 1004007, module = "mall")
public class PlayerPayInfoSetHandler implements IServerHandler {

    private static final Logger logger = LoggerFactory.getLogger(PlayerPayInfoSetHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        logger.info("Player pay info set request: {}", JSONObject.toJSONString(abstartParaseMessage));
        PlayerPayInfoSet payInfoSet = (PlayerPayInfoSet) abstartParaseMessage;

        PlayerPayInfoSetSuccess payInfoSetSuccess = new PlayerPayInfoSetSuccess();

        PlayerData playerData = PlayerCache.getInstance().getPlayer(payInfoSet.getConnId());
        if (playerData == null) {
            payInfoSetSuccess.setSuccess(false);
            payInfoSetSuccess.setMessage("player is not true");
            iSession.sendMessageByID(payInfoSetSuccess, payInfoSet.getConnId());
            logger.error("player is not true");
            return;
        }

        long playerId = playerData.getPlayerId();
        String payMobile = payInfoSet.getPayMobile();
        String email = payInfoSet.getEmail();
        String depositName = payInfoSet.getDepositName();
        if (!MessageCheckUtil.checkMobile(payMobile)) {
            payInfoSetSuccess.setSuccess(false);
            payInfoSetSuccess.setMessage("Phone number format error");
            iSession.sendMessageByID(payInfoSetSuccess, payInfoSet.getConnId());
            logger.error("电话号码格式错误，playerId:{}, phone:{}", playerId, payMobile);
            return;
        }
        if (!MessageCheckUtil.checkEmail(email)) {
            payInfoSetSuccess.setSuccess(false);
            payInfoSetSuccess.setMessage("Email format error");
            iSession.sendMessageByID(payInfoSetSuccess, payInfoSet.getConnId());
            logger.error("邮件格式错误，playerId:{}, email:{}", playerId, email);
            return;
        }
        if (StringUtils.isNotBlank(depositName) && depositName.length() > 64) {
            payInfoSetSuccess.setSuccess(false);
            payInfoSetSuccess.setMessage("Name format error, The length should be less than 64 characters");
            iSession.sendMessageByID(payInfoSetSuccess, payInfoSet.getConnId());
            logger.error("用户存款姓名格式错误，playerId:{}, depositName:{}", playerId, depositName);
            return;
        }

        if (StringUtils.isBlank(depositName)) depositName = "";

        String finalDepositName = depositName;
        Map<String, Object> params = new HashMap<String, Object>() {{
            put("playerId", playerId);
            put("payMobile", payMobile);
            put("email", email);
            put("depositName", finalDepositName);
        }};

        PlayerAccount playerAccount = PlayerAccountService.getInstance().findByPlayerId(playerId);
        if (playerAccount == null) {
            PlayerAccountService.getInstance().savePlayerAccount(params);
        } else {
            PlayerAccountService.getInstance().updateAccountPayInfo(new Object[]{payMobile, email, depositName, playerId});
        }

        payInfoSetSuccess.setSuccess(true);
        iSession.sendMessageByID(payInfoSetSuccess, payInfoSet.getConnId());
    }
}
