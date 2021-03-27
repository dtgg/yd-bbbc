package com.ydqp.lobby.handler.mall;

import com.alibaba.fastjson.JSONObject;
import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.entity.PayWithdrawalConfig;
import com.ydqp.common.entity.Player;
import com.ydqp.common.entity.PlayerAccount;
import com.ydqp.common.receiveProtoMsg.mall.PlayerWithdrawal;
import com.ydqp.common.sendProtoMsg.CoinPointSuccess;
import com.ydqp.common.sendProtoMsg.mall.PlayerWithdrawalSuccess;
import com.ydqp.lobby.cache.MallCache;
import com.ydqp.lobby.pay.CommonPay;
import com.ydqp.lobby.pay.OrderPay;
import com.ydqp.lobby.service.PlayerService;
import com.ydqp.lobby.service.mall.PayWithdrawalConfigService;
import com.ydqp.lobby.service.mall.PlayerAccountService;
import com.ydqp.lobby.service.mall.PlayerWithdrawalService;
import com.ydqp.lobby.utils.PayUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@ServerHandler(command = 1004005, module = "mall")
public class PlayerWithdrawalHandler implements IServerHandler {

    private static final Logger logger = LoggerFactory.getLogger(PlayerWithdrawalHandler.class);

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    private static final String WITHDRAWAL = "WITHDRAWAL:PLAYERID:";

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        logger.info("Player withdrawal request: {}", JSONObject.toJSONString(abstartParaseMessage));
        PlayerWithdrawal withdrawals = (PlayerWithdrawal) abstartParaseMessage;
        PlayerWithdrawalSuccess withdrawalSuccess = new PlayerWithdrawalSuccess();

        ZonedDateTime ydTime = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("+05:30"));
        logger.info("当前提现时间：{}:{}", ydTime.getHour(), ydTime.getMinute());
        if ((ydTime.getHour() >= 0 && ydTime.getHour() < 9) || (ydTime.getHour() == 9 && ydTime.getMinute() < 30)) {
            withdrawalSuccess.setSuccess(false);
            withdrawalSuccess.setMessage("Withdrawal time: 9:30 to 24:00!");// on working days
            iSession.sendMessageByID(withdrawalSuccess, withdrawals.getConnId());
            logger.error("提现失败，未到提现时间，playerId：{}, time:{}", withdrawals.getPlayerId(), ydTime.getHour());
            return;
        }

        if (StringUtils.isBlank(withdrawals.getPassword())) {
            withdrawalSuccess.setSuccess(false);
            withdrawalSuccess.setMessage("Please enter the password");
            iSession.sendMessageByID(withdrawalSuccess, withdrawals.getConnId());
            logger.error("提现失败，提现密码为空，playerId：{}", withdrawals.getPlayerId());
            return;
        }

        double amount = withdrawals.getAmount();
        if (amount < 100) {
            withdrawalSuccess.setSuccess(false);
            withdrawalSuccess.setMessage("The amount must be greater than 100");
            iSession.sendMessageByID(withdrawalSuccess, withdrawals.getConnId());
            logger.error("提现失败，提现金额小于100，playerId：{}，amount:{}", withdrawals.getPlayerId(), amount);
            return;
        }
        if (amount % 50 != 0) {
            withdrawalSuccess.setSuccess(false);
            withdrawalSuccess.setMessage("You can only enter multiples of 50");
            iSession.sendMessageByID(withdrawalSuccess, withdrawals.getConnId());
            logger.error("提现失败，提现金额不是50的倍数，playerId：{}，amount:{}", withdrawals.getPlayerId(), amount);
            return;
        }

        PlayerData playerData = PlayerCache.getInstance().getPlayer(withdrawals.getConnId());
        if (playerData == null) {
            withdrawalSuccess.setSuccess(false);
            withdrawalSuccess.setMessage("The special player is not exist");
            iSession.sendMessageByID(withdrawalSuccess, withdrawals.getConnId());
            logger.error("提现失败，玩家不在线，playerId：{}，amount:{}", withdrawals.getPlayerId(), amount);
            return;
        }
//        if (playerData.getRoomId() != 0) {
//            withdrawalSuccess.setSuccess(false);
//            withdrawalSuccess.setMessage("No withdrawal is allowed in the game");
//            iSession.sendMessageByID(withdrawalSuccess, withdrawals.getConnId());
//            logger.error("提现失败，游戏房间中不允许提现，playerId：{}，amount:{}", playerData.getPlayerId(), amount);
//            return;
//        }

        //查询提现次数
        String key = WITHDRAWAL + playerData.getPlayerId() + ":" + sdf.format(new Date());
        Long withdrawalCount = MallCache.getInstance().getWithdrawalCount(key);
        if (withdrawalCount != null && withdrawalCount >= 2) {
            withdrawalSuccess.setSuccess(false);
            withdrawalSuccess.setMessage("Today`s the number of withdrawals has reached the upper limit");
            iSession.sendMessageByID(withdrawalSuccess, withdrawals.getConnId());
            logger.error("提现失败，今日提现次数已达上限，playerId：{}，amount:{}", playerData.getPlayerId(), amount);
            return;
        }

        Player player = PlayerService.getInstance().queryByPlayerId(playerData.getPlayerId());
        if (player.getIsVir() == 1 || player.getOrderAmount() <= 0) {
            withdrawalSuccess.setSuccess(false);
            withdrawalSuccess.setMessage("Virtual account cannot be withdraw");
            iSession.sendMessageByID(withdrawalSuccess, withdrawals.getConnId());
            logger.error("提现失败，虚拟账户无法提现，playerId：{}，amount:{}, balance:{}", player.getId(), amount, player.getZjPoint());
            return;
        }
        if (player.getZjPoint() < amount) {
            withdrawalSuccess.setSuccess(false);
            withdrawalSuccess.setMessage("Insufficient account balance");
            iSession.sendMessageByID(withdrawalSuccess, withdrawals.getConnId());
            logger.error("提现失败，账户余额不足，playerId：{}，amount:{}, balance:{}", player.getId(), amount, player.getZjPoint());
            return;
        }

        PlayerAccount playerAccount = PlayerAccountService.getInstance().findByPlayerId(player.getId());
        String password = DigestUtils.md5Hex(withdrawals.getPassword());
        if (!playerAccount.getPassword().equals(password)) {
            withdrawalSuccess.setSuccess(false);
            withdrawalSuccess.setMessage("Please enter the withdrawal password");
            iSession.sendMessageByID(withdrawalSuccess, withdrawals.getConnId());
            logger.error("提现失败，密码错误，playerId：{}，password:{}", withdrawals.getPlayerId(), withdrawals.getPassword());
            return;
        }

        //校验通过，扣钱
        double coin = 0 - amount;
        //数据库
        int row = PlayerService.getInstance().updatePlayerZjPoint(coin, player.getId());
        if (row == 0) {
            withdrawalSuccess.setSuccess(false);
            withdrawalSuccess.setMessage("Insufficient balance");
            iSession.sendMessageByID(withdrawalSuccess, withdrawals.getConnId());
            logger.error("提现失败，余额不足，playerId：{}，amount:{}", withdrawals.getPlayerId(), amount);
            return;
        }

        //缓存
        playerData.setZjPoint(player.getZjPoint() - amount);
        PlayerCache.getInstance().addPlayer(withdrawals.getConnId(), playerData);

        com.ydqp.common.entity.PlayerWithdrawal withdrawal = new com.ydqp.common.entity.PlayerWithdrawal();
        withdrawal.setPlayerId(player.getId());
        withdrawal.setName(playerAccount.getName());
        withdrawal.setAccNo(playerAccount.getAccNo());
        withdrawal.setIfsc(playerAccount.getIfsc());
        withdrawal.setMobile(playerAccount.getMobile());
        withdrawal.setAmount(amount);
        withdrawal.setStatus(0);
        String orderId = UUID.randomUUID().toString().replace("-", "");
        withdrawal.setTransferId(orderId);
        withdrawal.setReferenceId(orderId);
        withdrawal.setCreateTime(new Long(System.currentTimeMillis() / 1000).intValue());
        withdrawal.setAppId(player.getAppId());
        withdrawal.setRegisterTime(player.getCreateTime());
        withdrawal.setKfId(player.getKfId());

        //二次校验
        Player checkPlayer = PlayerService.getInstance().queryByPlayerId(playerData.getPlayerId());
        if (checkPlayer.getZjPoint() < 0) {
            withdrawal.setStatus(2);
            withdrawal.setErrorMsg("Account amount is abnormal");
            PlayerWithdrawalService.getInstance().savePlayerWithdrawal(withdrawal.getParameterMap());

            //通知客户端
            CoinPointSuccess coinPointSuccess = new CoinPointSuccess();
            coinPointSuccess.setCoinPoint(player.getZjPoint() - amount);
            coinPointSuccess.setPlayerId(player.getId());
            iSession.sendMessageByID(coinPointSuccess, withdrawals.getConnId());

            withdrawalSuccess.setSuccess(false);
            withdrawalSuccess.setMessage("Account amount is abnormal");
            iSession.sendMessageByID(withdrawalSuccess, withdrawals.getConnId());
            return;
        }

        //渠道
        List<PayWithdrawalConfig> configs = PayWithdrawalConfigService.getInstance().getAllEnableChannel();
        if (CollectionUtils.isNotEmpty(configs)) {
            PayWithdrawalConfig config = PayUtil.getInstance().getWithdrawalConfig(configs, playerData.getPlayerId());
            withdrawal.setPayChannel(config.getAliasName());

            String message = "Withdrawal application has been submitted, awaiting review";
            if (config.getWithdrawalAudit() == 0) {
                OrderPay orderPay = new CommonPay().getOrderPay(config.getName());
                if (orderPay == null) {
                    withdrawal.setStatus(2);
                    withdrawal.setErrorMsg("Unable to process payment request");
                    withdrawalSuccess.setSuccess(false);
                    withdrawalSuccess.setMessage("Unable to process payment request");
                } else {
                    withdrawalSuccess = orderPay.payout(withdrawal, config, playerAccount);
                    if (!withdrawalSuccess.isSuccess()) {
                        //失败加钱
                        //数据库
                        PlayerService.getInstance().updatePlayerZjPoint(amount, player.getId());
                        //缓存
                        playerData.setZjPoint(player.getZjPoint());
                        PlayerCache.getInstance().addPlayer(withdrawals.getConnId(), playerData);
                        //提现订单
                        PlayerWithdrawalService.getInstance().savePlayerWithdrawal(withdrawal.getParameterMap());

                        iSession.sendMessageByID(withdrawalSuccess, withdrawals.getConnId());
                        logger.error("提现失败，CashFree支付失败，playerId:{}, amount:{}，message:{}", player.getId(), amount, message);
                        return;
                    }
                }
            }

            //提现次数加一
            if (withdrawal.getStatus() != 2) {
                MallCache.getInstance().incrWithdrawalCount(key);
                PlayerWithdrawalService.getInstance().updateWithdrawAmount(withdrawals.getAmount(), player.getId());
            }
            //通知客户端
            CoinPointSuccess coinPointSuccess = new CoinPointSuccess();
            coinPointSuccess.setCoinPoint(player.getZjPoint() - amount);
            coinPointSuccess.setPlayerId(player.getId());
            iSession.sendMessageByID(coinPointSuccess, withdrawals.getConnId());

            PlayerWithdrawalService.getInstance().savePlayerWithdrawal(withdrawal.getParameterMap());

            withdrawalSuccess.setSuccess(true);
            withdrawalSuccess.setMessage(message);
            iSession.sendMessageByID(withdrawalSuccess, withdrawals.getConnId());
            //数据上报
//            ThreadManager.getInstance().getStatUploadExecutor().execute(new StatisticsUploadTask(UpLoadConstant.WITHDRAWAL, new JSONObject(withdrawal.getParameterMap())));
        } else {
            withdrawalSuccess.setSuccess(false);
            withdrawalSuccess.setMessage("No payment channel");
            logger.error("提现失败，支付渠道配置信息不存在");
            iSession.sendMessageByID(withdrawalSuccess, withdrawals.getConnId());
        }
    }
}
