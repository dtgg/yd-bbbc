package com.ydqp.generator.dao;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author cfq
 */
public class TypeMapping {

    private static Map<String, String> typeMapping = new HashMap<String, String>();
    private static Map<String, String> fieldTypeMapping = new HashMap<String, String>();
    private static int[] sizes = {8, 11, 16, 24, 32, 36, 64, 128, 256, 512, 1024, 2048, 65535};

    static {

        typeMapping.put("TINYINT UNSIGNED", "byte");
        typeMapping.put("BIGINT UNSIGNED", "long");
        typeMapping.put("BIGINT", "long");
        typeMapping.put("DATETIME", "Date");
        typeMapping.put("TIMESTAMP", "Date");
        typeMapping.put("INT", "int");
        typeMapping.put("MEDIUMINT", "int");
        typeMapping.put("SMALLINT", "int");
        typeMapping.put("FLOAT", "float");
        typeMapping.put("BIT", "byte");
        typeMapping.put("DATE", "Date");
        typeMapping.put("TINYINT", "byte");
        typeMapping.put("VARCHAR", "String");
        typeMapping.put("CHAR", "String");
        typeMapping.put("DOUBLE", "double");
        typeMapping.put("TEXT", "String");
        typeMapping.put("TIME", "String");
        typeMapping.put("INT UNSIGNED", "int");
        typeMapping.put("SMALLINT UNSIGNED", "int");
        typeMapping.put("MEDIUMINT UNSIGNED", "int");
        fieldTypeMapping.put("BIGINT UNSIGNED", "BIG_INT");
        fieldTypeMapping.put("BIGINT", "BIG_INT");
        fieldTypeMapping.put("DATETIME", "DATETIME");
        fieldTypeMapping.put("INT", "INT");
        fieldTypeMapping.put("INT UNSIGNED", "INT");
        fieldTypeMapping.put("MEDIUMINT", "INT");
        fieldTypeMapping.put("MEDIUMINT UNSIGNED", "INT");
        fieldTypeMapping.put("SMALLINT", "SMALL_INT");
        fieldTypeMapping.put("SMALLINT UNSIGNED", "SMALL_INT");
        fieldTypeMapping.put("FLOAT", "DOUBLE");
        fieldTypeMapping.put("BIT", "TYINT");
        fieldTypeMapping.put("DATE", "DATE");
        fieldTypeMapping.put("TIMESTAMP", "DATETIME");
        fieldTypeMapping.put("TINYINT UNSIGNED", "TYINT");
        fieldTypeMapping.put("TINYINT", "TYINT");
        fieldTypeMapping.put("VARCHAR", "CHAR");//
        fieldTypeMapping.put("CHAR", "CHAR8");
        fieldTypeMapping.put("DOUBLE", "DOUBLE");
        fieldTypeMapping.put("TEXT", "CHAR2048");
  
       
    }

    public static String getJavaType(String dbType) {
        return typeMapping.get(dbType);
    }

    public static String getFieldType(String dbType, int dataSize) {
        String result = null;

        if ("VARCHAR".equals(dbType)) {
            int preSize = 8;
            for (int size : sizes) {
                if (dataSize - size >= 0) {
                    preSize = size;
                } else {
                    break;
                }
            }
            result = "CHAR" + preSize;

        } else {
            result = fieldTypeMapping.get(dbType);
        }
        return result;

    }
}
