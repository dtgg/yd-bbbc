package com.ydqp.lobby.pay.cashfree.api;

import com.alibaba.fastjson.JSONObject;
import com.ydqp.common.utils.HttpUtils;
import com.ydqp.lobby.pay.PayUrlUtil;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class CashFreeApi {

    private CashFreeApi() {
    }

    private static CashFreeApi instance = new CashFreeApi();

    public static CashFreeApi getInstance() {
        if (instance == null) instance = new CashFreeApi();
        return instance;
    }

    private static final String ORDER_CREATE = "/api/v2/cftoken/order";

    public String createOrder(Map<String, String> headParams, JSONObject params) {
        String host = PayUrlUtil.getInstance().getUrl("cashFreePaymentUrl");
        return HttpUtils.getInstance().sendPost(host + ORDER_CREATE, headParams, params);
    }

    /******************************************************************************************************************/

    private static final String AUTHORIZE = "/payout/v1/authorize";
    private static final String VERIFY_TOKEN = "/payout/v1/verifyToken";
    private static final String ADD_BENEFICIARY = "/payout/v1/addBeneficiary";
    private static final String REMOVE_BENEFICIARY = "/payout/v1/removeBeneficiary";
    private static final String GET_BALANCE = "/payout/v1/getBalance";
    private static final String REQUEST_TRANSFER_SYNC = "/payout/v1/requestTransfer";
    private static final String REQUEST_TRANSFER_ASYNC = "/payout/v1/requestAsyncTransfer";
    private static final String GET_TRANSFER_STATUS = "/payout/v1/getTransferStatus";
    private static final String GET_BENE_ID = "/payout/v1/getBeneId";
    private static final String GET_BENEFICIARY = "/payout/v1/getBeneficiary";

    private String payoutHost() {
        return PayUrlUtil.getInstance().getUrl("cashFreePayoutUrl");
    }

    private Map<String, String> headParams(String token) {
        return new HashMap<String, String>() {{
            put("Authorization", "Bearer " + token);
        }};
    }

    public boolean isSuccess(String params) {
        if (StringUtils.isBlank(params)) return false;

        JSONObject data;
        try {
            data = JSONObject.parseObject(params);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        String status = data.getString("status");
        return "SUCCESS".equals(status) || "ACCEPTED".equals(status) || "PENDING".equals(status);
    }

    public JSONObject data(String params) {
        if (StringUtils.isBlank(params)) return null;

        JSONObject data;
        try {
            data = JSONObject.parseObject(params);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return data.getJSONObject("data");
    }

    /**
     * 认证
     * {"status":"SUCCESS", "message":"Token generated", "subCode":"200", "data": {"token":"eyJ0eXA...fWStg", "expiry":1564130052}}
     *
     * @return
     */
    public String authorize(String clientId, String clientSecret) {
        return HttpUtils.getInstance().sendPost(payoutHost() + AUTHORIZE, new HashMap<String, String>() {{
            put("X-Client-Id", clientId);
            put("X-Client-Secret", clientSecret);
        }}, null);
    }

    /**
     * 验证令牌
     * {"status":"SUCCESS", "message":"Token is valid", "subCode":"200"}
     *
     * @param token
     * @return
     */
    public String verifyToken(String token) {
        return HttpUtils.getInstance().sendPost(payoutHost() + VERIFY_TOKEN, headParams(token), null);
    }

    /**
     * 添加收款人
     * {"beneId": "JOHN18011", "name": "john doe","email": "johndoe@cashfree.com", "phone": "9876543210", "bankAccount": "00001111222233",
     * "ifsc": "HDFC0000001", "address1" : "ABC Street", "city": "Bangalore", "state":"Karnataka", "pincode": "560001"}
     * {"status":"SUCCESS","subCode":"200","message":"Beneficiary added successfully"}
     *
     * @param token
     * @param params
     * @return
     */
    public String addBeneficiary(String token, JSONObject params) {
        return HttpUtils.getInstance().sendPost(payoutHost() + ADD_BENEFICIARY, headParams(token), params);
    }

    public String removeBeneficiary(String token, JSONObject params) {
        return HttpUtils.getInstance().sendPost(payoutHost() + REMOVE_BENEFICIARY, headParams(token), params);
    }

    /**
     * 获取余额
     * {"status":"SUCCESS", "subCode":"200", "message":"Ledger balance for the account",
     * "data": {"balance":"214735.50", "availableBalance":"173980.50"}}
     *
     * @param token
     * @return
     */
    public String getBalance(String token) {
        return HttpUtils.getInstance().sendPost(payoutHost() + GET_BALANCE, headParams(token), null);
    }

    /**
     * 请求转账
     * {"beneId" : "JOHN18011", "amount": "100.00", "transferId": "DEC2016"}
     * {"status":"SUCCESS", "subCode":"200", "message":"Transfer completed successfully",
     * "data": {"referenceId":"10023","utr":"P16111765023806","acknowledged": 1}}
     *
     * @param token
     * @param params
     * @return
     */
    public String requestTransfer(String token, JSONObject params) {
        return HttpUtils.getInstance().sendPost(payoutHost() + REQUEST_TRANSFER_SYNC, headParams(token), params);
    }

    public String getTransferStatus(String token, JSONObject params) {
        return HttpUtils.getInstance().sendGet(payoutHost() + GET_TRANSFER_STATUS, headParams(token), params);
    }

    public String getBeneId(String token, JSONObject params) {
        return HttpUtils.getInstance().sendGet(payoutHost() + GET_BENE_ID, headParams(token), params);
    }

    public String getBeneficiary(String token, String beneId) {
        return HttpUtils.getInstance().sendGet(payoutHost() + GET_BENEFICIARY + "/" + beneId, headParams(token), new JSONObject());
    }
}
