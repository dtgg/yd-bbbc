package com.ydqp.common.dao.lottery;

import com.cfq.jdbc.JdbcOrm;
import com.ydqp.common.entity.LotteryConfig;

import java.util.List;

public class LotteryConfigDao {

    private LotteryConfigDao() {}

    private static LotteryConfigDao instance;

    public static LotteryConfigDao getInstance() {
        if (instance == null)
            instance = new LotteryConfigDao();
        return instance;
    }

    private static final String FIND_ALL = "select * from lottery_config;";
    public List<LotteryConfig> findAll() {
        return JdbcOrm.getInstance().getListBean(FIND_ALL, LotteryConfig.class);
    }
}
