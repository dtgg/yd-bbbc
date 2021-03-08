package com.ydqp.lobby.utils;

import com.ydqp.common.entity.PayChannelConfig;

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

    public PayChannelConfig getConfig(List<PayChannelConfig> configs) {
        if (configs.size() == 1) return configs.get(0);

        Random random = new Random();
        int i = random.nextInt(configs.size());
        return configs.get(i);
    }
}
