package com.ydqp.lobby.utils;

import java.util.Random;

public class RandomValueUtil {

    public static String name = "abcdefghijklmnopqrstuvwxyz";
    public static String base = "abcdefghijklmnopqrstuvwxyz0123456789";
    public static final String[] email_suffix = "@gmail.com,@yahoo.com,@msn.com,@hotmail.com,@aol.com,@ask.com,@live.com,@qq.com,@0355.net,@163.com,@163.net,@263.net,@3721.net,@yeah.net,@googlemail.com,@126.com,@sina.com,@sohu.com,@yahoo.com.cn".split(",");

    public static int getNum(int start, int end) {
        return (int) (Math.random() * (end - start + 1) + start);
    }

    public static String getName() {
        int length = getNum(8, 10);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = (int) (Math.random() * name.length());
            sb.append(name.charAt(number));
        }
        return sb.toString();
    }

    /***
     *
     * Project Name: recruit-helper-util
     * <p>随机生成Email
     *
     * @author youqiang.xiong
     * @date 2018年5月23日  下午2:13:06
     * @version v1.0
     * @since
     * @param lMin
     *         最小长度
     * @param lMax
     *         最大长度
     * @return
     */
    public static String getEmail(int lMin, int lMax) {
        int length = getNum(lMin, lMax);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = (int) (Math.random() * base.length());
            sb.append(base.charAt(number));
        }
        sb.append(email_suffix[(int) (Math.random() * email_suffix.length)]);
        return sb.toString();
    }

    private static String[] telFirst = "134,135,136,137,138,139,150,151,152,157,158,159,130,131,132,155,156,133,153".split(",");

    /***
     *
     * Project Name: recruit-helper-util
     * <p>随机生成手机号码
     *
     * @author youqiang.xiong
     * @date 2018年5月23日  下午2:14:17
     * @version v1.0
     * @since
     * @return
     */
    public static String getTelephone() {
        int index = getNum(0, telFirst.length - 1);
        String first = telFirst[index];
        String second = String.valueOf(getNum(1, 888) + 10000).substring(1);
        String thrid = String.valueOf(getNum(1, 9100) + 10000).substring(1);
        return first + second + thrid;
    }

    /***
     *
     * Project Name: recruit-helper-util
     * <p>随机生成8位电话号码
     *
     * @author youqiang.xiong
     * @date 2018年5月23日  下午2:15:31
     * @version v1.0
     * @since
     * @return
     */
    public static String getLandline() {
//        int index=getNum(0,telFirst.length-1);
//        String first=telFirst[index];
        Random random = new Random();
        String first = String.valueOf(1 + random.nextInt(98));
        String second = String.valueOf(getNum(1, 888) + 10000).substring(1);
        String thrid = String.valueOf(getNum(1, 9100) + 10000).substring(1);
        return first + second + thrid;
    }
}
