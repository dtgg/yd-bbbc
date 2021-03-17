package com.ydqp.lobby.pay.razorpay;

import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.util.StackTraceUtil;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.ydqp.common.entity.*;
import com.ydqp.common.sendProtoMsg.mall.PlayerOrderSuccess;
import com.ydqp.common.sendProtoMsg.mall.PlayerWithdrawalSuccess;
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
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
    public PlayerOrderSuccess payment(PlayerOrder order, PayChannelConfig config) {
        PlayerOrderSuccess orderSuccess = new PlayerOrderSuccess();
        try {
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", order.getAmount()  * 100);
            orderRequest.put("currency", "INR");
            orderRequest.put("payment_capture", true);
            orderRequest.put("receipt", "order_rcptid");

            RazorpayClient razorpayClient = new RazorpayClient(config.getAppId(), config.getSecretKey());
            logger.info("请求razorpay创建订单：{}", orderRequest.toString());
            Order razorpayOrder = razorpayClient.Orders.create(orderRequest);
            logger.info("razorpay订单创建结果：{}", order.toString());
            JSONObject razorPayResult = new JSONObject(razorpayOrder);
            if ("created".equals(razorPayResult.getString("status"))) {
//                        String paymentUrl = payChannelConfig.getPaymentUrl() + "appId=" + payChannelConfig.getAppId() + "&orderId=" + razorPayResult.getString("id");
                JSONObject razorResp = new JSONObject() {{
                    put("appId", config.getAppId());
                    put("orderId", razorPayResult.getString("id"));
                    put("orderCurrency", "INR");
                    put("orderAmount", order.getAmount() * 100);
                    put("notifyUrl", config.getPaymentNotifyUrl());
                }};
                orderSuccess.setSuccess(true);
                orderSuccess.setPayChannel("razorpay");
                orderSuccess.setData(razorResp.toString());
                order.setTxnOrderId(razorPayResult.getString("id"));
            } else {
                orderSuccess.setSuccess(false);
                orderSuccess.setMessage(razorPayResult.getString("message"));
            }
        } catch (RazorpayException e) {
            logger.error("razorpay payment异常：{}", StackTraceUtil.getStackTrace(e));

            orderSuccess.setSuccess(false);
            orderSuccess.setMessage("Recharge failed");
            return orderSuccess;
        }
        return orderSuccess;
    }

    @Override
    public PlayerWithdrawalSuccess payout(PlayerWithdrawal withdrawal, PayWithdrawalConfig config, PlayerAccount account) {
        PlayerWithdrawalSuccess withdrawalSuc = new PlayerWithdrawalSuccess();

        //判断下faId存不存在
        String faId = account.getAccountId();
        if (StringUtils.isBlank(faId)) {
            JSONObject faParamJson = new JSONObject();
            faParamJson.put("clientId", config.getClientId());
            faParamJson.put("clientSecret", config.getClientSecret());
            faParamJson.put("playerId", withdrawal.getPlayerId());
            faParamJson.put("name", account.getName());
            faParamJson.put("accNo", account.getAccNo());
            faParamJson.put("ifsc", account.getIfsc());
            String fundStr = getFaId(config, account);
            JSONObject faJson = new JSONObject(fundStr);
            if (!faJson.getBoolean("success")) {
                withdrawalSuc.setSuccess(false);
                withdrawalSuc.setMessage(faJson.getString("message"));
                return withdrawalSuc;
            }
            faId = faJson.getString("faId");
            PlayerAccountService.getInstance().updateFaId(faId, withdrawal.getPlayerId());
        }

        BigDecimal am = new BigDecimal(String.valueOf(withdrawal.getAmount()));
        BigDecimal amount = am.multiply(BigDecimal.ONE.subtract(config.getWithdrawFee())).setScale(2, RoundingMode.DOWN);
        //payout
        String finalFaId = faId;
        JSONObject json = new JSONObject() {{
            put("account_number", config.getBusinessAccount());
            put("fund_account_id", finalFaId);
            put("amount", amount.floatValue() * 100);
            put("currency", "INR");
            put("mode", "IMPS");
            put("purpose", "payout");
        }};
        logger.info("razorpay payout请求参数：{}", json.toString());
        String payoutStr = RazorPayApi.getInstance().payout(getAuthMap(config), json.toString());
        logger.info("razorpay payout返回数据：{}", payoutStr);

        JSONObject payoutJson = new JSONObject(payoutStr);

        boolean success = true;
        String errorMsg = "";
        if (payoutJson.get("error") != null) {
            JSONObject error = payoutJson.getJSONObject("error");
            success = false;
            errorMsg = error.getString("description");
        } else {
            withdrawal.setReferenceId(payoutJson.getString("id"));
            if (!PAY_STATUS.contains(payoutJson.getString("status"))) {
                success = false;
                errorMsg = payoutJson.getString("failure_reason");
            }
        }
        if (!success) {
            withdrawalSuc.setSuccess(false);
            withdrawalSuc.setMessage(errorMsg);
            return withdrawalSuc;
        }

        withdrawalSuc.setSuccess(true);
        return withdrawalSuc;
    }

    private Map<String, String> getAuthMap(PayWithdrawalConfig config) {
        Map<String, String> authMap = new HashMap<>();
        authMap.put("keyId", config.getClientId());
        authMap.put("keySecret", config.getClientSecret());
        return authMap;
    }

    public String getFaId(PayWithdrawalConfig config, PlayerAccount account) {

        JSONObject json = new JSONObject();
        String contactName = account.getName().replaceAll(" ", "") + account.getPlayerId();
        if (contactName.length() > 50) contactName = contactName.substring(0, 50);

        json.put("name", contactName);
        logger.info("razorpay create contact, name: {}", contactName);
        String contactStr = RazorPayApi.getInstance().createContact(getAuthMap(config), json.toString());
        logger.info("razorpay create contact, resp: {}", contactStr);
        JSONObject contactJson = new JSONObject(contactStr);
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
            put("name", account.getName());
            put("ifsc", account.getIfsc());
            put("account_number", account.getAccNo());
        }});
        logger.info("razorpay create fund, param: {}", fundParam.toString());
        String fundStr = RazorPayApi.getInstance().createFund(getAuthMap(config), fundParam.toString());
        logger.info("razorpay create fund, resp: {}", fundStr);
        JSONObject fundJson = new JSONObject(fundStr);
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
