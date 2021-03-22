package com.ydqp.lobby.pay.shineu;

import com.alibaba.fastjson.JSONObject;
import com.ydqp.common.entity.*;
import com.ydqp.common.sendProtoMsg.mall.PlayerOrderSuccess;
import com.ydqp.common.sendProtoMsg.mall.PlayerWithdrawalSuccess;
import com.ydqp.common.utils.HttpUtils;
import com.ydqp.lobby.pay.CommonPay;
import com.ydqp.lobby.pay.OrderPay;
import com.ydqp.lobby.pay.PayUrlUtil;
import com.ydqp.lobby.utils.RandomValueUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShineU extends OrderPay {

    private static final Logger logger = LoggerFactory.getLogger(ShineU.class);

    @Override
    public PlayerOrderSuccess payment(PlayerOrder order, PayChannelConfig config) {
        JSONObject params = new JSONObject();
        params.put("merchantId", config.getMchId());
        params.put("timestamp", String.valueOf(System.currentTimeMillis()));

        JSONObject body = new JSONObject();
        body.put("amount", order.getAmount());
        body.put("orderId", order.getOrderId());
        body.put("details", "Recharge");
        body.put("userId", String.valueOf(order.getPlayerId()));
        body.put("notifyUrl", config.getPaymentNotifyUrl());
        body.put("redirectUrl", "about:blank");
        params.put("body", body);

        String signStr = params.toJSONString() + "|" + config.getSecretKey();
        logger.info("signStr:{}", signStr);
        String sign = DigestUtils.md5Hex(signStr);

        Map<String, String> header = new HashMap<String, String>() {{
            put("Content-Type", "application/json");
            put("Api-Sign", sign);
        }};

        String proxy = PayUrlUtil.getInstance().getUrl("isDebug");

        logger.info("ShineU payment params:{}", params.toJSONString());
        String result = HttpUtils.getInstance().sendPost("https://gateway.shineupay.com/pay/create", header, params, Boolean.parseBoolean(proxy));
        logger.info("ShineU payment response:{}", result);

        PlayerOrderSuccess playerOrderSuccess = new PlayerOrderSuccess();

        JSONObject response = JSONObject.parseObject(result);
        if (response.getInteger("status") == 0) {
            Long timestamp = response.getLong("timestamp");
            if (System.currentTimeMillis() - timestamp < 2 * 60 * 1000) {
                JSONObject data = response.getJSONObject("body");
                order.setTxnOrderId(data.getString("transactionId"));

                playerOrderSuccess.setSuccess(true);
                playerOrderSuccess.setPayChannel("url");
                JSONObject urlData = new JSONObject();
                urlData.put("url", data.getString("content"));
                playerOrderSuccess.setData(urlData.toJSONString());
                return playerOrderSuccess;
            }
        }

        order.setStatus(2);
        order.setMsg("Recharge failed!");

        playerOrderSuccess.setSuccess(false);
        playerOrderSuccess.setMessage("Recharge failed!");
        return playerOrderSuccess;
    }

    @Override
    public PlayerWithdrawalSuccess payout(PlayerWithdrawal withdrawal, PayWithdrawalConfig config, PlayerAccount account) {
        JSONObject params = new JSONObject();
        params.put("merchantId", config.getMchId());
        params.put("timestamp", String.valueOf(System.currentTimeMillis()));

        JSONObject body = new JSONObject();
        body.put("advPasswordMd5", DigestUtils.md5Hex(config.getBusinessAccount()));
        body.put("orderId", withdrawal.getTransferId());
        body.put("flag", 0);
        body.put("bankUser", account.getName());
        body.put("bankCode", account.getAccNo());
        body.put("bankUserIFSC", account.getIfsc());
        body.put("bankUserPhone", account.getMobile());
        body.put("bankAddress", "icic");
        body.put("bankUserEmail", RandomValueUtil.getEmail(8, 10));

        BigDecimal am = BigDecimal.valueOf(withdrawal.getAmount());
        BigDecimal amount = am.multiply(BigDecimal.ONE.subtract(config.getWithdrawFee())).setScale(2, RoundingMode.DOWN);
        body.put("amount", amount);
        body.put("realAmount", amount);
        body.put("notifyUrl", config.getPayoutNotifyUrl());
        params.put("body", body);

        String signStr = params.toJSONString() + "|" + config.getClientSecret();
        String sign = DigestUtils.md5Hex(signStr);

        Map<String, String> header = new HashMap<String, String>() {{
            put("Content-Type", "application/json");
            put("Api-Sign", sign);
        }};

        String proxy = PayUrlUtil.getInstance().getUrl("isDebug");

        logger.info("ShineU payout params:{}", params.toJSONString());
        String result = HttpUtils.getInstance().sendPost("https://gateway.shineupay.com/withdraw/create", header, params, Boolean.parseBoolean(proxy));
        logger.info("ShineU payout response:{}", result);

        PlayerWithdrawalSuccess playerWithdrawalSuccess = new PlayerWithdrawalSuccess();

        JSONObject response = JSONObject.parseObject(result);
        if (response.getInteger("status") == 0) {
            withdrawal.setStatus(1);
            try {
                JSONObject json = response.getJSONObject("body");
                withdrawal.setReferenceId(json.getString("platformOrderId"));
            } catch (Exception e) {
                logger.error("ShineU json parse error:{}", e.getMessage());
            }

            playerWithdrawalSuccess.setSuccess(true);
            return playerWithdrawalSuccess;
        }

        withdrawal.setStatus(2);
        withdrawal.setErrorMsg(response.getString("message"));

        playerWithdrawalSuccess.setSuccess(false);
        return playerWithdrawalSuccess;
    }

    public static void main(String[] args) {

        PayChannelConfig config = new PayChannelConfig();
//        PayWithdrawalConfig config = new PayWithdrawalConfig();
        config.setName("shineu");
        config.setMchId("AFOSNS50BQPS5656");
        config.setSecretKey("b20ea60e8d8f4d5d91d1da924f460646");
        config.setClientSecret("b20ea60e8d8f4d5d91d1da924f460646");
        config.setWithdrawFee(new BigDecimal("0.18"));
        config.setPaymentNotifyUrl("http://whw.ngrok2.xiaomiqiu.cn/api/shineu/payment/notify");
        config.setPayoutNotifyUrl("http://whw.ngrok2.xiaomiqiu.cn/api/shineu/payout/notify");
        config.setBusinessAccount("AFSHI9D29Z408907");

        PlayerOrder order = new PlayerOrder();
        order.setOrderId(UUID.randomUUID().toString().replace("-", ""));
        order.setAmount(100);

        new CommonPay().getOrderPay(config.getName()).payment(order, config);

//        PlayerWithdrawal withdrawal = new PlayerWithdrawal();
//        withdrawal.setTransferId(UUID.randomUUID().toString().replace("-", ""));
//        withdrawal.setAmount(250);
//
//        PlayerAccount account = new PlayerAccount();
//        account.setName("zhangsan");
//        account.setMobile("9876543210");
//        account.setAccNo("00000000000");
//        account.setIfsc("011");
//        new CommonPay().getOrderPay(config.getName()).payout(withdrawal, config, account);
        //{"body":{"platformOrderId":"20210322AFSHSQ7YHB0G1761"},"status":0,"merchantId":"AFOSNS50BQPS5656","timestamp":"1616410969161"}
    }
}
