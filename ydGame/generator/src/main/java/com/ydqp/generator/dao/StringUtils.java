package com.ydqp.generator.dao;

/**
 *
 * @author cfq
 */
public class StringUtils {

    public static String toFirstUpcase(String str) {
        String result = str;
        char c = str.charAt(0);
        char first = 0;
        if (c >= 'a' && c <= 'z') {
            first = (char) (c - 32);
            result = first + str.substring(1);
        }

        result = result.replaceAll("_", "");
        return result;
    }

    public static String getClassName(String tableName) {
         String name = "";
        if (tableName.contains("_")) {
            String[] words = tableName.split("_");
            for (String word : words) {
                char c = word.charAt(0);
                char first = 0;
                if (c >= 'a' && c <= 'z') {
                    first = (char) (c - 32);
                    word = first + word.substring(1);
                    name = name + word;
                }

            }
        }else if(tableName.contains("-")){
             String[] words = tableName.split("-");
           
            for (String word : words) {
                char c = word.charAt(0);
                char first = 0;
                if (c >= 'a' && c <= 'z') {
                    first = (char) (c - 32);
                    word = first + word.substring(1);
                    name = name + word;
                }

            }
        }else{
            char first = 0;
            char c = tableName.charAt(0);
            if (c >= 'a' && c <= 'z') {
                first = (char) (c - 32);
                name = first + tableName.substring(1);
            }
        }
        return name;

    }
    
    public static String replaceMiddle(String dbName){
        if(dbName.contains("-")){
            dbName = dbName.replaceAll("-", "_");
        }
        return dbName;
    }
}
