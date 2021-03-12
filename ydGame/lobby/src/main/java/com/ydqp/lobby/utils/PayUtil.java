package com.ydqp.lobby.utils;

import com.ydqp.common.entity.PayChannelConfig;
import com.ydqp.common.entity.PayWithdrawalConfig;

import java.util.List;
import java.util.Random;

public class PayUtil {

    private PayUtil() {}

    private static PayUtil instance;

    public static PayUtil getInstance() {
        if (instance == null)
            instance = new PayUtil();
        return instance;
    }

    public PayChannelConfig getConfig(List<PayChannelConfig> configs, Long playerId) {
        if (configs.size() == 1) return configs.get(0);

        int i = configs.size() % playerId.intValue();
        return configs.get(i);
    }

    public PayWithdrawalConfig getWithdrawalConfig(List<PayWithdrawalConfig> configs, Long playerId) {
        if (configs.size() == 1) return configs.get(0);

        int i = configs.size() % playerId.intValue();
        return configs.get(i);
    }
}
