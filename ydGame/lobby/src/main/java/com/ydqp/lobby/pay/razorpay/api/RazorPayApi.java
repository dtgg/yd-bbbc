package com.ydqp.lobby.pay.razorpay.api;

import com.alibaba.fastjson.JSONObject;
import com.ydqp.common.utils.HttpUtils;
import com.ydqp.lobby.pay.PayUrlUtil;

import java.util.Map;

public class RazorPayApi {

    private RazorPayApi() {}

    private static RazorPayApi instance = new RazorPayApi();

    public static RazorPayApi getInstance() {
        return instance;
    }

    private static final String CREATE_CONTACT = "/contacts";

    private static final String CREATE_FUND = "/fund_accounts";

    private static final String PAYOUTS = "/payouts";

    private String getHost() {
        return PayUrlUtil.getInstance().getUrl("razorPayUrl");
    }

    public String createContact(Map<String, String> authMap, String paramStr) {
        return HttpUtils.getInstance().sendPostWithAuth(getHost() + CREATE_CONTACT, null, authMap, JSONObject.parseObject(paramStr), false);
    }

    public String createFund(Map<String, String> authMap, String paramStr) {
        return HttpUtils.getInstance().sendPostWithAuth(getHost() + CREATE_FUND, null, authMap, JSONObject.parseObject(paramStr), false);
    }

    public String payout(Map<String, String> authMap, String paramStr) {
        return HttpUtils.getInstance().sendPostWithAuth(getHost() + PAYOUTS, null, authMap, JSONObject.parseObject(paramStr), false);
    }
}
