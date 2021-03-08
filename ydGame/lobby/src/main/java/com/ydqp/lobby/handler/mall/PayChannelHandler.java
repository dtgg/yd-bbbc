package com.ydqp.lobby.handler.mall;

import com.alibaba.fastjson.JSONObject;
import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.entity.PayChannelConfig;
import com.ydqp.common.receiveProtoMsg.mall.PayChannel;
import com.ydqp.common.sendProtoMsg.mall.PayChannelSuccess;
import com.ydqp.lobby.pay.CommonPay;

@ServerHandler(command = 1004002, module = "mall")
public class PayChannelHandler implements IServerHandler {
    private static Logger logger = LoggerFactory.getLogger(PayChannelHandler.class);
    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        logger.info("Get pay channel request: {}", JSONObject.toJSONString(abstartParaseMessage));
        PayChannel payChannel = (PayChannel) abstartParaseMessage;

        CommonPay commonPay = new CommonPay();
        PayChannelConfig payConfig = commonPay.getPayConfig();
        logger.info("当前支付渠道信息: {}", JSONObject.toJSONString(payConfig));

        PayChannelSuccess payChannelSuccess = new PayChannelSuccess();
        payChannelSuccess.setPayChannel(payConfig.getChannel());

        iSession.sendMessage(payChannelSuccess, payChannel);
    }
}
