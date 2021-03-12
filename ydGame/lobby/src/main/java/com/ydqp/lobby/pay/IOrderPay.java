package com.ydqp.lobby.pay;

import com.ydqp.common.entity.*;
import com.ydqp.common.sendProtoMsg.mall.PlayerOrderSuccess;
import com.ydqp.common.sendProtoMsg.mall.PlayerWithdrawalSuc;

import java.io.IOException;
import java.text.ParseException;

public interface IOrderPay {

    PayChannelConfig getPayConfig();

    PlayerOrderSuccess payment(PlayerOrder order, PayChannelConfig config);

    PlayerWithdrawalSuc payout(PlayerWithdrawal withdrawal, PayWithdrawalConfig config, PlayerAccount account);
}
