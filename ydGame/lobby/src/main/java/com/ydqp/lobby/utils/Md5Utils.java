package com.ydqp.lobby.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.security.MessageDigest;
import java.util.*;

/**
 * @author ：whw
 * @date ：Created in 2021/3/21 22:16
 * @description：md5
 */
public class Md5Utils {

    public static String sortMapAndSign(Map<String, String> map, String secretKey) {
        List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(map.entrySet());

        // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
        infoIds.sort(Map.Entry.comparingByKey());
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> item : infoIds) {
            if (item.getValue() != null) {
                builder.append(item.getKey());
                builder.append("=");
                builder.append(item.getValue());
                builder.append("&");
            }
        }
        builder.append("key=").append(secretKey);

//        return Objects.requireNonNull(MD5(builder.toString())).toLowerCase();
        return builder.toString();
    }

    public static String MD5(String s) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象

            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();


            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            System.out.println(new String(str));
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
