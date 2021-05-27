package com.ydqp.lobby.pay.payplus;

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
import com.ydqp.lobby.utils.RSAUtils;
import com.ydqp.lobby.utils.SHA256WithRSAUtils;
import org.apache.commons.codec.digest.DigestUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * -----BEGIN PUBLIC KEY-----
 * MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA3ZutgeQDyv1OrtNLjXUNVALcmOFTyNKMn4v8EhXQSr5SgLf/Rmv4unpc0hqeDfZzL1Apk1vOhGn5tM27NNDXbo3eigNP38WMrlRk7LC1jdOIV2kOPjHROIX0smOBbk1sAm7Bksz6OvDhjreVomoPVU/CtVq2K35h0bKiPA88SOBC3iqwy7gPz8xbLTxbi/bODfMXerqntNzqVGp9MPxS3VRc3gBanP1ruEPiuadDCmot8GY073xQ0YFkZcvoID2uUODWRE1EGRm9DWYv7y0QiwCQOL7Vt2BSJHM2FadTyFae0y8Q4v7xGiquk7QWHqmjfFcEXPU/mTFVIkb0ApHLIQIDAQAB
 * -----END PUBLIC KEY-----
 * <p>
 * -----BEGIN PRIVATE KEY-----
 * MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDdm62B5APK/U6u00uNdQ1UAtyY4VPI0oyfi/wSFdBKvlKAt/9Ga/i6elzSGp4N9nMvUCmTW86Eafm0zbs00Ndujd6KA0/fxYyuVGTssLWN04hXaQ4+MdE4hfSyY4FuTWwCbsGSzPo68OGOt5Wiag9VT8K1WrYrfmHRsqI8DzxI4ELeKrDLuA/PzFstPFuL9s4N8xd6uqe03OpUan0w/FLdVFzeAFqc/Wu4Q+K5p0MKai3wZjTvfFDRgWRly+ggPa5Q4NZETUQZGb0NZi/vLRCLAJA4vtW3YFIkczYVp1PIVp7TLxDi/vEaKq6TtBYeqaN8VwRc9T+ZMVUiRvQCkcshAgMBAAECggEAAXsm37VcaSDkQ7rZyGo92o/iVYs7+h2eHvrahmxq5fT7rCyOojPah7SFpknTQJijEG/itz9n0+65YoH1fFbg+bYtBJMdF4vEcu3Rdtt/eyEsyhjDpZrDoTMT/ZU4qjoOPL3swURpb7doaoHDmEBJNfLAhGHE0emYUcR13Rzk9D+4Q5GaoV1xDi0EO5W5WBjUg5F/6LGJgB9MGl98jsbmClaHgq/MxQ2fXEGfiQbG8+pgC+Bi6r3FeeppmkXYuZfk2UjaTTmG2GSOmoyeBkmkdcD3eIQGao9tRIdWDgGyCd0G/Dt6NG8FGRFfPs1HGN7lbIGEsrHTb+q9PzfGFNgWAQKBgQDxlWHqEadVwNNwvXI2perOB/oFmQiqrrMNuYX1aBnva7FBqRIi0WpcaWSEMzFRx1s5ZS6hjl8roGDUs7335ed3eY6iIyBEJKzizn0y6UAR46KYo1kyUmz1/8YGNz5FDr09qKIWRM1gF/79pP7szrUSttwUctbZTS4yY0FTN49gUQKBgQDq1SKrRkwY940iMdxN6p7uQZTYfX1FsrOqiG3sKQ5UsZisnV7BO5B67Bsbg/Cxu9pf8KmrRKgfh7HK6VEnfvFtF8mtp7SOnRpxkAXgcH6tLiGijYJROcqMGKJTUkhM7+q1w2mnrKyUdsp8OmI5l0Phx0R2OCHUVBd/Hkd/s4hZ0QKBgQCu1HCbuFLlGDrsZ+1z5vbaS6OA0ZMYEn25/P0Y8FF0lvcckYJyeCME5bC8i1+x0xgNnU/10tbE97ebtshP0q2EJRA3/junlPMY4F5yraKUT6yn0e8HH+eCz6s7bFdAGr6RFKzYeMo4NKVwYiuqrJ0WAgebNNCPSe+Yv4PS+W5lQQKBgQDaUyGSXx7sr2p1eim5/wGXvT0nMTRAkm3bhE++GkWKlDq/cOfhewFOZyikojwq4d2bCOKVqpUsCzpOzjB3KdxHcrA8VQjW9lTZc+8ixTPgylzXBf08iyA2s8hm8r7pWLy8FFusWitxNCf460bbCDlCNcL+rh01jKDzvDD5Fu8ToQKBgQDKT2RqmzjPeGG8kdb92iNMejN/MJTzkoiyx9VVeWxDYmfYFDpFLptKHu3OmgY3RK4SEClvtOhYQ1t5NkvUNM5yQL/YBqGHiYK5XvkCn8as/1dJYY1q5V4jCnmK5VER/HqhutjwFXAnCeiCqC2Wx6teCY79BY8B3PrndIXCd9INBQ==
 * -----END PRIVATE KEY-----
 */
public class PayPlus extends OrderPay {
    private static final Logger logger = LoggerFactory.getLogger(PayPlus.class);
    private static final String HOST = "http://admin.payplus.cash/api";

    @Override
    public PlayerOrderSuccess payment(PlayerOrder order, PayChannelConfig config) {
        PlayerOrderSuccess playerOrderSuccess = new PlayerOrderSuccess();
        Map<String, String> header = new HashMap<String, String>() {{
            put("Content-Type", "application/json");
        }};

        Map<String, String> params = new HashMap<>();
        params.put("merId", config.getMchId());
        params.put("orderId", order.getOrderId());
        params.put("orderAmt", String.valueOf(Double.valueOf(order.getAmount()).intValue()));
        params.put("channel", "PAYU");
        params.put("desc", "desc");
        params.put("ip", "153.122.175.102");
        params.put("notifyUrl", config.getPaymentNotifyUrl());
//        params.put("notifyurl", "http://whw.ngrok2.xiaomiqiu.cn/api/payplus/notify");
        params.put("returnUrl", "https://www.google.com");
        params.put("nonceStr", UUID.randomUUID().toString().replaceAll("-", ""));

        String signStr = Md5Utils.sortMapAndSign(params, config.getBusinessAccount());
        logger.info("signStr:{}", signStr);
        String signUpper = DigestUtils.md5Hex(signStr).toUpperCase();
        logger.info("signStr to upper:{}", signUpper);
        try {
//            RSAUtils.RSAKeyPair keyPair = RSAUtils.generateKeyPair(config.getAppId(), config.getSecretKey());
//            String sign = RSAUtils.encryptByPrivateKey(keyPair.getPrivateKey(), signUpper);
            String sign = SHA256WithRSAUtils.buildRSASignByPrivateKey(signUpper, config.getSecretKey());
            params.put("sign", sign);
            logger.info("sign:{}", sign);
        } catch (Exception e) {
            e.printStackTrace();

            order.setStatus(2);
            order.setMsg("calculate sign error");

            playerOrderSuccess.setSuccess(false);
            playerOrderSuccess.setMessage("Recharge failed!");
            return playerOrderSuccess;
        }

        String proxy = PayUrlUtil.getInstance().getUrl("isDebug");
        logger.info("payplus payment params:{}", JSONObject.toJSONString(params));
        JSONObject jsonParams = JSONObject.parseObject(JSONObject.toJSONString(params));
        String result = HttpUtils.getInstance().post(HOST + "/pay", header, jsonParams, Boolean.parseBoolean(proxy));
        logger.info("payplus payment response:{}", result);

        JSONObject response = JSONObject.parseObject(result);
        if (response.getInteger("code") != 1) {
            order.setStatus(2);
            order.setMsg(response.getString("msg"));

            playerOrderSuccess.setSuccess(false);
            playerOrderSuccess.setMessage("Recharge failed!");
            return playerOrderSuccess;
        }

        JSONObject data = response.getJSONObject("data");
        if (data == null) {
            order.setStatus(2);
            order.setMsg("payurl is null");

            playerOrderSuccess.setSuccess(false);
            playerOrderSuccess.setMessage("Recharge failed!");
            return playerOrderSuccess;
        }

        order.setTxnOrderId(response.getString("sysorderno"));
        JSONObject urlData = new JSONObject();
        urlData.put("url", data.getString("payurl"));

        playerOrderSuccess.setSuccess(true);
        playerOrderSuccess.setPayChannel("url");
        playerOrderSuccess.setData(urlData.toJSONString());
        return playerOrderSuccess;
    }

    @Override
    public PlayerWithdrawalSuccess payout(PlayerWithdrawal withdrawal, PayWithdrawalConfig config, PlayerAccount account) {
        PlayerWithdrawalSuccess playerWithdrawalSuccess = new PlayerWithdrawalSuccess();
        Map<String, String> header = new HashMap<String, String>() {{
            put("Content-Type", "application/json");
        }};

        HashMap<String, String> params = new HashMap<>();
        params.put("merId", config.getMchId());
        params.put("orderId", withdrawal.getTransferId());
        BigDecimal am = new BigDecimal(String.valueOf(withdrawal.getAmount()));
//        BigDecimal amount = am.multiply(BigDecimal.ONE.subtract(config.getWithdrawFee())).setScale(2, RoundingMode.DOWN);
        params.put("money", am.toString());
        params.put("name", account.getName());
        params.put("ka", account.getAccNo());
        params.put("zhihang", account.getIfsc());
        params.put("bank", account.getBankName());
        params.put("notifyUrl", config.getPayoutNotifyUrl());
//        params.put("notifyurl", "http://whw.ngrok2.xiaomiqiu.cn/api/payplus/notify");
        params.put("nonceStr", UUID.randomUUID().toString().replaceAll("-", ""));

        String signStr = Md5Utils.sortMapAndSign(params, config.getBusinessAccount());
        logger.info("signStr:{}", signStr);
        String signUpper = DigestUtils.md5Hex(signStr).toUpperCase();
        logger.info("signStr to upper:{}", signUpper);
        try {
            String sign = SHA256WithRSAUtils.buildRSASignByPrivateKey(signUpper, config.getSecretKey());
            params.put("sign", sign);
            logger.info("sign:{}", params.get("sign"));
        } catch (Exception e) {
            e.printStackTrace();

            withdrawal.setStatus(2);
            withdrawal.setErrorMsg("calculate sign error");

            playerWithdrawalSuccess.setSuccess(false);
            playerWithdrawalSuccess.setMessage("Withdrawal failed!");
            return playerWithdrawalSuccess;
        }

        String proxy = PayUrlUtil.getInstance().getUrl("isDebug");
        logger.info("payplus payout params:{}", JSONObject.toJSONString(params));
        JSONObject jsonParams = JSONObject.parseObject(JSONObject.toJSONString(params));
        String result = HttpUtils.getInstance().post(HOST + "/pay/repay", header, jsonParams, Boolean.parseBoolean(proxy));
        logger.info("payplus payout response:{}", result);

        JSONObject response = JSONObject.parseObject(result);
        if (response.getInteger("code") != 1) {
            withdrawal.setStatus(2);
            withdrawal.setErrorMsg(response.getString("msg"));

            playerWithdrawalSuccess.setSuccess(false);
            playerWithdrawalSuccess.setMessage("Withdrawal failed!");
            return playerWithdrawalSuccess;
        }

        JSONObject data = response.getJSONObject("data");
        if (data == null) {
            withdrawal.setStatus(2);
            withdrawal.setErrorMsg("data is null");

            playerWithdrawalSuccess.setSuccess(false);
            playerWithdrawalSuccess.setMessage("Withdrawal failed!");
            return playerWithdrawalSuccess;
        }

//        withdrawal.setReferenceId(data.getString("ticket"));
        withdrawal.setStatus(1);

        playerWithdrawalSuccess.setSuccess(true);
        return playerWithdrawalSuccess;
    }

    public static void main(String[] args) {
//        PayChannelConfig config = new PayChannelConfig();
        PayWithdrawalConfig config = new PayWithdrawalConfig();
        config.setName("payplus");
        config.setMchId("20210511 ");
        config.setAppId("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA3ZutgeQDyv1OrtNLjXUNVALcmOFTyNKMn4v8EhXQSr5SgLf/Rmv4unpc0hqeDfZzL1Apk1vOhGn5tM27NNDXbo3eigNP38WMrlRk7LC1jdOIV2kOPjHROIX0smOBbk1sAm7Bksz6OvDhjreVomoPVU/CtVq2K35h0bKiPA88SOBC3iqwy7gPz8xbLTxbi/bODfMXerqntNzqVGp9MPxS3VRc3gBanP1ruEPiuadDCmot8GY073xQ0YFkZcvoID2uUODWRE1EGRm9DWYv7y0QiwCQOL7Vt2BSJHM2FadTyFae0y8Q4v7xGiquk7QWHqmjfFcEXPU/mTFVIkb0ApHLIQIDAQAB");
        config.setSecretKey("MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDdm62B5APK/U6u00uNdQ1UAtyY4VPI0oyfi/wSFdBKvlKAt/9Ga/i6elzSGp4N9nMvUCmTW86Eafm0zbs00Ndujd6KA0/fxYyuVGTssLWN04hXaQ4+MdE4hfSyY4FuTWwCbsGSzPo68OGOt5Wiag9VT8K1WrYrfmHRsqI8DzxI4ELeKrDLuA/PzFstPFuL9s4N8xd6uqe03OpUan0w/FLdVFzeAFqc/Wu4Q+K5p0MKai3wZjTvfFDRgWRly+ggPa5Q4NZETUQZGb0NZi/vLRCLAJA4vtW3YFIkczYVp1PIVp7TLxDi/vEaKq6TtBYeqaN8VwRc9T+ZMVUiRvQCkcshAgMBAAECggEAAXsm37VcaSDkQ7rZyGo92o/iVYs7+h2eHvrahmxq5fT7rCyOojPah7SFpknTQJijEG/itz9n0+65YoH1fFbg+bYtBJMdF4vEcu3Rdtt/eyEsyhjDpZrDoTMT/ZU4qjoOPL3swURpb7doaoHDmEBJNfLAhGHE0emYUcR13Rzk9D+4Q5GaoV1xDi0EO5W5WBjUg5F/6LGJgB9MGl98jsbmClaHgq/MxQ2fXEGfiQbG8+pgC+Bi6r3FeeppmkXYuZfk2UjaTTmG2GSOmoyeBkmkdcD3eIQGao9tRIdWDgGyCd0G/Dt6NG8FGRFfPs1HGN7lbIGEsrHTb+q9PzfGFNgWAQKBgQDxlWHqEadVwNNwvXI2perOB/oFmQiqrrMNuYX1aBnva7FBqRIi0WpcaWSEMzFRx1s5ZS6hjl8roGDUs7335ed3eY6iIyBEJKzizn0y6UAR46KYo1kyUmz1/8YGNz5FDr09qKIWRM1gF/79pP7szrUSttwUctbZTS4yY0FTN49gUQKBgQDq1SKrRkwY940iMdxN6p7uQZTYfX1FsrOqiG3sKQ5UsZisnV7BO5B67Bsbg/Cxu9pf8KmrRKgfh7HK6VEnfvFtF8mtp7SOnRpxkAXgcH6tLiGijYJROcqMGKJTUkhM7+q1w2mnrKyUdsp8OmI5l0Phx0R2OCHUVBd/Hkd/s4hZ0QKBgQCu1HCbuFLlGDrsZ+1z5vbaS6OA0ZMYEn25/P0Y8FF0lvcckYJyeCME5bC8i1+x0xgNnU/10tbE97ebtshP0q2EJRA3/junlPMY4F5yraKUT6yn0e8HH+eCz6s7bFdAGr6RFKzYeMo4NKVwYiuqrJ0WAgebNNCPSe+Yv4PS+W5lQQKBgQDaUyGSXx7sr2p1eim5/wGXvT0nMTRAkm3bhE++GkWKlDq/cOfhewFOZyikojwq4d2bCOKVqpUsCzpOzjB3KdxHcrA8VQjW9lTZc+8ixTPgylzXBf08iyA2s8hm8r7pWLy8FFusWitxNCf460bbCDlCNcL+rh01jKDzvDD5Fu8ToQKBgQDKT2RqmzjPeGG8kdb92iNMejN/MJTzkoiyx9VVeWxDYmfYFDpFLptKHu3OmgY3RK4SEClvtOhYQ1t5NkvUNM5yQL/YBqGHiYK5XvkCn8as/1dJYY1q5V4jCnmK5VER/HqhutjwFXAnCeiCqC2Wx6teCY79BY8B3PrndIXCd9INBQ==");
        config.setWithdrawFee(new BigDecimal("0.18"));
        config.setBusinessAccount("ChBrReGqdKgfaTcsXlvntWyDOPNbkpjS");
        config.setMchId("20210511");
        config.setPaymentNotifyUrl("http://whw.ngrok2.xiaomiqiu.cn/api/payplus/payment/notify");
        config.setPayoutNotifyUrl("http://whw.ngrok2.xiaomiqiu.cn/api/payplus/payout/notify");

        PlayerOrder order = new PlayerOrder();
        order.setOrderId(UUID.randomUUID().toString().replace("-", "").substring(0, 30));
        order.setAmount(100);

//        new CommonPay().getOrderPay(config.getName()).payment(order, config);

        PlayerWithdrawal withdrawal = new PlayerWithdrawal();
        withdrawal.setTransferId(UUID.randomUUID().toString().replace("-", "").substring(0, 30));
        withdrawal.setAmount(100);
//
        PlayerAccount account = new PlayerAccount();
        account.setName("zhangsan");
        account.setAccNo("4111111111111111");
        account.setIfsc("PYTM0123456");
        account.setBankName("PYTM");
        new CommonPay().getOrderPay(config.getName()).payout(withdrawal, config, account);
    }
}
