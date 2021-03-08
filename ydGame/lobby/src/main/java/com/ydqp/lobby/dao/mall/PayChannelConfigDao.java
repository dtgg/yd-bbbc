package com.ydqp.lobby.dao.mall;

import com.cfq.jdbc.JdbcOrm;
import com.ydqp.common.entity.PayChannelConfig;

import java.util.List;

public class PayChannelConfigDao {

    private PayChannelConfigDao() {}

    private static PayChannelConfigDao instance = new PayChannelConfigDao();

    public static PayChannelConfigDao getInstance() {
        if (instance == null) instance = new PayChannelConfigDao();
        return instance;
    }

    private static final String PAY_CONFIG = "select * from pay_channel_config where enabled = 1 limit 1;";
    public PayChannelConfig getPayChannelConfig() {
        return (PayChannelConfig) JdbcOrm.getInstance().getBean(PAY_CONFIG, PayChannelConfig.class);
    }

    private static final String FIND_BY_CHANNEL = "select * from pay_channel_config where enabled = 1 and channel = ";
    public PayChannelConfig findByChannel(int channel) {
        String sql = FIND_BY_CHANNEL + channel + ";";
        return (PayChannelConfig) JdbcOrm.getInstance().getBean(sql, PayChannelConfig.class);
    }

    private static final String FIND_BY_NAME = "select * from pay_channel_config where name = '";
    public PayChannelConfig findByName(String name) {
        String sql = FIND_BY_NAME + name + "';";
        return (PayChannelConfig) JdbcOrm.getInstance().getBean(sql, PayChannelConfig.class);
    }

    private static final String ENABLE_PAY_CONFIG = "select * from pay_channel_config where enabled = 1;";
    public List<PayChannelConfig> getAllEnableChannel() {
        return JdbcOrm.getInstance().getListBean(ENABLE_PAY_CONFIG, PayChannelConfig.class);
    }

    public PayChannelConfig findById(int id) {
        String sql = "select * from pay_channel_config where id = " + id + ";";
        return (PayChannelConfig) JdbcOrm.getInstance().getBean(sql, PayChannelConfig.class);
    }
}
