package com.ydqp.lobby.pay.yaarpay.api;

import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.ydqp.common.utils.HttpUtil;
import com.ydqp.lobby.pay.PayUrlUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YaarPayApi {

    private static final Logger logger = LoggerFactory.getLogger(YaarPayApi.class);

    private YaarPayApi() {}

    private static YaarPayApi instance;

    public static YaarPayApi getInstance() {
        if (instance == null)
            instance = new YaarPayApi();
        return instance;
    }

    private static final String PAYMENT = "/v1/payin/pay_info";

    private static final String PAYOUT = "/agentpay/apply";

    private String getHost() {
        return PayUrlUtil.getInstance().getUrl("yaarPayUrl");
    }

    public String payment(HashMap<String, String> param, String secret) {
        String sign = getSign(param, secret);
        logger.info("sign:{}", sign);
        param.put("sign", sign);

        String s = "";
        try {
            s = HttpUtil.requestPostUrl(getHost() + PAYMENT, param, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }

    public String payout(HashMap<String, String> param, String secret) {
        String sign = getSign(param, secret);

        param.put("sign", sign);
        String s = "";
        try {
            s = HttpUtil.requestPostUrl(getHost() + PAYOUT, param, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }

    private String getSign(Map<String, String> map, String secret) {

        String result = "";
        try {
            List<Map.Entry<String, String>> infoIds = new ArrayList<>(map.entrySet());
            // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
            infoIds.sort(Map.Entry.comparingByKey());

            // 构造签名键值对的格式
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> item : infoIds) {
                String key = item.getKey();
                String val = item.getValue();
                if (StringUtils.isNotBlank(val) && !"sign".equals(key)) {
                    sb.append(key).append("=").append(val).append("&");
                }
            }
            sb.append("key").append("=").append(secret);
            result = sb.toString();
            //进行MD5加密
            logger.info("signstr:{}", result);
            result = DigestUtils.md5Hex(result).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
