package com.ydqp.lottery.dao;

import com.cfq.jdbc.JdbcOrm;
import com.ydqp.common.entity.SysCloseServer;

public class SysCloseServerDao {

    public static SysCloseServerDao instance;

    public static SysCloseServerDao getInstance () {
        if (instance == null) {
            instance = new SysCloseServerDao();
        }
        return instance;
    }


    public SysCloseServer getSysCloseServer  (int serverCode, int status) {
        String selectSql = "select * from sys_close_server where serverCode = " + serverCode + " and status = " + status + ";";

        SysCloseServer sysCloseServer = (SysCloseServer) JdbcOrm.getInstance().getBean(selectSql, SysCloseServer.class);
        return sysCloseServer;
    }

    public void updateSysCloseServer (int id, int status ) {
        String updateSql = "update sys_close_server set status = " + status + " where id = " + id + ";";

        JdbcOrm.getInstance().update(updateSql);
    }
}
