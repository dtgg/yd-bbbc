package com.ydqp.lobby.service.mall;

import com.ydqp.common.entity.PayChannelConfig;
import com.ydqp.lobby.dao.mall.PayChannelConfigDao;

import java.util.List;

public class PayChannelConfigService {

    private PayChannelConfigService() {}

    private static PayChannelConfigService instance = new PayChannelConfigService();

    public static PayChannelConfigService getInstance() {
        if (instance == null) instance = new PayChannelConfigService();
        return instance;
    }

    public PayChannelConfig getPayChannelConfig() {
        return PayChannelConfigDao.getInstance().getPayChannelConfig();
    }

    public PayChannelConfig findByChannel(int channel) {
        return PayChannelConfigDao.getInstance().findByChannel(channel);
    }

    public PayChannelConfig findByName(String name) {
        return PayChannelConfigDao.getInstance().findByName(name);
    }

    public List<PayChannelConfig> getAllEnableChannel() {
        return PayChannelConfigDao.getInstance().getAllEnableChannel();
    }

    public PayChannelConfig findById(int id) {
        return PayChannelConfigDao.getInstance().findById(id);
    }
}
