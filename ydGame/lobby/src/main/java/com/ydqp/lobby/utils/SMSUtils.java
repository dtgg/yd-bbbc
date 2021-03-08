package com.ydqp.lobby.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Description: 天一泓国际短信IMFS HTTP API 签名
 * @Author: whw
 * @Date: 2020/06/07
 */
public class SMSUtils {

    private static final String ACCOUNT = "cs_2al35o";

    private static final String PASSWORD = "ZdHEFd97";

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private static final String URL = "http://sms.skylinelabs.cc:20003/sendsmsV2?";

    private static final SMSUtils INSTANCE = new SMSUtils();

    private SMSUtils() {}

    public static SMSUtils getInstance() {
        return INSTANCE;
    }

    public String smsSign(String datetime) {
        return DigestUtils.md5Hex((ACCOUNT + PASSWORD + datetime).getBytes());
    }

    public String smsRequestUrl() {
        String datetime = ZonedDateTime.now(ZoneId.of("GMT+08")).format(FORMATTER);
        return URL +
                "account=" + ACCOUNT +
                "&sign=" + smsSign(datetime) +
                "&datetime=" + datetime;
    }

    public static void main(String[] args) {
        int smsCode = (int) ((Math.random() * 9 + 1) * 1000);
        System.out.println(smsCode);
        System.out.println(DigestUtils.md5Hex(("account" + "pwd" + "20181109231202").getBytes()));
    }
}
