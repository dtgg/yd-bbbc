package com.ydqp.lobby.dao.mall;

import com.cfq.jdbc.JdbcOrm;
import com.ydqp.common.entity.PayWithdrawalConfig;

import java.util.List;

public class PayWithdrawalConfigDao {

    private PayWithdrawalConfigDao() {}

    private static PayWithdrawalConfigDao instance = new PayWithdrawalConfigDao();

    public static PayWithdrawalConfigDao getInstance() {
        if (instance == null) instance = new PayWithdrawalConfigDao();
        return instance;
    }

    private static final String PAY_CONFIG = "select * from pay_channel_config where enabled = 1 limit 1;";
    public PayWithdrawalConfig getPayChannelConfig() {
        return (PayWithdrawalConfig) JdbcOrm.getInstance().getBean(PAY_CONFIG, PayWithdrawalConfig.class);
    }

    private static final String FIND_BY_CHANNEL = "select * from pay_channel_config where enabled = 1 and channel = ";
    public PayWithdrawalConfig findByChannel(int channel) {
        String sql = FIND_BY_CHANNEL + channel + ";";
        return (PayWithdrawalConfig) JdbcOrm.getInstance().getBean(sql, PayWithdrawalConfig.class);
    }

    private static final String FIND_BY_NAME = "select * from pay_channel_config where name = '";
    public PayWithdrawalConfig findByName(String name) {
        String sql = FIND_BY_NAME + name + "';";
        return (PayWithdrawalConfig) JdbcOrm.getInstance().getBean(sql, PayWithdrawalConfig.class);
    }

    private static final String ENABLE_PAY_CONFIG = "select * from pay_channel_config where enabled = 1;";
    public List<PayWithdrawalConfig> getAllEnableChannel() {
        return JdbcOrm.getInstance().getListBean(ENABLE_PAY_CONFIG, PayWithdrawalConfig.class);
    }

    public PayWithdrawalConfig findById(int id) {
        String sql = "select * from pay_channel_config where id = " + id + ";";
        return (PayWithdrawalConfig) JdbcOrm.getInstance().getBean(sql, PayWithdrawalConfig.class);
    }
}
