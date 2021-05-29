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
import com.ydqp.common.receiveProtoMsg.mall.PlayerAccountUpdate;
import com.ydqp.common.sendProtoMsg.mall.PlayerAccountUpdateSuccess;
import com.ydqp.lobby.service.mall.PlayerAccountService;
import com.ydqp.lobby.utils.MessageCheckUtil;
import org.apache.commons.lang.StringUtils;

@ServerHandler(command = 1004007, module = "mall")
public class PlayerAccountUpdateHandler implements IServerHandler {

    private static final Logger logger = LoggerFactory.getLogger(PlayerAccountUpdateHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        logger.info("Player account set request: {}", JSONObject.toJSONString(abstartParaseMessage));
        PlayerAccountUpdate playerAccountUpdate = (PlayerAccountUpdate) abstartParaseMessage;

        PlayerAccountUpdateSuccess playerAccountUpdateSuccess = new PlayerAccountUpdateSuccess();

        PlayerData playerData = PlayerCache.getInstance().getPlayer(playerAccountUpdate.getConnId());
        if (playerData == null) {
            playerAccountUpdateSuccess.setSuccess(false);
            playerAccountUpdateSuccess.setMessage("player is not true");
            iSession.sendMessageByID(playerAccountUpdateSuccess, playerAccountUpdate.getConnId());
            logger.error("账户信息更新失败，玩家不在线，playerId: {}", playerAccountUpdate.getPlayerId());
            return;
        }

        long playerId = playerData.getPlayerId();
        String name = playerAccountUpdate.getName();
        String mobile = playerAccountUpdate.getMobile();
        String bankName = playerAccountUpdate.getBankName();
        String accNo = playerAccountUpdate.getAccNo();
        String ifsc = playerAccountUpdate.getIfsc();
        String verifyCode = playerAccountUpdate.getVerificationCode();

        boolean success = true;
        String message = "";
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(accNo) || StringUtils.isEmpty(ifsc) || StringUtils.isEmpty(mobile)
                || StringUtils.isEmpty(bankName) || StringUtils.isEmpty(verifyCode) ) {
            success = false;
            message = "Account info can not be emppty";
            logger.error("{}账户信息更新失败,存在为空的字段,playerAccountInfo:{}", JSONObject.toJSONString(playerAccountUpdate));
        }

        if ("paytm123456".equals(ifsc)) {
            success = false;
            message = "Does not support paytm";
            logger.error("paytm123456不允许绑定，playerId:{}", playerId);
        }

        if (name.length() > 100) {
            success = false;
            message = "Beneficiary name only alphabets and white space (100 character limit)";
            logger.error("账户信息更新失败，名字格式错误，playerId:{}, name:{}", playerId, name);
        }
        if (!MessageCheckUtil.checkMobile(mobile)) {
            success = false;
            message = "Beneficiaries phone number(only digits, 8-12 characters after stripping +91)";
            logger.error("账户信息更新失败，电话号码格式错误，playerId:{}, mobile:{}", playerId, mobile);
        }
        if (!MessageCheckUtil.checkAccNo(accNo)) {
            success = false;
            message = "Beneficiary bank account (9-18 alphanumeric character limit)";
            logger.error("账户信息更新失败，accNo格式错误，playerId:{}, accNo:{}", playerId, accNo);
        }
        if (ifsc.length() != 11) {
            success = false;
            message = "Accounts IFSC (standard IFSC format) - length 11";
            logger.error("账户信息更新失败，ifsc格式错误，playerId:{}, ifsc:{}", playerId, ifsc);
        }
        String verificationCode = PlayerCache.getInstance().getVerificationCode(playerData.getPlayerName());
        if (!verifyCode.equals(verificationCode)) {
            playerAccountUpdateSuccess.setSuccess(false);
            playerAccountUpdateSuccess.setMessage("Verification code error");
            iSession.sendMessageByID(playerAccountUpdateSuccess, playerAccountUpdate.getConnId());
            logger.error("手机注册，验证码错误，playerName:{}", playerData.getPlayerName());
            return;
        }
        if (!success) {
            playerAccountUpdateSuccess.setSuccess(false);
            playerAccountUpdateSuccess.setMessage(message);
            iSession.sendMessageByID(playerAccountUpdateSuccess, playerAccountUpdate.getConnId());
            return;
        }

        PlayerAccount account = PlayerAccountService.getInstance().findByPlayerId(playerId);
        if (account == null) {
            playerAccountUpdateSuccess.setSuccess(false);
            playerAccountUpdateSuccess.setMessage("Please bind bank card information first");
            iSession.sendMessageByID(playerAccountUpdateSuccess, playerAccountUpdate.getConnId());
            return;
        }
        if (!accNo.equals(account.getAccNo())) {
            PlayerAccount account2 = PlayerAccountService.getInstance().findByAccNo(accNo);
            if (account2 != null) {
                playerAccountUpdateSuccess.setSuccess(false);
                playerAccountUpdateSuccess.setMessage("Bank card has been bound");
                iSession.sendMessageByID(playerAccountUpdateSuccess, playerAccountUpdate.getConnId());
                return;
            }
        }

        PlayerAccount playerAccount = new PlayerAccount();
        playerAccount.setPlayerId(playerId);
        playerAccount.setName(name);
        playerAccount.setMobile(mobile);
        playerAccount.setBankName(bankName);
        playerAccount.setAccNo(accNo);
        playerAccount.setIfsc(ifsc);
        PlayerAccountService.getInstance().updatePlayerAccount(new Object[]{name, mobile, bankName, accNo, ifsc, playerId});

        PlayerAccountData playerAccountData = new PlayerAccountData(playerAccount);

        logger.info("账户信息更新成功，playerId: {}", playerAccountUpdate.getPlayerId());
        playerAccountUpdateSuccess.setSuccess(true);
        playerAccountUpdateSuccess.setPlayerAccountData(playerAccountData);
        iSession.sendMessageByID(playerAccountUpdateSuccess, playerAccountUpdate.getConnId());
    }
}
