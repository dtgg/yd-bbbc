package com.ydqp.lobby.pay.cashfree.param;

import lombok.Data;

@Data
public class OrderCreateReq {

    private String appId;

    private String secretKey;

    private String orderId;

    private double orderAmount;

    private String customerName;

    private String customerPhone;

    private String customerEmail;

    private String returnUrl;

    private String notifyUrl;
}
