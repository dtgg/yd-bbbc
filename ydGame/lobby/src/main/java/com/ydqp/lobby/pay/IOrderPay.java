package com.ydqp.lobby.pay;

import com.ydqp.common.entity.*;
import com.ydqp.common.sendProtoMsg.mall.PlayerOrderSuccess;
import com.ydqp.common.sendProtoMsg.mall.PlayerWithdrawalSuccess;

public interface IOrderPay {

    PayChannelConfig getPayConfig();

    PlayerOrderSuccess payment(PlayerOrder order, PayChannelConfig config);

    PlayerWithdrawalSuccess payout(PlayerWithdrawal withdrawal, PayWithdrawalConfig config, PlayerAccount account);
}
