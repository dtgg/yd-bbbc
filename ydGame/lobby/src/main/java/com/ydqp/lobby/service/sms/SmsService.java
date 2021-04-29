package com.ydqp.lobby.service.sms;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.receiveProtoMsg.player.SmsSend;
import com.ydqp.lobby.service.sms.data.SMSCodeEnum;
import com.ydqp.lobby.service.sms.data.SMSParam;
import com.ydqp.lobby.utils.HttpSmsUtil;
import com.ydqp.lobby.utils.SMSUtils;

public class SmsService {

    private static final Logger logger = LoggerFactory.getLogger(SmsService.class);

    private SmsService() {
    }

    private static SmsService instance;

    public static SmsService getInstance() {
        if (instance == null) instance = new SmsService();
        return instance;
    }

    //产品名称:云通信短信API产品,开发者无需替换
    static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";

    private static final String SMS_CHUANG_LAN_ACCOUNT = "I5351393";
    private static final String SMS_CHUANG_LAN_PASSWORD = "ozG9g7TNj";
    private static final String SMS_CHUANG_LAN_SENDERID = ""; //默认为空
    private static final String SMS_CHUANG_LAN_URL = "http://intapi.253.com/send/json";
    private static final String SMS_CHUANG_LAN_MSG = "【LuckyTime】Your Verification Code is :";

    public boolean sendVerifyCode(SmsSend smsSend) {
        String mobile = smsSend.getMobile();
        int areaCode = smsSend.getAreaCode();

        int expire = PlayerCache.getInstance().getVerificationCodeExpire(mobile);
        //验证码未过期
        if (expire > 0) {
            logger.info("验证码已发送, mobile:{}", mobile);
            return true;
        }
        // 限制手机号位数
        if (smsSend.getMobile().length() != 10) {
            logger.info("手机号格式错误, mobile:{}", mobile);
            return false;
        }
        // 发送短信验证码
        String code = sendSmsByTianYiHong(mobile, areaCode);
//        String code = "1234";
        if (code != null) {
            // 保存验证码至redis
            PlayerCache.getInstance().addVerificationCode(mobile, code);
            return true;
        }
        return false;
    }

    /**
     * 使用创蓝的短信通道发送验证码
     *
     * @param phone
     * @return
     */
    private String sendSmsByChuanglan(String phone, int areaCode) {
        int smsCode = (int) ((Math.random() * 9 + 1) * 1000);
        String msg = "Your SMS verification code is:" + smsCode;
        //组装请求参数
        JSONObject map = new JSONObject();
        map.put("account", SMS_CHUANG_LAN_ACCOUNT);
        map.put("password", SMS_CHUANG_LAN_PASSWORD);
        map.put("msg", msg);
        map.put("mobile", areaCode + "" + phone);
        map.put("senderId", SMS_CHUANG_LAN_SENDERID);
        String params = map.toString();
        logger.info("请求参数为:{}", params);
        try {
            String result = HttpSmsUtil.post(SMS_CHUANG_LAN_URL, params);
            JSONObject jsonObject = JSON.parseObject(result);
            String code = jsonObject.get("code").toString();
            String msgId = jsonObject.get("msgid").toString();
            String error = jsonObject.get("error").toString();
            logger.info("状态码:" + "" + code + " phone:" + phone + ",消息id:" + msgId + " smsCode:" + smsCode);
            if (!"0".equalsIgnoreCase(code)) {
                logger.error("发送验证码失败," + "状态码:" + code + ",状态码说明:" + error + ",消息id:" + msgId);
                return null;
            }

            return smsCode + "";
        } catch (Exception e) {
            logger.error("请求异常:{}", e.toString());
        }
        return null;
    }

    /**
     * 天一泓国际短信
     *
     * @param phone
     * @param areaCode
     * @return
     */
    private String sendSmsByTianYiHong(String phone, int areaCode) {
        String requestUrl = SMSUtils.getInstance().smsRequestUrl();
        SMSParam param = new SMSParam();
        param.setNumbers(areaCode + phone);
        param.setSenderid("");
        int smsCode = (int) ((Math.random() * 9 + 1) * 1000);
        param.setContent("[COCO] SMS verification code is "+smsCode+", valid for 5 minutes, please don't tell others.");
        logger.info("请求地址为: {};请求参数为: {}", requestUrl, JSON.toJSONString(param));
        try {
            String result = HttpSmsUtil.post(requestUrl, JSON.toJSONString(param));
            logger.info("天一弘短信通道返回结果: {}", result);
            JSONObject jsonObject = JSON.parseObject(result);
            Integer status = jsonObject.getInteger("status");
            logger.info("状态码: {}", status);
            if (0 != status) {
                logger.error("发送验证码失败,状态码: {}; 状态码说明: {}", status, SMSCodeEnum.getMessage(status));
                return null;
            }
            return String.valueOf(smsCode);
        } catch (Exception e) {
            logger.error("请求异常:{}" + e.toString());
        }
        return null;
    }
}
