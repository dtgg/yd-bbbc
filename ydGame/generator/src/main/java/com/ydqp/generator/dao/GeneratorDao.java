package com.ydqp.generator.dao;

import java.io.*;
import java.util.List;
import java.util.Random;

/**
 * Created by cfq on 2017/5/24.
 */
public class GeneratorDao {
    private static String template;
    static {
        StringBuilder sb = new StringBuilder(200);
        String fileName = "D:\\project\\gameproject\\yd-bbbc\\ydGame\\generator\\src\\main\\java\\com\\ydqp\\generator\\dao\\template";
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                sb.append(tempString).append("\r\n");
                System.out.println("line " + line + ": " + tempString);
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        template = sb.toString();
    }

    public static void main(String[] args) {
        try {
            String url = "jdbc:mysql://127.0.0.1:3306/lottery_game?useUnicode=true&autoReconnect=true&rewriteBatchedStatements=true";
            String user = "root";
            String password = "";
            String dbName = "lottery_game";
            String dbType = "MYSQL";
            String cacheChain = "";
            String caches = "CacheType.REDIS";
            if (cacheChain.isEmpty() || cacheChain.equals("NO_CACHE")) {
                cacheChain = "CacheChainType.NO_CACHE";
                caches = "{}";
            } else {
                cacheChain = "CacheChainType." + cacheChain;
                if (caches.contains(",")) {

                    String[] cacheNames = caches.split(",");
                    caches = "{";
                    for (String cacheName : cacheNames) {
                        caches = caches + "CacheType." + cacheName + ",";
                    }
                    caches = caches.substring(0, caches.length() - 1);
                    caches = caches + "}";

                } else {
                    caches = "{CacheType." + caches + "}";
                }
            }


            String path = "D:\\yanfa\\youxi\\qp\\dao";
            //path = context.getPrimaryFile().getPath();
            System.out.println("path =" + path);
            int index = path.indexOf("/src/main/java/");
            String packages = path.substring(index + 15);
            packages = packages.replace("/", ".");

            List<TableEntity> tableList = DAOGenerate.generateDAO(url, user, password);
            String tableName;
            String className;
            List<ColumnEntity> primaryKeyColumnList;
            List<ColumnEntity> columnList;
            StringBuilder keyFields;
            StringBuilder fieldInfo;
            StringBuilder columnInfo;
            StringBuilder toMap;
            StringBuilder parseMap;
            StringBuilder keyField;
            String javaType;
            String methodFieldName;
            String fieldType;
            String columnInfoStr;
            StringBuilder fieldName;

            StringBuilder entityNames = new StringBuilder(500);
            //entityName
            entityNames.append("package ").append("com.ydqp.common.entity").append(";\r\n").append("public class EntityNames { \r\n");
            for (TableEntity tableEntity : tableList) {
                try {

                    tableName = tableEntity.getTableName();
                    className = StringUtils.getClassName(tableName);
                    entityNames.append("\tpublic static final String ").append(className).append("= \"").append(tableName).append("\";").append("\r\n");
                    //primary key
                    primaryKeyColumnList = tableEntity.getPrimaryKeyColumnList();
//                    keyFields = new StringBuilder();
//                    keyField = new StringBuilder();
//                    keyFields.append("{");
//                    keyField.append("\tPrimaryKey primaryKey = new PrimaryKey();\r\n");
//                    for (ColumnEntity column : primaryKeyColumnList) {
//                        keyFields.append("\"").append(column.getColumnName()).append("\"").append(",");
//                        keyField.append("\t primaryKey.putKeyField(\"").append(column.getColumnName()).append("\", String.valueOf(this.").append(column.getColumnName()).append("));\r\n");
//                    }
//                    keyField.append("\t return primaryKey;");
//                    if (keyFields.length() > 2) {
//                        keyFields.setLength(keyFields.length() - 1);
//                    }
//                    keyFields.append("}");
                    fieldInfo = new StringBuilder();
                    columnList = tableEntity.getColumnList();
                    fieldName = new StringBuilder();
                    toMap = new StringBuilder();
                    parseMap = new StringBuilder();
                    for (ColumnEntity column : columnList) {
                        String columnName = column.getColumnName().toUpperCase() + "_NAME";
                        fieldName.append("\tpublic static final String " + columnName + " = \"" + column.getColumnName() + "\";\r\n");
                        System.out.println(column.getColumnType());
                        javaType = TypeMapping.getJavaType(column.getColumnType());
                        fieldType = TypeMapping.getFieldType(column.getColumnType(), column.getDataSize());
                        System.out.println(fieldType);
                        methodFieldName = StringUtils.toFirstUpcase(column.getColumnName());
                        //columnInfo = new StringBuilder("\t@FieldConfig(fieldName = ${colunmName}, fieldType =FieldTypeEnum.${fieldType} , description = \"${comment}\")\r\n");
                        columnInfo = new StringBuilder();
                        columnInfo.append("\tprivate ${javaType} ${fieldName};\r\n");
                        columnInfo.append("\tpublic ${javaType} get${methodFieldName}() {\r\n");
                        columnInfo.append("\treturn ${fieldName};\r\n");
                        columnInfo.append("\t}\r\n");
                        columnInfo.append(" \t public void set${methodFieldName}(${javaType} ${fieldName}){\r\n");
                        columnInfo.append("\t this.${fieldName} = ${fieldName};\r\n");
                        if ("VARCHAR".equals(column.getColumnType()) || "CHAR".equals(column.getColumnType()) || column.getColumnType().contains("VARCHAR") || column.getColumnType().contains("CHAR")) {
                            columnInfo.append("\t entityMap.put(").append(columnName).append(", ${fieldName});\r\n");
                        } else if ("TIMESTAMP".equals(column.getColumnType())) {
                            columnInfo.append("\t entityMap.put(").append(columnName).append(",").append("new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").format(").append("${fieldName})").append(");\r\n");
                        } else if ("DATETIME".equals(column.getColumnType())) {
                            columnInfo.append("\t entityMap.put(").append(columnName).append(",").append("new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").format(").append("${fieldName})").append(");\r\n");
                        } else if ("DATE".equals(column.getColumnType())) {
                            columnInfo.append("\t entityMap.put(").append(columnName).append(",").append("new SimpleDateFormat(\"yyyy-MM-dd\").format(").append("${fieldName})").append(");\r\n");
                        } else {
                            columnInfo.append("\t entityMap.put(").append(columnName).append(", ${fieldName});\r\n");
                        }
                        columnInfo.append("\t}\r\n");
                        columnInfoStr = columnInfo.toString().replace("${fieldName}", column.getColumnName());
                        System.out.println("fieldName =" + columnName);
                        System.out.println("fieldType =" + fieldType);
                        columnInfoStr = columnInfoStr.replace("${fieldType}", fieldType);
                        columnInfoStr = columnInfoStr.replace("${methodFieldName}", methodFieldName);
                        columnInfoStr = columnInfoStr.replace("${javaType}", javaType);
//                        if ("VARCHAR".equals(column.getColumnType()) || "CHAR".equals(column.getColumnType()) || column.getColumnType().contains("VARCHAR") || column.getColumnType().contains("CHAR")) {
//                            toMap.append("\t entityMap.put(").append(columnName).append(", this.").append(column.getColumnName()).append("== null ? \"\":").append("this.").append(column.getColumnName()).append(");\r\n");
//                        } else if ("TIMESTAMP".equals(column.getColumnType())) {
//                            toMap.append("\ttry{\r\n");
//                            toMap.append("\t entityMap.put(").append(columnName).append(",").append("new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").format(").append(column.getColumnName()).append("));\r\n");
//                            toMap.append("\t}catch(Exception ex){ \r\n\tSystem.err.print(ex);\r\n").append("\t entityMap.put(\"").append(column.getColumnName()).append("\",\"\");\r\n").append("\t}\r\n");
//                        } else if ("DATETIME".equals(column.getColumnType())) {
//                            toMap.append("\ttry{\r\n");
//                            toMap.append("\t entityMap.put(").append(columnName).append(",").append("new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").format(").append(column.getColumnName()).append("));\r\n");
//                            toMap.append("\t}catch(Exception ex){ \r\n\tSystem.err.print(ex);\r\n").append("\t entityMap.put(\"").append(column.getColumnName()).append("\",\"\");\r\n").append("\t}\r\n");
//                        } else if ("DATE".equals(column.getColumnType())) {
//                            toMap.append("\r\ttry{\r\n");
//                            toMap.append("\t entityMap.put(").append(columnName).append(",").append("new SimpleDateFormat(\"yyyy-MM-dd\").format(").append(column.getColumnName()).append("));\r\n");
//                            toMap.append("\t}catch(Exception ex){ \r\n\tSystem.err.print(ex);\r\n").append("\t entityMap.put(\"").append(column.getColumnName()).append("\",\"\");\r\n").append("\t}\r\n");
//                        } else {
//                            toMap.append("\t entityMap.put(").append(columnName).append(", String.valueOf(this.").append(column.getColumnName()).append("));\r\n");
//                        }
//                        if ("INT".equals(column.getColumnType()) || "MEDIUMINT".equals(column.getColumnType()) || "SMALLINT".equals(column.getColumnType()) || "INT UNSIGNED".equals(column.getColumnType()) || "MEDIUMINT UNSIGNED".equals(column.getColumnType()) || "SMALLINT UNSIGNED".equals(column.getColumnType())) {
//                            parseMap.append("\t this.").append(column.getColumnName()).append("=").append("Integer.parseInt(entityMap.get(").append(columnName).append(") == null ? \"0\" : entityMap.get(").append(columnName).append("));\r\n");
//                        } else if ("TINYINT".equals(column.getColumnType()) || "BIT".equals(column.getColumnType()) || "TINYINT UNSIGNED".equals(column.getColumnType())) {
//                            parseMap.append("\t this.").append(column.getColumnName()).append("=").append("Byte.parseByte(entityMap.get(").append(columnName).append(") == null ? \"0\" : entityMap.get(").append(columnName).append("));\r\n");
//                        } else if ("DOUBLE".equals(column.getColumnType())) {
//                            parseMap.append("\t this.").append(column.getColumnName()).append("=").append("Double.parseDouble(entityMap.get(").append(column.getColumnName()).append(") == null ? \"0.0\" : entityMap.get(").append(columnName).append("));\r\n");
//                        } else if ("FLOAT".equals(column.getColumnType())) {
//                            parseMap.append("\t this.").append(column.getColumnName()).append("=").append("Float.parseFloat(entityMap.get(").append(columnName).append(") == null ? \"0.0\" : entityMap.get(").append(columnName).append("));\r\n");
//                        } else if ("DATE".equals(column.getColumnType())) {
//                            parseMap.append("\ttry{\r\n");
//                            parseMap.append("\t this.").append(column.getColumnName()).append("=").append("new SimpleDateFormat(\"yyyy-MM-dd\").parse(entityMap.get(").append(columnName).append("));\r\n");
//                            parseMap.append("\t}catch(Exception ex){ \r\n\tSystem.err.print(ex);\r\n").append("\t}\r\n");
//                        } else if ("DATETIME".equals(column.getColumnType()) || "TIMESTAMP".equals(column.getColumnType())) {
//                            parseMap.append("\r\ttry{\r\n");
//                            parseMap.append("\t this.").append(column.getColumnName()).append("=").append("new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(entityMap.get(").append(column.getColumnName()).append("));\r\n");
//                            parseMap.append("\t}catch(Exception ex){ \r\n\tSystem.err.print(ex);\r\n").append("\t}\r\n");
//                        } else if ("BIGINT".equals(column.getColumnType()) || "BIGINT UNSIGNED".equals(column.getColumnType())) {
//                            parseMap.append("\t this.").append(column.getColumnName()).append("=").append("Long.parseLong(entityMap.get(").append(columnName).append(") == null ? \"0\" : entityMap.get(").append(columnName).append("));\r\n");
//                        } else {
//                            parseMap.append("\t this.").append(column.getColumnName()).append("=").append("(entityMap.get(").append(columnName).append(")== null ? \"\" :").append("entityMap.get(").append(columnName).append("));").append("\r\n");
//                        }
                        fieldInfo.append(columnInfoStr);
                    }
                    toMap.append("\t return entityMap;\r\n");
                    path = "D:\\project\\gameproject\\yd-bbbc\\dao";
                    //path = "D:\\project\\gameproject\\yd-bbbc\\dao";

                    System.out.println("path =" + path);
                    FileWriter fw = null;

                    fw = new FileWriter(path + File.separator + className + ".java");
                    System.out.println("tablename=" + tableName);
                    StringBuilder sb = new StringBuilder(template);
                    String sbTemplate = sb.toString();
                    sbTemplate = sbTemplate.replace("${tableName}", className);
                    sbTemplate = sbTemplate.replace("${package}", "com.ydqp.common.entity");
                    sbTemplate = sbTemplate.replace("${className}", className);
//                    sbTemplate = sbTemplate.replace("${dbType}", dbType);
//                    sbTemplate = sbTemplate.replace("${keyFields}", keyFields.toString());
                    sbTemplate = sbTemplate.replace("${dbName}", StringUtils.replaceMiddle(dbName));
//                    sbTemplate = sbTemplate.replace("${caches}", caches);
//                    sbTemplate = sbTemplate.replace("${cacheChain}", cacheChain);
                    sbTemplate = sbTemplate.replace("${FieldName}", fieldName);
                    //sbTemplate = sbTemplate.replace("${KeyField}", keyField);
                    sbTemplate = sbTemplate.replace("${FieldInfo}", fieldInfo);
//                    sbTemplate = sbTemplate.replace("${parseMap}", parseMap);
//                    sbTemplate = sbTemplate.replace("${toMap}", toMap);
                    sbTemplate = sbTemplate.replace("${serialVersionUID}", String.valueOf(System.currentTimeMillis() + new Random().nextInt(100000000)));

                    fw.write(sbTemplate);
                    fw.flush();
                } catch (IOException ex) {

                }
            }
            entityNames.append("\tpublic static final String ").append("dbDSName").append("= \"").append(dbName).append("\";").append("\r\n");
            entityNames.append("\tpublic static final String ").append("redisDSName").append("= \"").append("redis_ds").append("\";").append("\r\n");
            entityNames.append("\tpublic static final String ").append(StringUtils.replaceMiddle(dbName)).append("= \"").append(dbName).append("\";").append("\r\n");
            entityNames.append("}");
            FileWriter entityNameWriter = null;
            entityNameWriter = new FileWriter(path + File.separator + "EntityNames.java");
            entityNameWriter.write(entityNames.toString());
            entityNameWriter.flush();
            //JOptionPane.showMessageDialog(null, "执行成功", "成功提示", JOptionPane.OK_OPTION);
        } catch (IOException ex) {

        }
    }
}
