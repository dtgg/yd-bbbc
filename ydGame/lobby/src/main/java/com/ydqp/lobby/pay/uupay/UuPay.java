package com.ydqp.lobby.pay.uupay;

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
import com.ydqp.lobby.utils.Md5Utils;
import com.ydqp.lobby.utils.RandomValueUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author ：whw
 * @date ：Created in 2021/3/21 22:04
 * @description：uupay
 */
public class UuPay extends OrderPay {

    private static final Logger logger = LoggerFactory.getLogger(UuPay.class);

    @Override
    public PlayerOrderSuccess payment(PlayerOrder order, PayChannelConfig config) {
        Map<String, String> header = new HashMap<String, String>() {{
            put("Content-Type", "application/json");
        }};

        JSONObject params = new JSONObject();
        params.put("merchant_sn", config.getMchId());
        params.put("order_sn", order.getOrderId());
        params.put("amount", order.getAmount());
        params.put("name", RandomValueUtil.getName());
        params.put("email", RandomValueUtil.getEmail(8,10));
        params.put("phone", RandomValueUtil.getTelephone());
        params.put("remark", "Recharge");
        params.put("redirect_url", config.getPaymentNotifyUrl());
//        params.put("redirect_url", "https://nrly.paysempbf.com/api/pay/success");
        Map<String, String> map = new HashMap<>();
        params.forEach((k, v) -> {map.put(k, String.valueOf(v));});
        String sign = Md5Utils.sortMapAndSign(map, config.getSecretKey());
        logger.info("sign:{}", sign);
        params.put("sign", sign);

        String proxy = PayUrlUtil.getInstance().getUrl("isDebug");

        logger.info("uupay payment params:{}", params.toJSONString());
        String result = HttpUtils.getInstance().sendPost("http://uupay.cash/gateway/payin/", header, params, Boolean.parseBoolean(proxy));
        logger.info("uupay payment response:{}", result);

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
        params.put("merchant_sn", config.getMchId());
        params.put("order_sn", withdrawal.getTransferId());
        BigDecimal am = new BigDecimal(String.valueOf(withdrawal.getAmount()));
        BigDecimal amount = am.multiply(BigDecimal.ONE.subtract(config.getWithdrawFee())).setScale(0, RoundingMode.DOWN);
        params.put("amount", amount.intValue());
        params.put("type", "bank");
        params.put("name", account.getName());
        params.put("account", account.getAccNo());
        params.put("ifsc", account.getAccNo());
        params.put("remark", "Withdrawal");
        Map<String, String> map = new HashMap<>();
        params.forEach((k, v) -> map.put(k, String.valueOf(v)));
        String sign = Md5Utils.sortMapAndSign(map, config.getSecretKey());
        logger.info("sign:{}", sign);
        params.put("sign", sign);

        String proxy = PayUrlUtil.getInstance().getUrl("isDebug");

        logger.info("uupay payout params:{}", params.toJSONString());
        String result = HttpUtils.getInstance().sendPost("http://uupay.cash/gateway/payout/", header, params, Boolean.parseBoolean(proxy));
        logger.info("uupay payout response:{}", result);

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
        config.setName("uupay");
        config.setMchId("20210319-odxcm");
        config.setSecretKey("68932751466a4ce32fb5890b5ce685ee");
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
