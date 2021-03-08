package com.ydqp.lobby.handler.mall;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.entity.PayChannelConfig;
import com.ydqp.common.entity.PlayerOrder;
import com.ydqp.common.entity.Product;
import com.ydqp.common.receiveProtoMsg.mall.PlayerOrders;
import com.ydqp.common.sendProtoMsg.mall.PlayerOrderSuccess;
import com.ydqp.lobby.cache.MallCache;
import com.ydqp.lobby.cache.PlayerCache;
import com.ydqp.lobby.pay.ChannelConstant;
import com.ydqp.lobby.pay.cashfree.CashFreePayNew;
import com.ydqp.lobby.pay.razorpay.RazorPay;
import com.ydqp.lobby.service.mall.PayChannelConfigService;
import com.ydqp.lobby.service.mall.PlayerOrderService;
import com.ydqp.lobby.service.mall.ProductService;
import com.ydqp.lobby.utils.PayUtil;
import org.apache.commons.collections.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@ServerHandler(command = 1004201, module = "mall")
public class PlayerOrderHandler implements IServerHandler {
    private static final Logger logger = LoggerFactory.getLogger(PlayerOrderHandler.class);

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    private static final String ORDER = "ORDER:PLAYERID:";

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        logger.info("Player pay request: {}", JSONObject.toJSONString(abstartParaseMessage));
        PlayerOrders playerOrder = (PlayerOrders) abstartParaseMessage;

        PlayerOrderSuccess orderSuccess = new PlayerOrderSuccess();

        PlayerData playerData = PlayerCache.getInstance().getPlayer(playerOrder.getConnId());
        if (playerData == null) {
            orderSuccess.setSuccess(false);
            orderSuccess.setMessage("The player don`t exist");
            iSession.sendMessageByID(orderSuccess, playerOrder.getConnId());
            logger.error("充值失败，玩家不在线，playerId:{}", playerOrder.getPlayerId());
            return;
        }

        String key = ORDER + playerData.getPlayerId() + ":" + sdf.format(new Date());
        Long withdrawalCount = MallCache.getInstance().incrWithdrawalCount(key);
        if (withdrawalCount > 20) {
            orderSuccess.setSuccess(false);
            orderSuccess.setMessage("Too frequent purchases or withdrawals, please try again the next day");
            iSession.sendMessageByID(orderSuccess, playerOrder.getConnId());
            logger.error("充值失败，购买次数达到上限，playerId:{}", playerOrder.getPlayerId());
            return;
        }

        Product product = ProductService.getInstance().findById(playerOrder.getProductId());
        if (product == null) {
            orderSuccess.setSuccess(false);
            orderSuccess.setMessage("The Product don`t exist");
            iSession.sendMessageByID(orderSuccess, playerOrder.getConnId());
            logger.error("充值失败，商品不存在，playerId:{}，productId", playerOrder.getPlayerId(), playerOrder.getProductId());
            return;
        }

        List<PayChannelConfig> configs = PayChannelConfigService.getInstance().getAllEnableChannel();
        if (CollectionUtils.isNotEmpty(configs)) {
            PayChannelConfig payChannelConfig = PayUtil.getInstance().getConfig(configs);
//            PayChannelConfig payChannelConfig = PayChannelConfigService.getInstance().findByName("razorpay");
            logger.info("支付渠道信息，payChannelConfig:{}", JSON.toJSONString(payChannelConfig));

            PlayerOrder order = new PlayerOrder();
            order.setOrderId(UUID.randomUUID().toString().replace("-", ""));
            order.setPlayerId(playerData.getPlayerId());
            order.setName(playerOrder.getName());
            order.setMobile(playerOrder.getMobile());
            order.setPayerVA(playerOrder.getPayerVA());
            order.setProductId(playerOrder.getProductId());
            order.setAmount(product.getAmount());
            order.setOrderTime(new Long(System.currentTimeMillis() / 1000).intValue());
            order.setPayStatus(0);
            order.setAppId(playerData.getAppId());
            order.setRegisterTime(playerData.getRegisterTime());
            order.setPlatformPayChannel(payChannelConfig.getChannel());

            Map<String, Object> params = order.getParameterMap();
            params.put("clientId", payChannelConfig.getAppId());
            params.put("secretKey", payChannelConfig.getSecretKey());
            switch (payChannelConfig.getChannel()) {
                case ChannelConstant.CASH_FREE:
                    String cashFreeResultStr = new CashFreePayNew().payment(JSON.toJSONString(params));
                    JSONObject cashFreeResult = JSONObject.parseObject(cashFreeResultStr);
                    if ("OK".equals(cashFreeResult.getString("status"))) {
                        orderSuccess.setSuccess(true);

                        JSONObject cashFreeResp = new JSONObject() {{
                            put("token", cashFreeResult.getString("cftoken"));
                            put("stage", "PROD");//PROD/TEST
                            put("appId", payChannelConfig.getAppId());
                            put("orderId", order.getOrderId());
                            put("orderCurrency", "INR");
                            put("orderAmount", product.getAmount());
                            put("notifyUrl", payChannelConfig.getPaymentNotifyUrl());
                        }};
                        orderSuccess.setPayChannel("cashfree");
                        orderSuccess.setData(cashFreeResp.toJSONString());
                        order.setCftoken(cashFreeResult.getString("cftoken"));
                    } else {
                        orderSuccess.setSuccess(false);
                        orderSuccess.setMessage(cashFreeResult.getString("message"));
                    }
                    break;
                case ChannelConstant.RAZOR_PAY:
                    String razorPayResultStr = new RazorPay().payment(JSON.toJSONString(params));
                    JSONObject razorPayResult = JSONObject.parseObject(razorPayResultStr);
                    if ("created".equals(razorPayResult.getString("status"))) {
                        orderSuccess.setSuccess(true);
//                        String paymentUrl = payChannelConfig.getPaymentUrl() + "appId=" + payChannelConfig.getAppId() + "&orderId=" + razorPayResult.getString("id");
                        JSONObject razorResp = new JSONObject() {{
                            put("appId", payChannelConfig.getAppId());
                            put("orderId", razorPayResult.getString("id"));
                            put("orderCurrency", "INR");
                            put("orderAmount", product.getAmount() * 100);
                            put("notifyUrl", payChannelConfig.getPaymentNotifyUrl());
                        }};
                        orderSuccess.setPayChannel("razorpay");
                        orderSuccess.setData(razorResp.toJSONString());
                        order.setTxnOrderId(razorPayResult.getString("id"));
                    } else {
                        orderSuccess.setSuccess(false);
                        orderSuccess.setMessage(razorPayResult.getString("message"));
                    }
                    break;
                case ChannelConstant.YAAR_PAY:
                    String orderId = order.getOrderId().substring(0, 30);
                    order.setOrderId(orderId);

                    orderSuccess.setSuccess(true);
                    orderSuccess.setPayChannel("yaarpay");
                    orderSuccess.setData(payChannelConfig.getPaymentUrl() + "orderId=" + orderId);
                    break;
                default:
            }
            order.getParameterMap().remove("clientId");
            order.getParameterMap().remove("secretKey");
            PlayerOrderService.getInstance().saveOrder(order.getParameterMap());
            logger.info("生成支付订单：{}", JSON.toJSONString(order));
        } else {
            orderSuccess.setSuccess(false);
            orderSuccess.setMessage("Require pay channel config failed");
            logger.error("支付渠道配置信息不存在");
        }
        iSession.sendMessageByID(orderSuccess, playerOrder.getConnId());
    }
}
