package com.ydqp.lobby.pay.razorpay;

import com.alibaba.fastjson.JSONObject;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.util.StackTraceUtil;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.ydqp.lobby.pay.OrderPay;
import com.ydqp.lobby.pay.razorpay.api.RazorPayApi;
import com.ydqp.lobby.service.mall.PlayerAccountService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RazorPay extends OrderPay {
    private static final Logger logger = LoggerFactory.getLogger(RazorPay.class);

    private static final List<String> PAY_STATUS = new ArrayList<String>() {{
        add("queued");
        add("pending");
        add("processing");
        add("processed");
    }};

    @Override
    public String payment(String params) {
        Order order;
        try {
            JSONObject data = JSONObject.parseObject(params);

            org.json.JSONObject orderRequest = new org.json.JSONObject();
            orderRequest.put("amount", data.getDouble("amount")  * 100);
            orderRequest.put("currency", "INR");
            orderRequest.put("payment_capture", true);
            orderRequest.put("receipt", "order_rcptid");

            RazorpayClient razorpayClient = new RazorpayClient(data.getString("clientId"), data.getString("secretKey"));
            logger.info("请求razorpay创建订单：{}", orderRequest.toString());
            order = razorpayClient.Orders.create(orderRequest);
            logger.info("razorpay订单创建结果：{}", order.toString());
        } catch (RazorpayException e) {
            logger.error("razorpay payment异常：{}", StackTraceUtil.getStackTrace(e));

            JSONObject error = new JSONObject();
            error.put("status", "error");
            error.put("message", e.getMessage());
            return error.toString();
        }
        return order.toString();
    }

    @Override
    public String payout(String params) {
        if (params == null) return null;
        JSONObject data = JSONObject.parseObject(params);

        JSONObject resp = new JSONObject();

        //判断下faId存不存在
        String faId = data.getString("faId");
        if (StringUtils.isBlank(faId)) {
            JSONObject faParamJson = new JSONObject();
            faParamJson.put("clientId", data.getString("clientId"));
            faParamJson.put("clientSecret", data.getString("clientSecret"));
            faParamJson.put("playerId", data.getString("playerId"));
            faParamJson.put("name", data.getString("name"));
            faParamJson.put("accNo", data.getString("accNo"));
            faParamJson.put("ifsc", data.getString("ifsc"));
            String fundStr = getFaId(faParamJson.toJSONString());
            JSONObject faJson = JSONObject.parseObject(fundStr);
            if (!faJson.getBoolean("success")) {
                JSONObject error = new JSONObject();
                error.put("message", faJson.getString("message"));

                resp.put("success", false);
                resp.put("data", error);
                return resp.toString();
            }
            faId = faJson.getString("faId");

            PlayerAccountService.getInstance().updateFaId(faId, data.getLongValue("playerId"));
        }

        //payout
        String finalFaId = faId;
        JSONObject json = new JSONObject() {{
            put("account_number", data.getString("businessAccount"));
            put("fund_account_id", finalFaId);
            put("amount", data.getDouble("amount") * 100);
            put("currency", "INR");
            put("mode", "IMPS");
            put("purpose", "payout");
        }};
        logger.info("razorpay payout请求参数：{}", json.toString());
        String payoutStr = RazorPayApi.getInstance().payout(getAuthMap(data), json.toString());
        logger.info("razorpay payout返回数据：{}", payoutStr);

        JSONObject payoutJson = JSONObject.parseObject(payoutStr);

        boolean success = true;
        String errorMsg = "";
        if (payoutJson.get("error") != null) {
            JSONObject error = payoutJson.getJSONObject("error");
            success = false;
            errorMsg = error.getString("description");
        } else {
            data.put("transferId", payoutJson.getString("id"));
            if (!PAY_STATUS.contains(payoutJson.getString("status"))) {
                success = false;
                errorMsg = payoutJson.getString("failure_reason");
            }
        }
        if (!success) {
            JSONObject error = new JSONObject();
            error.put("message", errorMsg);

            resp.put("success", false);
            resp.put("data", error);
            return resp.toString();
        }

        data.remove("clientId");
        data.remove("clientSecret");
        data.remove("businessAccount");
        data.remove("faId");
        resp.put("success", true);
        resp.put("data", data);
        return resp.toString();
    }

    private Map<String, String> getAuthMap(JSONObject data) {
        Map<String, String> authMap = new HashMap<>();
        authMap.put("keyId", data.getString("clientId"));
        authMap.put("keySecret", data.getString("clientSecret"));
        return authMap;
    }

    public String getFaId(String str) {
        JSONObject data = JSONObject.parseObject(str);

        JSONObject json = new JSONObject();
        String contactName = data.getString("name").replaceAll(" ", "") + data.getLong("playerId");
        if (contactName.length() > 50) contactName = contactName.substring(0, 50);

        json.put("name", contactName);
        logger.info("razorpay create contact, name: {}", data.getString("name"));
        String contactStr = RazorPayApi.getInstance().createContact(getAuthMap(data), json.toString());
        logger.info("razorpay create contact, resp: {}", contactStr);
        JSONObject contactJson = JSONObject.parseObject(contactStr);
        if (contactJson.get("error") != null) {
            JSONObject error = contactJson.getJSONObject("error");
            return new JSONObject() {{
                put("success", false);
                put("message", error.getString("description"));
            }}.toString();
        }
        String contactId = contactJson.getString("id");

        JSONObject fundParam = new JSONObject();
        fundParam.put("contact_id", contactId);
        fundParam.put("account_type", "bank_account");
        fundParam.put("bank_account", new JSONObject() {{
            put("name", data.getString("name"));
            put("ifsc", data.getString("ifsc"));
            put("account_number", data.getString("accNo"));
        }});
        logger.info("razorpay create fund, param: {}", fundParam.toString());
        String fundStr = RazorPayApi.getInstance().createFund(getAuthMap(data), fundParam.toJSONString());
        logger.info("razorpay create fund, resp: {}", fundStr);
        JSONObject fundJson = JSONObject.parseObject(fundStr);
        if (fundJson.get("error") != null) {
            JSONObject error = fundJson.getJSONObject("error");
            return new JSONObject() {{
                put("success", false);
                put("message", error.getString("description"));
            }}.toString();
        }

        return new JSONObject() {{
            put("success", true);
            put("faId", fundJson.getString("id"));
        }}.toString();
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
//        JSONObject params = new JSONObject() {{
//            put("amount", 100);
//            put("clientId", "rzp_test_OkY5u5ej0ZWG3K");
//            put("secretKey", "nd4DqMsYICotXXDFRs1uiXMY");
//        }};
//        System.out.println(new RazorPay().payment(params.toString()));

        String authString = "rzp_live_TpGtOV6kOMiIrn:MpUsSnZnAPsvUM2dVAAXE0Oc";
        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes("utf-8"));
        String authStringEnc = new String(authEncBytes);
        System.out.println(authStringEnc);


        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials
                = new UsernamePasswordCredentials("rzp_test_OkY5u5ej0ZWG3K", "nd4DqMsYICotXXDFRs1uiXMY");
        provider.setCredentials(AuthScope.ANY, credentials);

        HttpClient client = HttpClientBuilder.create()
                .setDefaultCredentialsProvider(provider)
                .build();
    }
}
