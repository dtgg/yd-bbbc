package com.ydqp.lobby.pay;

import com.ydqp.common.entity.PayChannelConfig;
import com.ydqp.lobby.service.mall.PayChannelConfigService;

public abstract class OrderPay implements IOrderPay {
    @Override
    public PayChannelConfig getPayConfig() {
        return PayChannelConfigService.getInstance().getPayChannelConfig();
    }
}
