package com.ydqp.common.dao;

import com.cfq.jdbc.JdbcOrm;
import com.ydqp.common.entity.PumpConfig;

public class PumpConfigDao {
    private PumpConfigDao() {}

    private static PumpConfigDao instance;

    public static PumpConfigDao getInstance() {
        if (instance == null) {
            instance = new PumpConfigDao();
        }
        return instance;
    }

    public PumpConfig getPumpConfig(int serverCode) {
        String sql = "select * from pump_config where serverCode = " + serverCode +";";
        PumpConfig pumpConfig = (PumpConfig) JdbcOrm.getInstance().getBean(sql, PumpConfig.class);
        return pumpConfig;
    }
}
