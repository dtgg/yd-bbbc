package com.ydqp.common.utils;

import java.util.List;

/**
 * @author ：whw
 * @date ：Created in 2021/3/14 21:20
 * @description：
 */
public class CommonUtils {

    public static String inString(List<Integer> list) {
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        list.forEach(integer -> builder.append(integer).append(","));
        builder.deleteCharAt(builder.length() - 1);
        builder.append(")");
        return builder.toString();
    }
}
