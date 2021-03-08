package com.ydqp.lobby.handler.mall;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.data.PlayerAccountData;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.entity.PayChannelConfig;
import com.ydqp.common.entity.PlayerAccount;
import com.ydqp.common.receiveProtoMsg.mall.PlayerAccountUpdate;
import com.ydqp.common.sendProtoMsg.mall.PlayerAccountUpdateSuccess;
import com.ydqp.lobby.cache.PlayerCache;
import com.ydqp.lobby.pay.cashfree.api.CashFreeApi;
import com.ydqp.lobby.pay.razorpay.RazorPay;
import com.ydqp.lobby.service.mall.PayChannelConfigService;
import com.ydqp.lobby.service.mall.PlayerAccountService;
import com.ydqp.lobby.service.mall.PlayerWithdrawalService;
import com.ydqp.lobby.utils.MessageCheckUtil;
import com.ydqp.lobby.utils.PayUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ServerHandler(command = 1004003, module = "mall")
public class PlayerAccountUpdateHandler implements IServerHandler {

    private static final Logger logger = LoggerFactory.getLogger(PlayerAccountUpdateHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        logger.info("Player account set request: {}", JSONObject.toJSONString(abstartParaseMessage));
        PlayerAccountUpdate playerAccountUpdate = (PlayerAccountUpdate) abstartParaseMessage;

        PlayerAccountUpdateSuccess playerAccountUpdateSuccess = new PlayerAccountUpdateSuccess();

//        Player player = PlayerService.getInstance().queryByCondition(String.valueOf(playerAccountUpdate.getPlayerId()));
//        PlayerData playerData = new PlayerData(player);
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
        String accNo = playerAccountUpdate.getAccNo();
        String ifsc = playerAccountUpdate.getIfsc();
        String mobile = playerAccountUpdate.getMobile();
        String bankCode = StringUtils.isBlank(playerAccountUpdate.getBankCode()) ? "" : playerAccountUpdate.getBankCode();

        boolean success = true;
        String message = "";
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(accNo) || StringUtils.isEmpty(ifsc) || StringUtils.isEmpty(mobile)) {
            success = false;
            message = "Name,accNo,ifsc,mobile can not be emppty";
            logger.error("{}账户信息更新失败,存在为空的字段,name:{},accNo:{},ifsc:{},mobile:{}", playerId, name, accNo, ifsc, mobile);
        }

        if (name.length() > 100 || !MessageCheckUtil.checkName(name)) {
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
        if (!success) {
            playerAccountUpdateSuccess.setSuccess(false);
            playerAccountUpdateSuccess.setMessage(message);
            iSession.sendMessageByID(playerAccountUpdateSuccess, playerAccountUpdate.getConnId());
            return;
        }

        //所有可用支付
        List<PayChannelConfig> payChannelConfigs = PayChannelConfigService.getInstance().getAllEnableChannel();
        if (CollectionUtils.isEmpty(payChannelConfigs)) {
            playerAccountUpdateSuccess.setSuccess(false);
            playerAccountUpdateSuccess.setMessage("Pay channel is not open");
            iSession.sendMessageByID(playerAccountUpdateSuccess, playerAccountUpdate.getConnId());
            return;
        }
        PayChannelConfig payChannelConfig = PayUtil.getInstance().getConfig(payChannelConfigs);

        PlayerAccount playerAccount = new PlayerAccount();
        playerAccount.setPlayerId(playerId);
        playerAccount.setName(name);
        playerAccount.setAccNo(accNo);
        playerAccount.setIfsc(ifsc);
        playerAccount.setMobile(mobile);
        playerAccount.setBankCode(bankCode);

        boolean deal = true;
        JSONObject jsonObject = new JSONObject();
        switch (payChannelConfig.getName()) {
            case "cashfree":
                jsonObject = setBeneId(playerId, payChannelConfig, playerAccountUpdate);
                break;
            case "razorpay":
                jsonObject = setFaId(playerId, payChannelConfig, playerAccountUpdate);
                break;
            case "yaarpay":
                deal = false;
                break;
            default:
        }

        PlayerAccountData playerAccountData;
        if (deal) {
            if (!jsonObject.getBoolean("success")) {
                playerAccountUpdateSuccess.setSuccess(false);
                playerAccountUpdateSuccess.setMessage(jsonObject.getString("message"));
                iSession.sendMessageByID(playerAccountUpdateSuccess, playerAccountUpdate.getConnId());
                return;
            }
            String str = jsonObject.getString("playerAccountData");
            playerAccountData = JSONObject.parseObject(str, PlayerAccountData.class);
        } else {
            PlayerAccount account = PlayerAccountService.getInstance().findByPlayerId(playerId);
            if (account == null) {
                PlayerAccountService.getInstance().savePlayerAccount(playerAccount.getParameterMap());
            } else {
                PlayerAccountService.getInstance().updateYaarAccount(new Object[]{name, accNo, ifsc, mobile, bankCode, playerId});
            }
            int withdrawalCount = PlayerWithdrawalService.getInstance().withdrawalCount(playerId);
            playerAccountData = new PlayerAccountData(playerAccount, withdrawalCount);
        }

        logger.info("账户信息更新成功，playerId: {}", playerAccountUpdate.getPlayerId());
        playerAccountUpdateSuccess.setSuccess(true);
        playerAccountUpdateSuccess.setPlayerAccountData(playerAccountData);
        iSession.sendMessageByID(playerAccountUpdateSuccess, playerAccountUpdate.getConnId());
    }

    private JSONObject setBeneId(long playerId, PayChannelConfig payChannelConfig, PlayerAccountUpdate playerAccountUpdate) {
        boolean success = true;
        String message = "";

        String name = playerAccountUpdate.getName();
        String accNo = playerAccountUpdate.getAccNo();
        String ifsc = playerAccountUpdate.getIfsc();
        String mobile = playerAccountUpdate.getMobile();
        Map<String, Object> params = new HashMap<String, Object>() {{
            put("playerId", playerId);
            put("name", name);
            put("accNo", accNo);
            put("ifsc", ifsc);
            put("mobile", mobile);
        }};

        String beneId = "";
        String authorize = CashFreeApi.getInstance().authorize(payChannelConfig.getClientId(), payChannelConfig.getClientSecret());
        logger.info("获取cashfree payout token：{}", authorize);
        if (StringUtils.isNotBlank(authorize)) {
            JSONObject tokenJson = JSONObject.parseObject(authorize);
            if ("SUCCESS".equals(tokenJson.getString("status"))) {
                String token = tokenJson.getJSONObject("data").getString("token");

                JSONObject param = new JSONObject() {{
                    put("bankAccount", params.get("accNo"));
                    put("ifsc", params.get("ifsc"));
                }};
                logger.info("查询银行账户是否添加收款人,参数：{}", param.toJSONString());
                String beneIdStr = CashFreeApi.getInstance().getBeneId(token, param);
                logger.info("查询银行账户是否添加收款人：{}", beneIdStr);
                if (StringUtils.isNotBlank(beneIdStr)) {
                    JSONObject beneIdJson = JSONObject.parseObject(beneIdStr);
                    if ("SUCCESS".equals(beneIdJson.getString("status"))) {
                        beneId = beneIdJson.getJSONObject("data").getString("beneId");
                    } else if ("409".equals(beneIdJson.getString("subCode"))) {
                        success = false;
                        message = beneIdJson.getString("message");
                    }
                }
            }
        }
        if (!success) {
            String finalMessage = message;
            return new JSONObject() {{
                put("success", false);
                put("message", finalMessage);
            }};
        }

        beneId = StringUtils.isNotBlank(beneId) ? beneId : name.replaceAll(" ", "") + playerId;
        if (beneId.length() > 50) beneId = beneId.substring(0, 50);
        PlayerAccountData playerAccountData = JSONObject.parseObject(JSON.toJSONString(params), PlayerAccountData.class);
        PlayerAccount playerAccount = PlayerAccountService.getInstance().findByPlayerId(playerId);
        if (playerAccount == null) {
            params.put("beneId", beneId);
            params.put("payChannelId", payChannelConfig.getId());
            PlayerAccountService.getInstance().savePlayerAccount(params);
        } else {
            PlayerAccountService.getInstance().updatePlayerAccount(new Object[]{name, accNo, ifsc, mobile, beneId, playerId});

            int withdrawalCount = PlayerWithdrawalService.getInstance().withdrawalCount(playerId);
            playerAccountData.setWithdrawalCount(withdrawalCount);
        }
        return new JSONObject() {{
            put("success", true);
            put("playerAccountData", JSONObject.toJSONString(playerAccountData));
        }};
    }

    private JSONObject setFaId(long playerId, PayChannelConfig payChannelConfig, PlayerAccountUpdate playerAccountUpdate) {
        String name = playerAccountUpdate.getName();
        String accNo = playerAccountUpdate.getAccNo();
        String ifsc = playerAccountUpdate.getIfsc();
        String mobile = playerAccountUpdate.getMobile();
        Map<String, Object> params = new HashMap<String, Object>() {{
            put("playerId", playerId);
            put("name", name);
            put("accNo", accNo);
            put("ifsc", ifsc);
            put("mobile", mobile);
        }};

        JSONObject data = new JSONObject();
        data.put("clientId", payChannelConfig.getAppId());
        data.put("clientSecret", payChannelConfig.getSecretKey());
        data.put("playerId", playerId);
        data.put("name", name);
        data.put("accNo", accNo);
        data.put("ifsc", ifsc);
        String str = new RazorPay().getFaId(data.toJSONString());

        JSONObject result = JSON.parseObject(str);
        if (!result.getBoolean("success")) return result;
        String faId = result.getString("faId");

        PlayerAccountData playerAccountData = JSONObject.parseObject(JSON.toJSONString(params), PlayerAccountData.class);
        PlayerAccount playerAccount = PlayerAccountService.getInstance().findByPlayerId(playerId);
        if (playerAccount == null) {
            params.put("faId", faId);
            params.put("payChannelId", payChannelConfig.getId());
            PlayerAccountService.getInstance().savePlayerAccount(params);
        } else {
            PlayerAccountService.getInstance().updatePlayerAccountWithFaId(new Object[]{name, accNo, ifsc, mobile, faId, playerId});

            int withdrawalCount = PlayerWithdrawalService.getInstance().withdrawalCount(playerId);
            playerAccountData.setWithdrawalCount(withdrawalCount);
        }
        return new JSONObject() {{
            put("success", true);
            put("playerAccountData", JSONObject.toJSONString(playerAccountData));
        }};
    }

    public static void main(String[] args) {
        PlayerAccountUpdate playerAccountUpdate = new PlayerAccountUpdate();
        playerAccountUpdate.setPlayerId(1069093);
        playerAccountUpdate.setName("hwhwhw");
        playerAccountUpdate.setMobile("1234567980");
        playerAccountUpdate.setAccNo("026291800001191");
        playerAccountUpdate.setIfsc("YESB0000262");

        new PlayerAccountUpdateHandler().process(null, playerAccountUpdate);
    }
}
