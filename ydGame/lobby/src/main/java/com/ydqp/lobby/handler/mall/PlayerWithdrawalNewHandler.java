package com.ydqp.lobby.handler.mall;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.constant.UpLoadConstant;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.entity.PayChannelConfig;
import com.ydqp.common.entity.Player;
import com.ydqp.common.entity.PlayerAccount;
import com.ydqp.common.receiveProtoMsg.mall.PlayerWithdrawals;
import com.ydqp.common.sendProtoMsg.CoinPointSuccess;
import com.ydqp.common.sendProtoMsg.mall.PlayerWithdrawalSuc;
import com.ydqp.common.task.StatisticsUploadTask;
import com.ydqp.lobby.ThreadManager;
import com.ydqp.lobby.cache.MallCache;
import com.ydqp.lobby.cache.PlayerCache;
import com.ydqp.lobby.pay.ChannelConstant;
import com.ydqp.lobby.pay.cashfree.CashFreePayNew;
import com.ydqp.lobby.pay.razorpay.RazorPay;
import com.ydqp.lobby.pay.yaarpay.YaarPay;
import com.ydqp.lobby.service.PlayerService;
import com.ydqp.lobby.service.mall.PayChannelConfigService;
import com.ydqp.lobby.service.mall.PlayerAccountService;
import com.ydqp.lobby.service.mall.PlayerWithdrawalService;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@ServerHandler(command = 1004202, module = "mall")
public class PlayerWithdrawalNewHandler implements IServerHandler {

    private static final Logger logger = LoggerFactory.getLogger(PlayerWithdrawalNewHandler.class);

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    private static final String WITHDRAWAL = "WITHDRAWAL:PLAYERID:";

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        logger.info("Player withdrawal request: {}", JSONObject.toJSONString(abstartParaseMessage));
        PlayerWithdrawals withdrawals = (PlayerWithdrawals) abstartParaseMessage;

        PlayerWithdrawalSuc withdrawalSuccess = new PlayerWithdrawalSuc();
        long pid = withdrawals.getPlayerId();
        double amount = withdrawals.getAmount();
        if (amount < 100) {
            withdrawalSuccess.setSuccess(false);
            withdrawalSuccess.setMessage("The amount must be greater than 100");
            iSession.sendMessageByID(withdrawalSuccess, withdrawals.getConnId());
            logger.error("提现失败，提现金额小于100，playerId：{}，amount:{}", pid, amount);
            return;
        }
        if (amount % 50 != 0) {
            withdrawalSuccess.setSuccess(false);
            withdrawalSuccess.setMessage("You can only enter multiples of 50");
            iSession.sendMessageByID(withdrawalSuccess, withdrawals.getConnId());
            logger.error("提现失败，提现金额不是50的倍数，playerId：{}，amount:{}", pid, amount);
            return;
        }

        PlayerData playerData = PlayerCache.getInstance().getPlayer(withdrawals.getConnId());
        if (playerData == null) {
            withdrawalSuccess.setSuccess(false);
            withdrawalSuccess.setMessage("The special player is not exist");
            iSession.sendMessageByID(withdrawalSuccess, withdrawals.getConnId());
            logger.error("提现失败，玩家不在线，playerId：{}，amount:{}", pid, amount);
            return;
        }
        if (playerData.getRoomId() != 0) {
            withdrawalSuccess.setSuccess(false);
            withdrawalSuccess.setMessage("No withdrawal is allowed in the game");
            iSession.sendMessageByID(withdrawalSuccess, withdrawals.getConnId());
            logger.error("提现失败，游戏房间中不允许提现，playerId：{}，amount:{}", pid, amount);
            return;
        }
        pid = playerData.getPlayerId();

        Player player = PlayerService.getInstance().queryByCondition(String.valueOf(pid));
//        if (player.getFbBind() == 0) {
//            withdrawalSuccess.setSuccess(false);
//            withdrawalSuccess.setMessage("Please bind your Facebook account first");
//            iSession.sendMessageByID(withdrawalSuccess, withdrawals.getConnId());
//            logger.error("提现失败，未绑定Facebook账户，playerId：{}，amount:{}, balance:{}", pid, amount, player.getZjPoint());
//            return;
//        }
        if (player.getZjPoint() < amount) {
            withdrawalSuccess.setSuccess(false);
            withdrawalSuccess.setMessage("Insufficient account balance");
            iSession.sendMessageByID(withdrawalSuccess, withdrawals.getConnId());
            logger.error("提现失败，账户余额不足，playerId：{}，amount:{}, balance:{}", pid, amount, player.getZjPoint());
            return;
        }
        long playerId = player.getId();
        //查询提现次数
//        int withdrawalCount = PlayerWithdrawalService.getInstance().withdrawalCount(playerId);
        String key = WITHDRAWAL + playerId + ":" + sdf.format(new Date());
        Long withdrawalCount = MallCache.getInstance().getWithdrawalCount(key);
        if (withdrawalCount != null && withdrawalCount >= 2) {
            withdrawalSuccess.setSuccess(false);
            withdrawalSuccess.setMessage("Today`s the number of withdrawals has reached the upper limit");
            iSession.sendMessageByID(withdrawalSuccess, withdrawals.getConnId());
            logger.error("提现失败，今日提现次数已达上限，playerId：{}，amount:{}", pid, amount);
            return;
        }

        //校验通过，扣钱
        double coin = 0 - amount;
        //数据库
        PlayerService.getInstance().updatePlayerZjPoint(coin, playerId);
        //缓存
        playerData.setZjPoint(player.getZjPoint() - amount);
        PlayerCache.getInstance().addPlayer(withdrawals.getConnId(), playerData);

        PlayerAccount playerAccount = PlayerAccountService.getInstance().findByPlayerId(playerId);

        //渠道
        PayChannelConfig payChannelConfig;
        if (playerAccount.getPayChannelId() == null) {
            List<PayChannelConfig> configs = PayChannelConfigService.getInstance().getAllEnableChannel();
            payChannelConfig = configs.get(0);
        } else {
            payChannelConfig = PayChannelConfigService.getInstance().findById(playerAccount.getPayChannelId());
            if (payChannelConfig == null || payChannelConfig.getEnabled() == 0) {
                List<PayChannelConfig> configs = PayChannelConfigService.getInstance().getAllEnableChannel();
                payChannelConfig = configs.get(0);
            }
        }

//        PayChannelConfig payChannelConfig = PayChannelConfigService.getInstance().findByName("razorpay");
        //手续费
        BigDecimal realAmount = new BigDecimal(amount).multiply(BigDecimal.ONE.subtract(payChannelConfig.getWithdrawFee()));

        Map<String, Object> params = new HashMap<String, Object>() {{
            put("playerId", playerId);
            put("name", playerAccount.getName());
            put("accNo", playerAccount.getAccNo());
            put("ifsc", playerAccount.getIfsc());
            put("mobile", playerAccount.getMobile());
            put("email", playerAccount.getEmail());
            put("beneId", playerAccount.getBeneId());
            put("amount", realAmount);
            put("status", 0);
            put("transferId", UUID.randomUUID().toString().replace("-", ""));
            put("createTime", new Long(System.currentTimeMillis() / 1000).intValue());
            put("appId", player.getAppId());
            put("registerTime", player.getCreateTime());
        }};

        boolean success = true;
        String message = "Withdrawal application has been submitted, awaiting review";
        boolean requireStatus = false;
        if (payChannelConfig.getWithdrawalAudit() == 0) {
            requireStatus = true;
            params.put("clientId", payChannelConfig.getClientId());
            params.put("clientSecret", payChannelConfig.getClientSecret());
            //转账请求
            String payoutStr = "";
            switch (payChannelConfig.getChannel()) {
                case ChannelConstant.CASH_FREE:
                    CashFreePayNew cashFreePay = new CashFreePayNew();
                    payoutStr = cashFreePay.payout(JSON.toJSONString(params));
                    break;
                case ChannelConstant.RAZOR_PAY:
                    params.put("businessAccount", payChannelConfig.getBusinessAccount());
                    params.put("faId", playerAccount.getFaId());
                    logger.info("razorpay payout request parameters: {}", JSON.toJSONString(params));
                    payoutStr = new RazorPay().payout(JSON.toJSONString(params));
                    break;
                case ChannelConstant.YAAR_PAY:
                    if (amount < 1050) {
                        withdrawalSuccess.setSuccess(false);
                        withdrawalSuccess.setMessage("The amount must be greater than 1050");
                        iSession.sendMessageByID(withdrawalSuccess, withdrawals.getConnId());
                        logger.error("提现失败，提现金额小于1050，playerId：{}，amount:{}", pid, amount);
                        return;
                    }
                    if (StringUtils.isBlank(playerAccount.getBankCode())) {
                        withdrawalSuccess.setSuccess(false);
                        withdrawalSuccess.setMessage("The bank code cant`t null");
                        iSession.sendMessageByID(withdrawalSuccess, withdrawals.getConnId());
                        logger.error("提现失败，提现金额小于100，playerId：{}，amount:{}", pid, amount);
                        return;
                    }
                    params.put("transferId", String.valueOf(params.get("transferId")).substring(0, 30));
                    params.put("notifyUrl", payChannelConfig.getPayoutNotifyUrl());
                    params.put("mchId", payChannelConfig.getMchId());
                    params.put("payoutBankCode", playerAccount.getBankCode());
                    payoutStr = new YaarPay().payout(JSON.toJSONString(params));
                default:
            }
            logger.info("payout请求返回数据：{}", payoutStr);
            if (StringUtils.isBlank(payoutStr)) {
                withdrawalSuccess.setSuccess(false);
                withdrawalSuccess.setMessage("parameter is null");
                iSession.sendMessageByID(withdrawalSuccess, withdrawals.getConnId());
                logger.error("提现失败，返回为空，playerId：{}，amount:{}", pid, amount);
                return;
            }

            JSONObject payoutJson = JSONObject.parseObject(payoutStr);
            if (payoutJson.getBoolean("success")) {
                params = JSONObject.parseObject(payoutJson.getString("data"));
                params.put("status", 1);
            } else {
                //失败加钱
                //数据库
                PlayerService.getInstance().updatePlayerZjPoint(amount, playerId);
                //缓存
                playerData.setZjPoint(player.getZjPoint());
                PlayerCache.getInstance().addPlayer(withdrawals.getConnId(), playerData);

                String str = payoutJson.getString("data");
                JSONObject json = JSONObject.parseObject(str);
                message = json.getString("message") == null ? "Withdraw failed" : json.getString("message");
                params.put("status", 2);
                params.put("errorMsg", message);

                params.remove("clientId");
                params.remove("clientSecret");
                params.remove("businessAccount");
                params.remove("faId");
                params.remove("notifyUrl");
                params.remove("payoutBankCode");
                params.remove("mchId");

                params.put("amount", amount);
                PlayerWithdrawalService.getInstance().savePlayerWithdrawal(params);

                withdrawalSuccess.setSuccess(false);
                withdrawalSuccess.setMessage(message);
                iSession.sendMessageByID(withdrawalSuccess, withdrawals.getConnId());
                logger.error("提现失败，CashFree支付失败，playerId:{}, amount:{}，message:{}", playerId, amount, message);
                return;
            }
        }

        //提现次数加一
        if (requireStatus) {
            MallCache.getInstance().incrWithdrawalCount(key);
        }
        //通知客户端
        CoinPointSuccess coinPointSuccess = new CoinPointSuccess();
        coinPointSuccess.setCoinPoint(player.getZjPoint() - amount);
        coinPointSuccess.setCoinType(2);
        coinPointSuccess.setPlayerId(playerId);
        iSession.sendMessageByID(coinPointSuccess, withdrawals.getConnId());

        params.put("amount", amount);
        PlayerWithdrawalService.getInstance().savePlayerWithdrawal(params);

        withdrawalSuccess.setSuccess(success);
        withdrawalSuccess.setMessage(message);
        iSession.sendMessageByID(withdrawalSuccess, withdrawals.getConnId());
        //数据上报
        ThreadManager.getInstance().getStatUploadExecutor().execute(new StatisticsUploadTask(UpLoadConstant.WITHDRAWAL, new JSONObject(params)));
    }
}
