package com.ydqp.lobby.dao;

import com.cfq.jdbc.JdbcOrm;
import com.ydqp.common.entity.ServiceConfig;

public class ServiceConfigDao {
    private ServiceConfigDao() {}

    public static ServiceConfigDao instance;

    public static ServiceConfigDao getInstance() {
        if (instance == null) instance = new ServiceConfigDao();
        return instance;
    }

    public ServiceConfig getServiceConfig(int appId) {
        String sql = "select * from service_config where appId = " + appId + ";";
        ServiceConfig serviceConfig = (ServiceConfig) JdbcOrm.getInstance().getBean(sql, ServiceConfig.class);
        return serviceConfig;
    }
}

