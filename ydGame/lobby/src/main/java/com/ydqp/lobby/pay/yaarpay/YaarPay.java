package com.ydqp.lobby.pay.yaarpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.ydqp.common.entity.*;
import com.ydqp.common.sendProtoMsg.mall.PlayerOrderSuccess;
import com.ydqp.common.sendProtoMsg.mall.PlayerWithdrawalSuc;
import com.ydqp.lobby.pay.OrderPay;
import com.ydqp.lobby.pay.yaarpay.api.YaarPayApi;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;

public class YaarPay extends OrderPay {
    private static final Logger logger = LoggerFactory.getLogger(YaarPay.class);

    @Override
    public PlayerOrderSuccess payment(PlayerOrder order, PayChannelConfig config) {
        HashMap<String, String> param = new HashMap<String, String>() {{
            put("amount", order.getAmount() * 100 + "");
            put("appId", config.getAppId());
            put("channelId", "8035");
            put("currency", "inr");
            put("mchId", config.getMchId());
            put("mchOrderNo", order.getOrderId());
            put("notifyUrl", config.getPaymentNotifyUrl());
            put("returnUrl", "");
            put("version", "1.0");
//            put("depositName", "Smith");
        }};
        logger.info("Yaarpay payment params:{}", JSON.toJSONString(param));
        String result = YaarPayApi.getInstance().payment(param, config.getSecretKey());
        logger.info("Yaarpay payment response:{}", result);
        String orderId = order.getOrderId().substring(0, 30);
        order.setOrderId(orderId);

        PlayerOrderSuccess orderSuccess = new PlayerOrderSuccess();
        orderSuccess.setSuccess(true);
        orderSuccess.setPayChannel("yaarpay");
        orderSuccess.setData(config.getPaymentUrl() + "orderId=" + orderId);
        return orderSuccess;
//        return null;
    }

    @Override
    public PlayerWithdrawalSuc payout(PlayerWithdrawal withdrawal, PayWithdrawalConfig config, PlayerAccount account) {
        HashMap<String, String> param = new HashMap<String, String>() {{
            put("accountName", account.getName());
            put("accountNo", account.getAccNo());
            put("amount", withdrawal.getAmount() * 100 + "");
            put("mchId", config.getMchId());
            put("mchOrderNo", withdrawal.getTransferId());
            put("notifyUrl", config.getPayoutNotifyUrl());
            put("payoutBankCode", account.getIfsc());
            put("reqTime", System.currentTimeMillis() + "");
            put("ifscCode", account.getIfsc());
        }};

        PlayerWithdrawalSuc withdrawalSuc = new PlayerWithdrawalSuc();
        String payout;
        try {
            logger.info("Yaarpay payout params:{}", JSON.toJSONString(param));
            payout = YaarPayApi.getInstance().payout(param, config.getClientSecret());
            logger.info("Yaarpay payout response:{}", payout);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Yaarpay payout request error:{}", e.getMessage());
            withdrawalSuc.setSuccess(false);
            withdrawalSuc.setMessage("Withdrawal failed");
            return withdrawalSuc;
        }

//        JSONObject result = JSON.parseObject(payout);
//        JSONObject resp= new JSONObject() {{
//            put("success", result.getInteger("status") != null && result.getInteger("status") != 3);
//            put("data", data);
//        }};
        return withdrawalSuc;
    }
}
