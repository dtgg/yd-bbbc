package com.ydqp.generator.dao;

import java.sql.Connection;
import java.sql.DriverManager;


/**
 *
 * @author cfq
 */
public class JDBCConnection {
    /* 获取数据库连接的函数*/
    public static Connection getConnection(String connectionURL, String user, String password) {
        Connection con = null;  //创建用于连接数据库的Connection对象  
        try {
            Class.forName("com.mysql.jdbc.Driver");// 加载Mysql数据驱动  
            con = DriverManager.getConnection(connectionURL, user, password);// 创建数据连接  
        } catch (Exception e) {
            System.out.println("数据库连接失败" + e.getMessage());
        }
        return con; //返回所建立的数据库连接  
    }



    public static void main(String[] args) {
        String url = "jdbc:mysql://192.168.0.101:3306/game27_uc?zeroDateTimeBehavior=convertToNull";
        String user = "root";
        String password = "net27123";
        getConnection(url,user,password);
   //     List<TableEntity> tableList =DAOGenerate.generateDAO(url, user, password);
    }
}
