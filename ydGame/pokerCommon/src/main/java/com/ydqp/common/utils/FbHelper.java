/*
 * author:javahou
 * blog:http://blog.sina.com.cn/javahou
 * time:2020-1-19 16:35:27
 */
package com.ydqp.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;

import java.io.IOException;

/**
 *
 * @author javahou
 */
public class FbHelper
{
    private static final Logger logger = LoggerFactory.getLogger(FbHelper.class);
    private static final String CLIENT_ID = "285795112836266";

    private static final String CLIENT_SECRET = "ae9912bff0e8c027679f3712e36f8868";
    
    private static final String URL_LOGIN = "https://graph.facebook.com/debug_token";
    
    /**
     * 
     * {
            "data": {
               "app_id": "2439760749600618",
               "type": "USER",
               "application": "\u062d\u0631\u0628 \u0627\u0644\u0639\u0631\u0648\u0634",
               "data_access_expires_at": 1587194949,
               "expires_at": 1584601913,
               "is_valid": true,
               "issued_at": 1579417913,
               "metadata": {
                  "auth_type": "rerequest"
               },
               "scopes": [
                  "email",
                  "public_profile"
               ],
               "user_id": "168766577772888"
            }
        }
     * 
     * 
     * @param token
     * @return
     * @throws IOException 
     */
    public static String getUserAcc(String token)
    {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(URL_LOGIN).append("?input_token=").append(token).append("&access_token=").append(CLIENT_ID).append("|").append(CLIENT_SECRET);
            logger.debug("开始请求fb登陆, accesstoken = {}", token);
            String ss = HttpUtil.requestGetUrl(sb.toString(), false);
            logger.debug("结束请求fb登陆, 返回信息 = {}", ss);
            JSONObject json = JSONObject.parseObject(ss);
            JSONObject data = json.getJSONObject("data");
            return data.getString("user_id");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
    
}
