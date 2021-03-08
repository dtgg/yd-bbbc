package com.ydqp.lobby.pay.cashfree.param;

import lombok.Data;

@Data
public class OrderCreateResp {

    private String status;

    private String paymentLink;

    private String reason;
}
