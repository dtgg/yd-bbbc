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
import com.ydqp.common.receiveProtoMsg.mall.PlayerOrder;
import com.ydqp.common.sendProtoMsg.mall.PlayerOrderSuccess;
import com.ydqp.lobby.cache.MallCache;
import com.ydqp.lobby.cache.PlayerCache;
import com.ydqp.lobby.pay.CommonPay;
import com.ydqp.lobby.pay.OrderPay;
import com.ydqp.lobby.service.mall.PayChannelConfigService;
import com.ydqp.lobby.service.mall.PlayerOrderService;
import com.ydqp.lobby.service.mall.ProductService;
import com.ydqp.lobby.utils.PayUtil;
import org.apache.commons.collections.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@ServerHandler(command = 1004002, module = "mall")
public class PlayerOrderHandler implements IServerHandler {
    private static final Logger logger = LoggerFactory.getLogger(PlayerOrderHandler.class);

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    private static final String ORDER = "ORDER:PLAYERID:";

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        logger.info("Player pay request: {}", JSONObject.toJSONString(abstartParaseMessage));
        PlayerOrder playerOrder = (PlayerOrder) abstartParaseMessage;

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
            PayChannelConfig payChannelConfig = PayUtil.getInstance().getConfig(configs, playerData.getPlayerId());
            logger.info("支付渠道信息，payChannelConfig:{}", JSON.toJSONString(payChannelConfig));

            com.ydqp.common.entity.PlayerOrder order = new com.ydqp.common.entity.PlayerOrder();
            String orderId = UUID.randomUUID().toString().replace("-", "");
            order.setOrderId(orderId);
            order.setPlayerId(playerData.getPlayerId());
            order.setProductId(playerOrder.getProductId());
            order.setAmount(product.getAmount());
            order.setStatus(0);
            order.setTxnOrderId(orderId);
            order.setCreateTime(new Long(System.currentTimeMillis() / 1000).intValue());
            order.setAppId(playerData.getAppId());
            order.setRegisterTime(playerData.getRegisterTime());
            order.setPayChannel(payChannelConfig.getAliasName());
            order.setKfId(playerData.getKfId());

            OrderPay orderPay = new CommonPay().getOrderPay(payChannelConfig.getName());
            if (orderPay == null) {
                order.setStatus(2);
                order.setMsg("Unable to process payment request");
                orderSuccess.setSuccess(false);
                orderSuccess.setMessage("Unable to process payment request");
            } else {
                orderSuccess = orderPay.payment(order, payChannelConfig);
                PlayerOrderService.getInstance().saveOrder(order.getParameterMap());
            }
        } else {
            orderSuccess.setSuccess(false);
            orderSuccess.setMessage("No payment channel");
            logger.error("充值失败，支付渠道配置信息不存在");
        }
        iSession.sendMessageByID(orderSuccess, playerOrder.getConnId());
    }
}
