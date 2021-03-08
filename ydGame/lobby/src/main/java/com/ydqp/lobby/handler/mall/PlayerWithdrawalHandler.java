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
import com.ydqp.common.receiveProtoMsg.mall.PlayerWithdrawalApply;
import com.ydqp.common.sendProtoMsg.CoinPointSuccess;
import com.ydqp.common.sendProtoMsg.mall.PlayerWithdrawalSuccess;
import com.ydqp.common.task.StatisticsUploadTask;
import com.ydqp.lobby.ThreadManager;
import com.ydqp.lobby.cache.MallCache;
import com.ydqp.lobby.cache.PlayerCache;
import com.ydqp.lobby.pay.ChannelConstant;
import com.ydqp.lobby.pay.cashfree.CashFreePay;
import com.ydqp.lobby.service.PlayerService;
import com.ydqp.lobby.service.mall.PayChannelConfigService;
import com.ydqp.lobby.service.mall.PlayerAccountService;
import com.ydqp.lobby.service.mall.PlayerWithdrawalService;
import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ServerHandler(command = 1004005, module = "mall")
public class PlayerWithdrawalHandler implements IServerHandler {

    private static final Logger logger = LoggerFactory.getLogger(PlayerWithdrawalHandler.class);

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    private static final String WITHDRAWAL = "WITHDRAWAL:PLAYERID:";

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        logger.info("Player withdrawal request: {}", JSONObject.toJSONString(abstartParaseMessage));
        PlayerWithdrawalApply withdrawalApply = (PlayerWithdrawalApply) abstartParaseMessage;

        PlayerWithdrawalSuccess withdrawalSuccess = new PlayerWithdrawalSuccess();
        long pid = withdrawalApply.getPlayerId();
        double amount = withdrawalApply.getAmount();
        if (amount < 100) {
            withdrawalSuccess.setSuccess(false);
            withdrawalSuccess.setMessage("The amount must be greater than 100");
            iSession.sendMessageByID(withdrawalSuccess, withdrawalApply.getConnId());
            logger.error("提现失败，提现金额小于100，playerId：{}，amount:{}", pid, amount);
            return;
        }
        if (amount % 50 != 0) {
            withdrawalSuccess.setSuccess(false);
            withdrawalSuccess.setMessage("You can only enter multiples of 50");
            iSession.sendMessageByID(withdrawalSuccess, withdrawalApply.getConnId());
            logger.error("提现失败，提现金额不是整数，playerId：{}，amount:{}", pid, amount);
            return;
        }

        PlayerData playerData = PlayerCache.getInstance().getPlayer(withdrawalApply.getConnId());
        if (playerData == null) {
            withdrawalSuccess.setSuccess(false);
            withdrawalSuccess.setMessage("The special player is not exist");
            iSession.sendMessageByID(withdrawalSuccess, withdrawalApply.getConnId());
            logger.error("提现失败，玩家不在线，playerId：{}，amount:{}", pid, amount);
            return;
        }
        if (playerData.getRoomId() != 0) {
            withdrawalSuccess.setSuccess(false);
            withdrawalSuccess.setMessage("No withdrawal is allowed in the game");
            iSession.sendMessageByID(withdrawalSuccess, withdrawalApply.getConnId());
            logger.error("提现失败，游戏房间中不允许提现，playerId：{}，amount:{}", pid, amount);
            return;
        }
        Player player = PlayerService.getInstance().queryByCondition(String.valueOf(playerData.getPlayerId()));
        if (player.getFbBind() == 0) {
            withdrawalSuccess.setSuccess(false);
            withdrawalSuccess.setMessage("Please bind your Facebook account first");
            iSession.sendMessageByID(withdrawalSuccess, withdrawalApply.getConnId());
            logger.error("提现失败，未绑定Facebook账户，playerId：{}，amount:{}, balance:{}", pid, amount, player.getZjPoint());
            return;
        }
        if (player.getZjPoint() < amount) {
            withdrawalSuccess.setSuccess(false);
            withdrawalSuccess.setMessage("Insufficient account balance");
            iSession.sendMessageByID(withdrawalSuccess, withdrawalApply.getConnId());
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
            iSession.sendMessageByID(withdrawalSuccess, withdrawalApply.getConnId());
            logger.error("提现失败，今日提现次数已达上限，playerId：{}，amount:{}", pid, amount);
            return;
        }

        PlayerAccount playerAccount = PlayerAccountService.getInstance().findByPlayerId(playerId);
//        if (player.getZjPoint() - playerAccount.getWithdrawing() < amount) {
//            withdrawalSuccess.setSuccess(false);
//            withdrawalSuccess.setMessage("Account withdrawable balance is insufficient");
//            iSession.sendMessageByID(withdrawalSuccess, withdrawalApply.getConnId());
//            return;
//        }

        String beneId = playerAccount.getBeneId();
        if (StringUtils.isBlank(beneId)) {
            beneId = playerAccount.getName().replaceAll(" ", "") + playerId;
        }
        if (beneId.length() > 50) beneId = beneId.substring(0, 50);
        String finalBeneId = beneId;
        Map<String, Object> params = new HashMap<String, Object>() {{
            put("playerId", playerId);
            put("name", playerAccount.getName());
            put("accNo", playerAccount.getAccNo());
            put("ifsc", playerAccount.getIfsc());
            put("mobile", playerAccount.getMobile());
            put("email", playerAccount.getEmail());
            put("beneId", finalBeneId);
            put("amount", amount);
            put("status", 0);
            put("transferId", UUID.randomUUID().toString().replace("-", ""));
            put("createTime", new Long(System.currentTimeMillis() / 1000).intValue());
            put("appId", playerData.getAppId());
            put("registerTime", playerData.getRegisterTime());
        }};

        PayChannelConfig payChannelConfig = PayChannelConfigService.getInstance().getPayChannelConfig();
        boolean success = true;
        String message = "Withdrawal application has been submitted, awaiting review";
        boolean requireStatus = false;
        boolean payoutStatus = false;
        if (payChannelConfig.getWithdrawalAudit() == 0) {
            logger.info("向CashFree发起提现申请");
            requireStatus = true;
            params.put("clientId", payChannelConfig.getClientId());
            params.put("clientSecret", payChannelConfig.getClientSecret());
            //转账请求
            String payoutStr = "";
            switch (payChannelConfig.getChannel()) {
                case ChannelConstant.CASH_FREE:
                    CashFreePay cashFreePay = new CashFreePay();
                    payoutStr = cashFreePay.payout(JSON.toJSONString(params));
                    break;
                default:
            }
            logger.info("cashFree返回数据：{}", payoutStr);
            if (StringUtils.isBlank(payoutStr)) {
                withdrawalSuccess.setSuccess(false);
                withdrawalSuccess.setMessage("parameter is null");
                iSession.sendMessageByID(withdrawalSuccess, withdrawalApply.getConnId());
                logger.error("提现失败，请求CashFree返回为空，playerId：{}，amount:{}", pid, amount);
                return;
            }

            JSONObject payoutJson = JSONObject.parseObject(payoutStr);
            if (payoutJson.getBoolean("success")) {
                params = JSONObject.parseObject(payoutJson.getString("data"));
                params.put("status", 1);
                payoutStatus = true;
//                if ("1".equals(String.valueOf(params.get("status")))) {
//                    payoutStatus = true;
//                    message = "Withdrawal success, please wait for the bank transfer";
//                    logger.error("提现成功，playerId:{}，amount:{}，result:{}", playerId, amount, payoutStr);
//                } else {
//                    message = String.valueOf(params.get("errorMsg"));
//                    success = false;
//                    logger.error("提现失败，CashFree支付失败，playerId:{}, amount:{}，message:{}", playerId, amount, message);
//                }
            } else {
                String str = payoutJson.getString("data");
                JSONObject json = JSONObject.parseObject(str);
                message = json.getString("message");
                params.put("status", 2);
                params.put("errorMsg", message);

                params.remove("clientId");
                params.remove("clientSecret");
                PlayerWithdrawalService.getInstance().savePlayerWithdrawal(params);

                withdrawalSuccess.setSuccess(false);
                withdrawalSuccess.setMessage(message);
                iSession.sendMessageByID(withdrawalSuccess, withdrawalApply.getConnId());
                logger.error("提现失败，CashFree支付失败，playerId:{}, amount:{}，message:{}", playerId, amount, message);

                //数据上报
//                ThreadManager.getInstance().getStatUploadExecutor().execute(new StatisticsUploadTask(UpLoadConstant.WITHDRAWAL, new JSONObject(params)));
                return;
            }
        }

        double coin = 0 - amount;
        if (!requireStatus) {
            PlayerService.getInstance().updatePlayerZjPoint(coin, playerId);

            playerData.setZjPoint(player.getZjPoint() - amount);
            PlayerCache.getInstance().addPlayer(withdrawalApply.getConnId(), playerData);

            CoinPointSuccess coinPointSuccess = new CoinPointSuccess();
            coinPointSuccess.setCoinPoint(player.getZjPoint() - amount);
            coinPointSuccess.setCoinType(2);
            coinPointSuccess.setPlayerId(playerId);
            iSession.sendMessageByID(coinPointSuccess, withdrawalApply.getConnId());
//            logger.info("提现申请添加成功，待审核，playerId:{}, amount:{}", playerId, amount);
        } else {
            if (payoutStatus) {
//                if (playerData.getRoomId() != 0) {
//                    SPlayerAddMoney playerAddMoney = new SPlayerAddMoney();
//                    playerAddMoney.setCommand(1000050);
//                    playerAddMoney.setPlayerId(playerId);
//                    playerAddMoney.setRoomId(playerData.getRoomId());
//                    playerAddMoney.setMoney(coin);
//                    playerAddMoney.setType(1);
//                    playerAddMoney.setConnId(withdrawalApply.getConnId());
//                    iSession.sendMessage(playerAddMoney, withdrawalApply);
//                } else {
//
//                }
                PlayerService.getInstance().updatePlayerZjPoint(coin, playerId);

                double point = player.getZjPoint() - amount;
                playerData.setZjPoint(point);
                PlayerCache.getInstance().addPlayer(withdrawalApply.getConnId(), playerData);

                CoinPointSuccess coinPointSuccess = new CoinPointSuccess();
                coinPointSuccess.setCoinPoint(point);
                coinPointSuccess.setCoinType(2);
                coinPointSuccess.setPlayerId(playerId);
                iSession.sendMessageByID(coinPointSuccess, withdrawalApply.getConnId());
//                logger.info("提现操作完成，等待CashFree转账，playerId:{}, amount:{}", playerId, amount);

                MallCache.getInstance().incrWithdrawalCount(key);
            }
        }

        PlayerWithdrawalService.getInstance().savePlayerWithdrawal(params);

        withdrawalSuccess.setSuccess(success);
        withdrawalSuccess.setMessage(message);
        iSession.sendMessageByID(withdrawalSuccess, withdrawalApply.getConnId());

        //数据上报
        ThreadManager.getInstance().getStatUploadExecutor().execute(new StatisticsUploadTask(UpLoadConstant.WITHDRAWAL, new JSONObject(params)));
    }
}
