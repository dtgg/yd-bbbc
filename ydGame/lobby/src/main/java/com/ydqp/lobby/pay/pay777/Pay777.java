package com.ydqp.lobby.pay.pay777;

import com.alibaba.fastjson.JSONObject;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.ydqp.common.entity.*;
import com.ydqp.common.sendProtoMsg.mall.PlayerOrderSuccess;
import com.ydqp.common.sendProtoMsg.mall.PlayerWithdrawalSuccess;
import com.ydqp.common.utils.HttpUtils;
import com.ydqp.lobby.pay.CommonPay;
import com.ydqp.lobby.pay.OrderPay;
import com.ydqp.lobby.pay.PayUrlUtil;
import org.apache.commons.codec.digest.DigestUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Pay777 extends OrderPay {
    private static final Logger logger = LoggerFactory.getLogger(Pay777.class);

    @Override
    public PlayerOrderSuccess payment(PlayerOrder order, PayChannelConfig config) {
        Map<String, String> header = new HashMap<String, String>() {{
            put("Content-Type", "application/json");
        }};

        JSONObject params = new JSONObject();
        params.put("userid", config.getMchId());
        params.put("orderid", order.getOrderId());
        params.put("type", "upi");

        Integer amount = Double.valueOf(order.getAmount()).intValue();
        params.put("amount", amount);
        params.put("notifyurl", config.getPaymentNotifyUrl());
//        params.put("notifyurl", "http://whw.ngrok2.xiaomiqiu.cn/api/777pay/notify");
        params.put("returnurl", "https://nrly.paysempbf.com/api/pay/success");

        String signStr = config.getSecretKey() + order.getOrderId() + amount;
        logger.info("signStr:{}", signStr);
        String sign = DigestUtils.md5Hex(signStr);
        params.put("sign", sign);

        String proxy = PayUrlUtil.getInstance().getUrl("isDebug");

        logger.info("777pay payment params:{}", params.toJSONString());
        String result = HttpUtils.getInstance().sendPost("https://api.zf77777.org/api/create", header, params, Boolean.parseBoolean(proxy));
        logger.info("777pay payment response:{}", result);

        JSONObject response = JSONObject.parseObject(result);

        PlayerOrderSuccess playerOrderSuccess = new PlayerOrderSuccess();
        if (response.getInteger("success") == 0) {
            order.setStatus(2);
            order.setMsg(response.getString("message"));

            playerOrderSuccess.setSuccess(false);
            playerOrderSuccess.setMessage("Recharge failed!");
        }

        order.setTxnOrderId(response.getString("ticket"));

        playerOrderSuccess.setSuccess(true);
        playerOrderSuccess.setPayChannel("url");
        JSONObject data = new JSONObject();
        data.put("url", response.getString("pageurl"));
        playerOrderSuccess.setData(data.toJSONString());
        return playerOrderSuccess;
    }

    @Override
    public PlayerWithdrawalSuccess payout(PlayerWithdrawal withdrawal, PayWithdrawalConfig config, PlayerAccount account) {
        Map<String, String> header = new HashMap<String, String>() {{
            put("Content-Type", "application/json");
        }};

        JSONObject params = new JSONObject();
        params.put("userid", config.getMchId());
        params.put("orderid", withdrawal.getTransferId());
        params.put("type", "bank");

        BigDecimal am = new BigDecimal(String.valueOf(withdrawal.getAmount()));
        BigDecimal amount = am.multiply(BigDecimal.ONE.subtract(config.getWithdrawFee())).setScale(0, RoundingMode.DOWN);
        params.put("amount", amount.intValue());
        params.put("notifyurl", config.getPayoutNotifyUrl());
//        params.put("notifyurl", "http://whw.ngrok2.xiaomiqiu.cn/api/777pay/notify");

        String signStr = config.getSecretKey() + withdrawal.getTransferId() + amount.intValue();
        logger.info("signStr:{}", signStr);
        String sign = DigestUtils.md5Hex(signStr);
        params.put("sign", sign);

        JSONObject payload = new JSONObject();
        payload.put("cardname", account.getName());
        payload.put("cardno", account.getAccNo());
        payload.put("ifsc", account.getIfsc());
        payload.put("bankid", "1000");
        payload.put("bankname", account.getBankName());
        params.put("payload", payload.toJSONString());

        String proxy = PayUrlUtil.getInstance().getUrl("isDebug");

        logger.info("777pay payout params:{}", params.toJSONString());
        String result = HttpUtils.getInstance().sendPost("https://api.zf77777.org/api/withdrawal", header, params, Boolean.parseBoolean(proxy));
        logger.info("777pay payout response:{}", result);

        JSONObject response = JSONObject.parseObject(result);

        PlayerWithdrawalSuccess playerWithdrawalSuccess = new PlayerWithdrawalSuccess();
        if (response.getInteger("success") == 0) {
            withdrawal.setStatus(2);
            withdrawal.setErrorMsg(response.getString("message"));

            playerWithdrawalSuccess.setSuccess(false);
            playerWithdrawalSuccess.setMessage("Withdrawal failed!");
        }

        withdrawal.setReferenceId(response.getString("ticket"));
        withdrawal.setStatus(1);

        playerWithdrawalSuccess.setSuccess(true);
        return playerWithdrawalSuccess;
    }

    public static void main(String[] args) {
//        System.out.println(DigestUtils.md5Hex("512b2e1d-0173-415a-95bd-e3585fc75fb6"+"20200101123456-A001" + "3000"));

        PayChannelConfig config = new PayChannelConfig();
        config.setName("777pay");
        config.setMchId("fz2166 ");
        config.setSecretKey("df8f9c5b-5dad-47d8-8387-a072b5f3ff2e");
        config.setWithdrawFee(new BigDecimal("0.18"));

        PlayerOrder order = new PlayerOrder();
        order.setOrderId(UUID.randomUUID().toString().replace("-", ""));
        order.setAmount(100);

        new CommonPay().getOrderPay(config.getName()).payment(order, config);

//        PlayerWithdrawal withdrawal = new PlayerWithdrawal();
//        withdrawal.setTransferId(UUID.randomUUID().toString().replace("-", ""));
//        withdrawal.setAmount(100);
//
//        PlayerAccount account = new PlayerAccount();
//        account.setName("zhangsan");
//        account.setAccNo("123456");
//        account.setIfsc("111");
//        new CommonPay().getOrderPay(config.getName()).payout(withdrawal, config, account);
    }
}
