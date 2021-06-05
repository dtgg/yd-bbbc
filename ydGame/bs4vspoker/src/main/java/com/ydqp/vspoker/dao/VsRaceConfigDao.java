package com.ydqp.vspoker.dao;

import com.cfq.jdbc.JdbcOrm;
import com.ydqp.common.entity.VsRaceConfig;

public class VsRaceConfigDao {

    private VsRaceConfigDao(){}

    private static VsRaceConfigDao instance= new VsRaceConfigDao();

    public static VsRaceConfigDao getInstance() {
        return instance;
    }

    public VsRaceConfig getRaceConfigs(int basePoint) {
        String sql = "select * from vs_race_config where raceType = 2 and enabled = 1 and basePoint = "+basePoint+";";
        return (VsRaceConfig) JdbcOrm.getInstance().getBean(sql, VsRaceConfig.class);
    }

    public VsRaceConfig getZjRaceConfig() {
        String sql = "select * from vs_race_config where raceType = 3 and enabled = 1;";
        return (VsRaceConfig) JdbcOrm.getInstance().getBean(sql, VsRaceConfig.class);
    }

    public static void main(String[] args) {
        VsRaceConfig raceConfig = VsRaceConfigDao.getInstance().getRaceConfigs(100);
        System.out.println();
    }
}
