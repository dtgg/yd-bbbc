package com.ydqp.lobby.pay;

import com.ydqp.common.entity.*;
import com.ydqp.common.sendProtoMsg.mall.PlayerOrderSuccess;
import com.ydqp.common.sendProtoMsg.mall.PlayerWithdrawalSuccess;
import com.ydqp.lobby.pay.hx.Hx;
import com.ydqp.lobby.pay.pay777.Pay777;
import com.ydqp.lobby.pay.payplus.PayPlus;
import com.ydqp.lobby.pay.shineu.ShineU;
import com.ydqp.lobby.pay.uupay.UuPay;

public class CommonPay extends OrderPay {

    public OrderPay getOrderPay(String payConfigName) {
        switch (payConfigName) {
            case "777pay":
                return new Pay777();
            case "shineu":
                return new ShineU();
            case "uupay":
                return new UuPay();
            case "hx":
                return new Hx();
            case "payplus":
                return new PayPlus();
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
