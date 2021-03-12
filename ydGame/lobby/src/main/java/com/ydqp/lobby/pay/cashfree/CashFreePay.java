package com.ydqp.lobby.pay.cashfree;

import com.alibaba.fastjson.JSONObject;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.ydqp.common.entity.*;
import com.ydqp.common.sendProtoMsg.mall.PlayerOrderSuccess;
import com.ydqp.common.sendProtoMsg.mall.PlayerWithdrawalSuc;
import com.ydqp.lobby.cache.PayCache;
import com.ydqp.lobby.pay.OrderPay;
import com.ydqp.lobby.pay.cashfree.api.CashFreeApi;
import com.ydqp.lobby.service.mall.PlayerAccountService;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class CashFreePay extends OrderPay {

    private static final Logger logger = LoggerFactory.getLogger(CashFreePay.class);

    private static final String PAYOUT_TOKEN = "cashFree_payout_token";

    @Override
    public PlayerOrderSuccess payment(PlayerOrder order, PayChannelConfig config) {
        Map<String, String> header = new HashMap<String, String>() {{
            put("X-Client-Id", config.getClientId());
            put("X-Client-Secret", config.getSecretKey());
            put("Content-Type", "application/json");
        }};
        JSONObject body = new JSONObject() {{
            put("orderId", order.getOrderId());
            put("orderAmount", order.getAmount());
            put("orderCurrency", "INR");
        }};
        logger.info("CashFree payment param: {}", body.toJSONString());
        String result = CashFreeApi.getInstance().createOrder(header, body);
        logger.info("CashFree payment response: {}", result);
        JSONObject cashFreeResult = JSONObject.parseObject(result);

        PlayerOrderSuccess orderSuccess = new PlayerOrderSuccess();
        if ("OK".equals(cashFreeResult.getString("status"))) {
            orderSuccess.setSuccess(true);

            JSONObject cashFreeResp = new JSONObject() {{
                put("token", cashFreeResult.getString("cftoken"));
                put("stage", "PROD");//PROD/TEST
                put("appId", config.getAppId());
                put("orderId", order.getOrderId());
                put("orderCurrency", "INR");
                put("orderAmount", order.getAmount());
                put("notifyUrl", config.getPaymentNotifyUrl());
            }};
            orderSuccess.setPayChannel("cashfree");
            orderSuccess.setData(cashFreeResp.toJSONString());
        } else {
            orderSuccess.setSuccess(false);
            orderSuccess.setMessage(cashFreeResult.getString("message"));

            order.setStatus(2);
            order.setMsg(cashFreeResult.getString("message"));
        }
        return orderSuccess;
    }

    @Override
    public PlayerWithdrawalSuc payout(PlayerWithdrawal withdrawal, PayWithdrawalConfig config, PlayerAccount account) {
        PlayerWithdrawalSuc withdrawalSuc = new PlayerWithdrawalSuc();

        JSONObject tokenJson = getToken(config);
        if (!tokenJson.getBoolean("success")) {
            withdrawalSuc.setSuccess(false);
            withdrawalSuc.setMessage(tokenJson.getString("data"));
            return withdrawalSuc;
        }

        String token = tokenJson.getString("data");

        //查询beneId
        boolean beneIdIsBlank = false;
        String beneId = account.getAccountId();
        if (StringUtils.isBlank(beneId)) {
            beneIdIsBlank = true;
            beneId = account.getName().replaceAll(" ", "") + account.getPlayerId();
        }
        if (beneId.length() > 50) beneId = beneId.substring(0, 50);
        boolean isExistBeneficiary = getBeneficiary(token, beneId);
        logger.info("beneId已绑定，beneId:{}", beneId);
        if (!isExistBeneficiary) {
            JSONObject beneficiaryJson = addBeneficiary(token, account);
            if (!beneficiaryJson.getBoolean("success")) {
                withdrawalSuc.setSuccess(false);
                withdrawalSuc.setMessage(tokenJson.getString("data"));
                return withdrawalSuc;
            }
        }
        if (beneIdIsBlank) {
            PlayerAccountService.getInstance().updateBeneId(beneId, withdrawal.getPlayerId());
        }

        BigDecimal amount = new BigDecimal(String.valueOf(withdrawal.getAmount()));
        BigDecimal f = amount.multiply(BigDecimal.ONE.subtract(config.getWithdrawFee())).setScale(2, RoundingMode.HALF_DOWN);
        JSONObject transferParams = new JSONObject() {{
            put("beneId", account.getAccountId());
            put("amount", f);
            put("transferId", withdrawal.getTransferId());
        }};
        JSONObject transferJson = requestTransferSync(token, transferParams);
        if (!transferJson.getBoolean("success")) {
            withdrawalSuc.setSuccess(false);
            withdrawalSuc.setMessage(tokenJson.getString("data"));
            return withdrawalSuc;
        }
        String referenceId = transferJson.getJSONObject("data").getString("referenceId");
        withdrawal.setReferenceId(referenceId);

        withdrawalSuc.setSuccess(true);
        return withdrawalSuc;
    }

    private JSONObject getToken(PayWithdrawalConfig config) {
        JSONObject result = new JSONObject();
        try {
            String authorize = CashFreeApi.getInstance().authorize(config.getClientId(), config.getClientSecret());
            logger.info("CashFree getToken response: {}", authorize);
            if (!CashFreeApi.getInstance().isSuccess(authorize)) {
                logger.error("authorize require error, key: {}, content: {}", PAYOUT_TOKEN, authorize);
                result.put("success", false);
                result.put("data", authorize.replaceAll("\n", ""));
                return result;
            }

            JSONObject data = CashFreeApi.getInstance().data(authorize);
            String token = data.getString("token");
            result.put("success", true);
            result.put("data", token);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("data", "Require cashFree token error");
        }
        return result;
    }

    private boolean getBeneficiary(String token, String beneId) {
        String beneficiary = CashFreeApi.getInstance().getBeneficiary(token, beneId);
        logger.info("查询beneficiary：{}", beneficiary);
        if (StringUtils.isBlank(beneficiary)) return false;

        JSONObject data;
        try {
            data = JSONObject.parseObject(beneficiary);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        String status = data.getString("status");
        return "SUCCESS".equals(status);
    }

    private JSONObject addBeneficiary(String token, PlayerAccount account) {
        JSONObject params = new JSONObject() {{
            put("beneId", account.getAccountId());
            put("name", account.getName());
            put("email", StringUtils.isBlank(account.getEmail()) ? "johndoe_1@cashfree.com" : account.getEmail());
            put("phone", account.getMobile());
            put("bankAccount", account.getAccNo());
            put("ifsc", account.getIfsc());
            put("address1", "Bangalore, Bengaluru (Bangalore) Urban, Karnataka");
        }};
        JSONObject result = new JSONObject();
        try {
            logger.info("CashFree addBeneficiary param: {}", params.toJSONString());
            String beneficiary = CashFreeApi.getInstance().addBeneficiary(token, params);
            logger.info("CashFree addBeneficiary response: {}", beneficiary);
            JSONObject jsonObject = JSONObject.parseObject(beneficiary);
            if ("SUCCESS".equals(jsonObject.getString("status"))
                    || "Beneficiary Id already exists".equals(jsonObject.getString("message"))) {
                result.put("success", true);
                result.put("data", account.getAccountId());

                PayCache.getInstance().addBeneId(account.getAccountId());
            } else {
                logger.error("addBeneficiary require error, data: {}", beneficiary);
                result.put("success", false);
                result.put("data", beneficiary.replaceAll("\n", ""));
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("data", "Require cashFree addBeneficiary error");
        }
        return result;
    }

    private JSONObject requestTransferSync(String token, JSONObject params) {
        JSONObject result = new JSONObject();
        try {
            logger.info("CashFree requestTransferSync param: {}", params.toJSONString());
            String requestTransfer = CashFreeApi.getInstance().requestTransfer(token, params);
            logger.info("CashFree requestTransferSync response: {}", requestTransfer);
            boolean status;
            if (StringUtils.isBlank(requestTransfer)) {
                status = false;
            } else {
                try {
                    JSONObject data = JSONObject.parseObject(requestTransfer);
                    String statusStr = data.getString("status");
                    status = !(StringUtils.isBlank(statusStr) || "ERROR".equals(statusStr));
                } catch (Exception e) {
                    e.printStackTrace();
                    status = false;
                }
            }

            if (!status) {
                logger.error("requestTransferSync require error, data: {}", requestTransfer);
                result.put("success", false);
                result.put("data", requestTransfer.replaceAll("\n", ""));

                //删除收款人
                JSONObject error = JSONObject.parseObject(requestTransfer);
                if ("422".equals(error.getString("subCode"))
                        || "Invalid bank account number or ifsc provided".equals(error.getString("message"))) {
                    PayCache.getInstance().remove(params.getString("beneId"));
                    String s = CashFreeApi.getInstance().removeBeneficiary(token, new JSONObject() {{
                        put("beneId", params.getString("beneId"));
                    }});
                    logger.info("Remove beneficiary result: {}", s);
                }
            } else {
                JSONObject object = JSONObject.parseObject(requestTransfer);
                result.put("success", true);
                result.put("data", object.getJSONObject("data"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("data", "Require cashFree requestTransferSync error");
        }
        return result;
    }
}
