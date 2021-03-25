package com.ydqp.lobby.task;

import com.cfq.connection.ISession;
import com.cfq.connection.ManagerSession;
import com.cfq.connection.NettySession;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.dao.PaySuccessDealDao;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.entity.PaySuccessDeal;
import com.ydqp.common.entity.Player;
import com.ydqp.common.sendProtoMsg.CoinPointSuccess;
import com.ydqp.common.sendProtoMsg.Refresh;
import com.ydqp.common.utils.CommonUtils;
import com.ydqp.lobby.service.PlayerService;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class PaymentTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(PaymentTask.class);

    @Override
    public void run() {
        try {
            //查询所有未处理支付数据
            List<PaySuccessDeal> allNotDeal = PaySuccessDealDao.getInstance().findAllNotDeal();
            if (CollectionUtils.isEmpty(allNotDeal)) return;

            List<Integer> dealIds = allNotDeal.stream().map(deal -> new Long(deal.getId()).intValue()).collect(Collectors.toList());
            String s = CommonUtils.inString(dealIds);
            PaySuccessDealDao.getInstance().setDealSuccess(s);

            Map<Long, Double> playerZjMap = new HashMap<>();

            allNotDeal.forEach(paySuccessDeal -> {
                long playerId = paySuccessDeal.getPlayerId();
                double point = paySuccessDeal.getPoint();
                logger.info("paySuccessDeal，playerId:{}, point:{}, payType:{}", playerId, point, paySuccessDeal.getPayType());
                try {
                    PlayerData playerData = PlayerCache.getInstance().getPlayerByPlayerID(playerId);
                    //不在线
                    if (playerData == null || playerData.getPlayerId() == 0) {
                        Player player = PlayerService.getInstance().queryByPlayerId(playerId);
                        if (player == null) return;
//                        PlayerService.getInstance().updatePlayerZjPoint(point, playerId);
                        if (playerZjMap.get(playerId) == null) {
                            playerZjMap.put(playerId, point);
                        } else {
                            playerZjMap.put(playerId, playerZjMap.get(playerId) + point);
                        }
                    } else {
                        PlayerData playerData1 = PlayerCache.getInstance().getPlayer(playerData.getSessionId());
                        if (playerData1 == null || playerData1.getPlayerId() == 0) {
                            Player player = PlayerService.getInstance().queryByPlayerId(playerId);
                            if (player == null) return;
//                            PlayerService.getInstance().updatePlayerZjPoint(point, playerId);
                            if (playerZjMap.get(playerId) == null) {
                                playerZjMap.put(playerId, point);
                            } else {
                                playerZjMap.put(playerId, playerZjMap.get(playerId) + point);
                            }
                        } else {
                            long sessionId = playerData1.getSessionId();
//                            PlayerService.getInstance().updatePlayerZjPoint(point, playerId);
                            if (playerZjMap.get(playerId) == null) {
                                playerZjMap.put(playerId, point);
                            } else {
                                playerZjMap.put(playerId, playerZjMap.get(playerId) + point);
                            }

                            Player player = PlayerService.getInstance().queryByPlayerId(playerId);
                            point = point + player.getZjPoint();
                            playerData1.setZjPoint(point);
                            PlayerCache.getInstance().addPlayer(sessionId, playerData1);

                            CoinPointSuccess coinPointSuccess = new CoinPointSuccess();
                            coinPointSuccess.setPlayerId(playerId);
                            coinPointSuccess.setCoinPoint(point);

                            Map<Long, ISession> sessionMap = ManagerSession.getInstance().getSessionMap();
                            NettySession iSession = (NettySession) sessionMap.get(new ArrayList<>(sessionMap.keySet()).get(0));
                            iSession.sendMessageByID(coinPointSuccess, sessionId);

                            if (paySuccessDeal.getPayType() == 1) {
                                Refresh refresh = new Refresh();
                                refresh.setType(1);
                                iSession.sendMessageByID(refresh, sessionId);
                            }
                        }
                    }

//                    if (paySuccessDeal.getPayType() == 1) {
//                        JSONObject jsonObject = new JSONObject();
//                        jsonObject.put("playerId", playerId);
//                        jsonObject.put("appId", appId);
//                        jsonObject.put("registerTime", registerTime);
//                        jsonObject.put("amount", paySuccessDeal.getPoint());
//                        jsonObject.put("createTime", new Long(System.currentTimeMillis() / 1000).intValue());
//                        ThreadManager.getInstance().getStatUploadExecutor().execute(new StatisticsUploadTask(UpLoadConstant.ORDER, jsonObject));
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("payment deal error, dealId: {}, msg: {}", paySuccessDeal.getId(), e.getMessage());
                }
            });

            Object[][] params = new Object[playerZjMap.size()][];
            AtomicInteger i = new AtomicInteger();
            playerZjMap.forEach((k, v) -> {
                params[i.get()] = new Object[]{v, k};
                i.getAndIncrement();
            });
            PlayerService.getInstance().batchUpdatePlayerZjPoint(params);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("payment deal error, msg: {}", e.getMessage());
        }
    }
}
