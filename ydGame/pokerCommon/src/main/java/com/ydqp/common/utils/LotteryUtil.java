package com.ydqp.common.utils;

import java.util.Random;

public class LotteryUtil {

    public static String getPrice() {
        StringBuilder price = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            int p = random.nextInt(10);
            if (i == 0 && p == 0) p += 1;
            price.append(p);
        }
        return price.toString();
    }

    public static String getDrawNum() {
        Random random = new Random();
        return String.valueOf(random.nextInt(10));
    }

    public static String intToPeriod(int yourNumber) {
        // 0 代表前面补充0
        // 4 代表长度为4
        // d 代表参数为正数型
        return String.format("%03d", yourNumber); // 0001
    }
}
