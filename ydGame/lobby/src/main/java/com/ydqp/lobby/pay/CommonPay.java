package com.ydqp.lobby.pay;

import com.ydqp.common.entity.*;
import com.ydqp.common.sendProtoMsg.mall.PlayerOrderSuccess;
import com.ydqp.common.sendProtoMsg.mall.PlayerWithdrawalSuccess;
import com.ydqp.lobby.pay.cashfree.CashFreePay;
import com.ydqp.lobby.pay.pay777.Pay777;
import com.ydqp.lobby.pay.razorpay.RazorPay;
import com.ydqp.lobby.pay.uupay.UuPay;

public class CommonPay extends OrderPay {

    public OrderPay getOrderPay(String payConfigName) {
        switch (payConfigName) {
            case "777pay":
                return new Pay777();
            case "uupay":
                return new UuPay();
            default:
                return null;
        }
    }

    @Override
    public PlayerOrderSuccess payment(PlayerOrder order, PayChannelConfig config) {
        return null;
    }

    @Override
    public PlayerWithdrawalSuccess payout(PlayerWithdrawal withdrawal, PayWithdrawalConfig config, PlayerAccount account) {
        return null;
    }
}
