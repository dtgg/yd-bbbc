package com.ydqp.vspoker.dao;

import com.cfq.jdbc.JdbcOrm;
import com.ydqp.common.entity.VsRaceConfig;

import java.util.List;

public class VsRaceConfigDao {

    private VsRaceConfigDao(){}

    private static VsRaceConfigDao instance= new VsRaceConfigDao();

    public static VsRaceConfigDao getInstance() {
        return instance;
    }

    public VsRaceConfig getRaceConfigs(int basePoint) {
        String sql = "select * from vs_race_config where enabled = 1 and basePoint = "+basePoint+";";
        return (VsRaceConfig) JdbcOrm.getInstance().getBean(sql, VsRaceConfig.class);
    }
}
