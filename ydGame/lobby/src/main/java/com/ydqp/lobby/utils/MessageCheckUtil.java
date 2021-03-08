package com.ydqp.lobby.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageCheckUtil {

    public static boolean checkName(String str) {
//        Pattern pattern = Pattern.compile("^[a-zA-Z][a-zA-Z\\s]+[a-zA-Z]$");
        Pattern pattern = Pattern.compile("[a-zA-Z\\s]+");
        Matcher matcher = pattern.matcher(str);
        int i = 0;
        while (matcher.find()) {
            i++;
        }
        return i == 1;
    }

    public static boolean checkMobile(String str) {
        Pattern pattern = Pattern.compile("^\\d{8,12}$");
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

    public static boolean checkEmail(String str) {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9]+([_\\.][A-Za-z0-9]+)*@([A-Za-z0-9\\-]+\\.)+[A-Za-z]{2,6}$");
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

    public static boolean checkAccNo(String str) {
        Pattern pattern = Pattern.compile("^[0-9A-Za-z]{9,18}$");
        Matcher matcher = pattern.matcher(str);
        int i = 0;
        while (matcher.find()) {
            i++;
        }
        return i == 1;
    }

    public static boolean checkPassword(String password) {
        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9]{6,20})$");
        Matcher matcher = pattern.matcher(password);
        return matcher.find();
    }

    public static void main(String[] args) {
        String str = "911234567890";
        System.out.println(checkMobile(str));
    }
}
