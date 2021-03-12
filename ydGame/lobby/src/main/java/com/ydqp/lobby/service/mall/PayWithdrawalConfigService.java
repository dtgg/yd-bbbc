package com.ydqp.lobby.service.mall;

import com.ydqp.common.entity.PayWithdrawalConfig;
import com.ydqp.lobby.dao.mall.PayWithdrawalConfigDao;

import java.util.List;

public class PayWithdrawalConfigService {

    private PayWithdrawalConfigService() {}

    private static PayWithdrawalConfigService instance = new PayWithdrawalConfigService();

    public static PayWithdrawalConfigService getInstance() {
        if (instance == null) instance = new PayWithdrawalConfigService();
        return instance;
    }

    public PayWithdrawalConfig getPayChannelConfig() {
        return PayWithdrawalConfigDao.getInstance().getPayChannelConfig();
    }

    public PayWithdrawalConfig findByChannel(int channel) {
        return PayWithdrawalConfigDao.getInstance().findByChannel(channel);
    }

    public PayWithdrawalConfig findByName(String name) {
        return PayWithdrawalConfigDao.getInstance().findByName(name);
    }

    public List<PayWithdrawalConfig> getAllEnableChannel() {
        return PayWithdrawalConfigDao.getInstance().getAllEnableChannel();
    }

    public PayWithdrawalConfig findById(int id) {
        return PayWithdrawalConfigDao.getInstance().findById(id);
    }
}
