package com.ydqp.lobby.pay.yaarpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.ydqp.lobby.pay.OrderPay;
import com.ydqp.lobby.pay.yaarpay.api.YaarPayApi;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;

public class YaarPay extends OrderPay {
    private static final Logger logger = LoggerFactory.getLogger(YaarPay.class);

    @Override
    public String payment(String params) {
        JSONObject data = JSONObject.parseObject(params);
        HashMap<String, String> param = new HashMap<String, String>() {{
            put("amount", data.getIntValue("amount") * 100 + "");
            put("appId", data.getString("clientId"));
            put("channelId", "8035");
            put("currency", "inr");
            put("mchId", data.getString("mchId"));
            put("mchOrderNo", data.getString("orderId"));
            put("notifyUrl", data.getString("notifyUrl"));
            put("returnUrl", data.getString("returnUrl"));
            put("version", "1.0");
//            put("depositName", "Smith");
        }};
        logger.info("Yaarpay payment params:{}", JSON.toJSONString(param));
        return YaarPayApi.getInstance().payment(param, data.getString("secretKey"));
//        return null;
    }

    @Override
    public String payout(String params) {
        if (StringUtils.isBlank(params)) return null;
        JSONObject data = JSONObject.parseObject(params);

        HashMap<String, String> param = new HashMap<String, String>() {{
            put("accountName", data.getString("name"));
            put("accountNo", data.getString("accNo"));
            put("amount", data.getIntValue("amount") * 100 + "");
            put("mchId", data.getString("mchId"));
            put("mchOrderNo", data.getString("transferId"));
            put("notifyUrl", data.getString("notifyUrl"));
            put("payoutBankCode", data.getString("payoutBankCode"));
            put("reqTime", System.currentTimeMillis() + "");
            put("ifscCode", data.getString("ifsc"));
        }};

        String payout;
        try {
            logger.info("Yaarpay payout params:{}", JSON.toJSONString(param));
            payout = YaarPayApi.getInstance().payout(param, data.getString("clientSecret"));
            logger.info("Yaarpay payout response:{}", payout);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Yaarpay payout request error:{}", e.getMessage());
            JSONObject resp = new JSONObject() {{
                put("success", false);
                put("data", data);
            }};
            return resp.toJSONString();
        }

        data.remove("clientId");
        data.remove("clientSecret");
        data.remove("notifyUrl");
        data.remove("payoutBankCode");
        data.remove("mchId");

        JSONObject result = JSON.parseObject(payout);
        JSONObject resp= new JSONObject() {{
            put("success", result.getInteger("status") != null && result.getInteger("status") != 3);
            put("data", data);
        }};
        return resp.toJSONString();
    }
}
