package com.ydqp.lobby.pay.cashfree;

import com.alibaba.fastjson.JSONObject;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.ydqp.lobby.cache.PayCache;
import com.ydqp.lobby.pay.OrderPay;
import com.ydqp.lobby.pay.cashfree.api.CashFreeApi;
import com.ydqp.lobby.service.mall.PlayerAccountService;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class CashFreePayNew extends OrderPay {

    private static final Logger logger = LoggerFactory.getLogger(CashFreePay.class);

    private static final String PAYOUT_TOKEN = "cashFree_payout_token";

    @Override
    public String payment(String params) {
        logger.info("CashFree payment request param: {}", params);
        JSONObject object = JSONObject.parseObject(params);

        Map<String, String> header = new HashMap<String, String>() {{
            put("X-Client-Id", object.getString("clientId"));
            put("X-Client-Secret", object.getString("secretKey"));
            put("Content-Type", "application/json");
        }};
        JSONObject body = new JSONObject() {{
            put("orderId", object.getString("orderId"));
            put("orderAmount", object.getString("amount"));
            put("orderCurrency", "INR");
        }};
        return CashFreeApi.getInstance().createOrder(header, body);
    }

    @Override
    public String payout(String params) {
        logger.info("CashFree payout request param: {}", params);
        if (StringUtils.isBlank(params)) return null;
        JSONObject paramData = JSONObject.parseObject(params);
        if (paramData == null) return null;

        JSONObject tokenJson = getToken(paramData);
        if (!tokenJson.getBoolean("success")) return tokenJson.toJSONString();

        String token = tokenJson.getString("data");

        //查询beneId
        boolean beneIdIsBlank = false;
        String beneId = paramData.getString("beneId");
        if (StringUtils.isBlank(beneId)) {
            beneIdIsBlank = true;
            beneId = paramData.getString("name").replaceAll(" ", "") + paramData.getString("playerId");
        }
        if (beneId.length() > 50) beneId = beneId.substring(0, 50);
        boolean isExistBeneficiary = getBeneficiary(token, beneId);
        logger.info("beneId已绑定，beneId:{}", beneId);
        if (!isExistBeneficiary) {
            paramData.put("beneId", beneId);
            JSONObject beneficiaryJson = addBeneficiary(token, paramData);
            if (!beneficiaryJson.getBoolean("success")) return beneficiaryJson.toJSONString();
        }
        if (beneIdIsBlank) {
            PlayerAccountService.getInstance().updateBeneId(beneId, Long.parseLong(paramData.getString("playerId")));
        }

        String transferId = paramData.getString("transferId");
        String finalBeneId = beneId;
        JSONObject transferParams = new JSONObject() {{
            put("beneId", finalBeneId);
            put("amount", paramData.getString("amount"));
            put("transferId", transferId);
        }};
        JSONObject transferJson = requestTransferSync(token, transferParams);
        if (!transferJson.getBoolean("success")) return transferJson.toJSONString();
        String referenceId = transferJson.getJSONObject("data").getString("referenceId");
        paramData.put("referenceId", referenceId);

        paramData.remove("clientId");
        paramData.remove("clientSecret");

        JSONObject resp = new JSONObject() {{
            put("success", true);
            put("data", paramData);
        }};
        return resp.toJSONString();
    }

    private JSONObject getToken(JSONObject paramData) {
        JSONObject result = new JSONObject();
        try {
            String authorize = CashFreeApi.getInstance().authorize(paramData.getString("clientId"), paramData.getString("clientSecret"));
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

    private JSONObject addBeneficiary(String token, JSONObject data) {
        logger.info("request addBeneficiary: {}", data.toJSONString());
        String beneId = data.getString("beneId");
        JSONObject params = new JSONObject() {{
            put("beneId", beneId);
            put("name", data.getString("name"));
            put("email", StringUtils.isBlank(data.getString("email")) ? "johndoe_1@cashfree.com" : data.getString("email"));
            put("phone", data.getString("mobile"));
            put("bankAccount", data.getString("accNo"));
            put("ifsc", data.getString("ifsc"));
//            put("vpa", data.getString(""));
//            put("cardNo", data.getString(""));
            put("address1", "Bangalore, Bengaluru (Bangalore) Urban, Karnataka");
//            put("address2", data.getString(""));
//            put("city", data.getString(""));
//            put("state", data.getString(""));
//            put("pincode", data.getString(""));
        }};
        JSONObject result = new JSONObject();
        try {
            String beneficiary = CashFreeApi.getInstance().addBeneficiary(token, params);
            logger.info("CashFree addBeneficiary response: {}", beneficiary);
            JSONObject jsonObject = JSONObject.parseObject(beneficiary);
            if ("SUCCESS".equals(jsonObject.getString("status"))
                    || "Beneficiary Id already exists".equals(jsonObject.getString("message"))) {
                result.put("success", true);
                result.put("data", beneId);

                PayCache.getInstance().addBeneId(beneId);
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
