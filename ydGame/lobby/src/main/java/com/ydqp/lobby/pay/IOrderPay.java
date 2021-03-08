package com.ydqp.lobby.pay;

import com.ydqp.common.entity.PayChannelConfig;

import java.io.IOException;
import java.text.ParseException;

public interface IOrderPay {

    PayChannelConfig getPayConfig();

    String payment(String params) throws IOException;

    String payout(String params) throws ParseException;
}
