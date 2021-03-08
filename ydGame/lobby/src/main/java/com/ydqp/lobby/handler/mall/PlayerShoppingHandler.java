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
import com.ydqp.common.entity.Product;
import com.ydqp.common.receiveProtoMsg.mall.PlayerShopping;
import com.ydqp.common.sendProtoMsg.mall.PlayerShoppingSuccess;
import com.ydqp.lobby.cache.MallCache;
import com.ydqp.lobby.cache.PlayerCache;
import com.ydqp.lobby.pay.ChannelConstant;
import com.ydqp.lobby.pay.cashfree.CashFreePay;
import com.ydqp.lobby.service.mall.PayChannelConfigService;
import com.ydqp.lobby.service.mall.PlayerOrderService;
import com.ydqp.lobby.service.mall.ProductService;
import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ServerHandler(command = 1004004, module = "mall")
public class PlayerShoppingHandler implements IServerHandler {
    private static Logger logger = LoggerFactory.getLogger(PlayerShoppingHandler.class);

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    private static final String ORDER = "ORDER:PLAYERID:";

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        logger.info("Player pay request: {}", JSONObject.toJSONString(abstartParaseMessage));
        PlayerShopping playerShopping = (PlayerShopping) abstartParaseMessage;

        PlayerShoppingSuccess shoppingSuccess = new PlayerShoppingSuccess();

        PlayerData playerData = PlayerCache.getInstance().getPlayer(playerShopping.getConnId());
        if (playerData == null) {
            shoppingSuccess.setSuccess(false);
            shoppingSuccess.setMessage("The player don`t exist");
            iSession.sendMessageByID(shoppingSuccess, playerShopping.getConnId());
            logger.error("充值失败，玩家不在线，playerId:{}", playerShopping.getPlayerId());
            return;
        }

        String key = ORDER + playerData.getPlayerId() + ":" + sdf.format(new Date());
        Long withdrawalCount = MallCache.getInstance().incrWithdrawalCount(key);
        if (withdrawalCount > 20) {
            shoppingSuccess.setSuccess(false);
            shoppingSuccess.setMessage("Too frequent purchases or withdrawals, please try again the next day");
            iSession.sendMessageByID(shoppingSuccess, playerShopping.getConnId());
            logger.error("充值失败，购买次数达到上限，playerId:{}", playerShopping.getPlayerId());
            return;
        }

        Product product = ProductService.getInstance().findById(playerShopping.getProductId());
        if (product == null) {
            shoppingSuccess.setSuccess(false);
            shoppingSuccess.setMessage("The Product don`t exist");
            iSession.sendMessageByID(shoppingSuccess, playerShopping.getConnId());
            logger.error("充值失败，商品不存在，playerId:{}，productId", playerShopping.getPlayerId(), playerShopping.getProductId());
            return;
        }

        PayChannelConfig payChannelConfig = PayChannelConfigService.getInstance().findByChannel(playerShopping.getPayChannel());
        logger.info("支付渠道信息，payChannelConfig:{}", JSON.toJSONString(payChannelConfig));
        if (payChannelConfig != null) {
            playerShopping.setAmount(product.getAmount());

            String orderId = UUID.randomUUID().toString().replace("-", "");
            Map<String, Object> params = new HashMap<String, Object>() {{
                put("orderId", orderId);
                put("playerId", playerData.getPlayerId());
                put("name", playerShopping.getName());
                put("mobile", playerShopping.getMobile());
                put("payerVA", playerShopping.getPayerVA());
                put("productId", playerShopping.getProductId());
                put("amount", playerShopping.getAmount());
                put("orderTime", new Long(System.currentTimeMillis() / 1000).intValue());
                put("payStatus", 0);
                put("appId", playerData.getAppId());
                put("registerTime", playerData.getRegisterTime());
            }};

            switch (payChannelConfig.getChannel()) {
                case ChannelConstant.CASH_FREE:
                    JSONObject result = cashFreePayment(playerShopping, payChannelConfig, orderId, params);
                    if ("OK".equals(result.getString("status"))) {
                        shoppingSuccess.setSuccess(true);
                        shoppingSuccess.setToken(result.getString("cftoken"));
                        shoppingSuccess.setStage("PROD");//PROD/TEST
                        shoppingSuccess.setAppId(payChannelConfig.getAppId());
                        shoppingSuccess.setOrderId(orderId);
                        shoppingSuccess.setOrderCurrency("INR");
                        shoppingSuccess.setOrderAmount(product.getAmount());
                        shoppingSuccess.setNotifyUrl(payChannelConfig.getPaymentNotifyUrl());

                        params.put("cftoken", result.getString("cftoken"));
                    } else {
                        shoppingSuccess.setSuccess(false);
                        shoppingSuccess.setMessage(result.getString("message"));
                    }
                    break;
                default:
            }
            PlayerOrderService.getInstance().saveOrder(params);
            logger.info("生成支付订单：{}", JSON.toJSONString(params));

            //数据上报
//            ThreadManager.getInstance().getStatUploadExecutor().execute(new StatisticsUploadTask(UpLoadConstant.ORDER, new JSONObject(params)));
        } else {
            shoppingSuccess.setSuccess(false);
            shoppingSuccess.setMessage("Require pay channel config failed");
            logger.error("支付渠道配置信息不存在");
        }
        iSession.sendMessageByID(shoppingSuccess, playerShopping.getConnId());
    }

    private JSONObject cashFreePayment(PlayerShopping playerShopping, PayChannelConfig payChannelConfig,
                                                String orderId, Map<String, Object> params) {
        Map<String, String> header = new HashMap<String, String>() {{
            put("X-Client-Id", payChannelConfig.getAppId());
            put("X-Client-Secret", payChannelConfig.getSecretKey());
            put("Content-Type", "application/json");
        }};
        JSONObject body = new JSONObject() {{
            put("orderId", orderId);
            put("orderAmount", playerShopping.getAmount());
            put("orderCurrency", "INR");
        }};

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("head", header);
        jsonObj.put("body", body);

        CashFreePay cashFreePay = new CashFreePay();
        String result = cashFreePay.payment(jsonObj.toJSONString());
        return JSONObject.parseObject(result);
    }
}
