package com.ydqp.lobby.handler.mall;

import com.alibaba.fastjson.JSONObject;
import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.data.PlayerAccountData;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.entity.PlayerAccount;
import com.ydqp.common.receiveProtoMsg.mall.PlayerAccountSave;
import com.ydqp.common.sendProtoMsg.mall.PlayerAccountSaveSuccess;
import com.ydqp.lobby.cache.PlayerCache;
import com.ydqp.lobby.service.mall.PlayerAccountService;
import com.ydqp.lobby.utils.MessageCheckUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

@ServerHandler(command = 1004003, module = "mall")
public class PlayerAccountSaveHandler implements IServerHandler {

    private static final Logger logger = LoggerFactory.getLogger(PlayerAccountSaveHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        logger.info("Player account set request: {}", JSONObject.toJSONString(abstartParaseMessage));
        PlayerAccountSave playerAccountSave = (PlayerAccountSave) abstartParaseMessage;

        PlayerAccountSaveSuccess playerAccountSaveSuccess = new PlayerAccountSaveSuccess();

        PlayerData playerData = PlayerCache.getInstance().getPlayer(playerAccountSave.getConnId());
        if (playerData == null) {
            playerAccountSaveSuccess.setSuccess(false);
            playerAccountSaveSuccess.setMessage("player is not true");
            iSession.sendMessageByID(playerAccountSaveSuccess, playerAccountSave.getConnId());
            logger.error("账户信息更新失败，玩家不在线，playerId: {}", playerAccountSave.getPlayerId());
            return;
        }

        long playerId = playerData.getPlayerId();
        String name = playerAccountSave.getName();
        String mobile = playerAccountSave.getMobile();
        String bankName = playerAccountSave.getBankName();
        String accNo = playerAccountSave.getAccNo();
        String ifsc = playerAccountSave.getIfsc();
        String password = playerAccountSave.getPassword();
        String repeatedPassword = playerAccountSave.getRepeatedPassword();

        boolean success = true;
        String message = "";
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(accNo) || StringUtils.isEmpty(ifsc) || StringUtils.isEmpty(mobile)
                || StringUtils.isEmpty(bankName) || StringUtils.isEmpty(password) || StringUtils.isEmpty(repeatedPassword)) {
            success = false;
            message = "Account info can not be emppty";
            logger.error("{}账户信息更新失败,存在为空的字段,playerAccountInfo:{}", JSONObject.toJSONString(playerAccountSave));
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
        if (!password.equals(repeatedPassword)) {
            success = false;
            message = "Inconsistent passwords";
            logger.error("账户信息更新失败，密码不一致，playerId:{}, password:{}, repeatedPassword:{}", playerId, password, repeatedPassword);
        }
        if (!success) {
            playerAccountSaveSuccess.setSuccess(false);
            playerAccountSaveSuccess.setMessage(message);
            iSession.sendMessageByID(playerAccountSaveSuccess, playerAccountSave.getConnId());
            return;
        }

        PlayerAccount account = PlayerAccountService.getInstance().findByAccNo(accNo);
        if (account != null) {
            playerAccountSaveSuccess.setSuccess(false);
            playerAccountSaveSuccess.setMessage("Bank card has been bound");
            iSession.sendMessageByID(playerAccountSaveSuccess, playerAccountSave.getConnId());
            return;
        }

        //所有可用支付
//        List<PayChannelConfig> payChannelConfigs = PayChannelConfigService.getInstance().getAllEnableChannel();
//        if (CollectionUtils.isEmpty(payChannelConfigs)) {
//            playerAccountUpdateSuccess.setSuccess(false);
//            playerAccountUpdateSuccess.setMessage("No payment channel");
//            iSession.sendMessageByID(playerAccountUpdateSuccess, playerAccountUpdate.getConnId());
//            return;
//        }
//        PayChannelConfig payChannelConfig = PayUtil.getInstance().getConfig(payChannelConfigs, playerId);

        PlayerAccount playerAccount = new PlayerAccount();
        playerAccount.setPlayerId(playerId);
        playerAccount.setName(name);
        playerAccount.setMobile(mobile);
        playerAccount.setBankName(bankName);
        playerAccount.setAccNo(accNo);
        playerAccount.setIfsc(ifsc);
        playerAccount.setPassword(DigestUtils.md5Hex(password));
        PlayerAccountService.getInstance().savePlayerAccount(playerAccount.getParameterMap());
//        int withdrawalCount = PlayerWithdrawalService.getInstance().withdrawalCount(playerId);
        PlayerAccountData playerAccountData = new PlayerAccountData(playerAccount);

        logger.info("账户信息更新成功，playerId: {}", playerAccountSave.getPlayerId());
        playerAccountSaveSuccess.setSuccess(true);
        playerAccountSaveSuccess.setPlayerAccountData(playerAccountData);
        iSession.sendMessageByID(playerAccountSaveSuccess, playerAccountSave.getConnId());
    }
}
