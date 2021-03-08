package com.ydqp.lobby.handler.mall;

import com.alibaba.fastjson.JSONObject;
import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.ydqp.common.dao.PaySuccessDealDao;
import com.ydqp.common.entity.*;
import com.ydqp.common.receiveProtoMsg.mall.OrderCallback;
import com.ydqp.common.utils.HttpUtils;
import com.ydqp.lobby.service.mall.PayChannelConfigService;
import com.ydqp.lobby.service.mall.PlayerOrderService;
import com.ydqp.lobby.service.mall.ProductService;
import com.ydqp.lobby.service.mall.PromotionService;

import java.util.HashMap;
import java.util.Map;

@ServerHandler(command = 1004009, module = "mall")
public class OrderCallbackHandler implements IServerHandler {

    private static final Logger logger = LoggerFactory.getLogger(OrderCallbackHandler.class);

    private static final String requestAddress = "https://api.razorpay.com/v1/payments/";

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
//        OrderCallback callback = (OrderCallback) abstartParaseMessage;
//
//        String payId = callback.getOrderId();
//
//        PayChannelConfig payChannelConfig = PayChannelConfigService.getInstance().findByName("razorpay");
//        String basic = payChannelConfig.getAppId() + ":" + payChannelConfig.getSecretKey();
//        String basicAuth = "Basic " + Base64.encode(basic.getBytes());
//
//        Map<String, String> headMap = new HashMap<>();
//        headMap.put("Authorization", basicAuth);
//        logger.info("支付ID：{}", payId);
//        String s = HttpUtils.getInstance().sendGet(requestAddress + payId, headMap, null);
//        logger.info("查询订单：{}", s);
//
//        JSONObject response = JSONObject.parseObject(s);
//        if ("captured".equals(response.getString("status"))) {
//            String orderId = response.getString("order_id");
//            PlayerOrder playerOrder = PlayerOrderService.getInstance().queryOrder(orderId);
//            if (playerOrder == null) {
//                logger.error("订单不存在; orderId: {}", orderId);
//                return;
//            }
//            if (playerOrder.getPayStatus() != 0) {
//                logger.error("订单已处理; orderId: {}", orderId);
//                return;
//            }
//
//            //update playerOrder
//            Object[] params = new Object[]{1, playerOrder.getId()};
//            PlayerOrderService.getInstance().updateOrder(params);
//
//            Product product = ProductService.getInstance().findById(playerOrder.getProductId());
//            if (product == null) {
//                logger.error("订单商品不存在; productId: {}, playerId: {}", playerOrder.getProductId(), playerOrder.getPlayerId());
//                return;
//            }
//            Promotion promotion = PromotionService.getInstance().findByProductId(product.getId());
//            PaySuccessDeal paySuccessDeal = new PaySuccessDeal();
//            paySuccessDeal.setPlayerId(playerOrder.getPlayerId());
//            paySuccessDeal.setOrderId((int) playerOrder.getId());
//            paySuccessDeal.setType(product.getType());
//            paySuccessDeal.setCreateTime(new Long(System.currentTimeMillis() / 1000L).intValue());
//            paySuccessDeal.setPayType(0);
//            if (promotion == null) {
//                paySuccessDeal.setPoint(product.getPoint());
//                PaySuccessDealDao.getInstance().save(paySuccessDeal.getParameterMap());
//            } else {
//                ProductBuyHistory productBuyHistory = ProductService.getInstance().findHistoryByPlayerIdAndPromotionId(playerOrder.getPlayerId(), promotion.getId());
//                if (productBuyHistory == null || productBuyHistory.getBuyNum() < promotion.getLimit()) {
//                    paySuccessDeal.setPoint(promotion.getPromotionPoint());
//                } else {
//                    paySuccessDeal.setPoint(product.getPoint());
//                }
//                PaySuccessDealDao.getInstance().save(paySuccessDeal.getParameterMap());
//
//                if (productBuyHistory == null) {
//                    productBuyHistory = new ProductBuyHistory();
//                    productBuyHistory.setPlayerId(playerOrder.getPlayerId());
//                    productBuyHistory.setPromotionId(promotion.getId());
//                    productBuyHistory.setBuyNum(1);
//                    productBuyHistory.setCreateTime(new Long(System.currentTimeMillis() / 1000L).intValue());
//                    ProductService.getInstance().saveProductBuyHistory(productBuyHistory.getParameterMap());
//                } else {
//                    ProductService.getInstance().updateProductBuyHistory(1, productBuyHistory.getId());
//                }
//            }
//        }
    }
}
