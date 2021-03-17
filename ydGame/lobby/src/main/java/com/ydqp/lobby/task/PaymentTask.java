package com.ydqp.lobby.task;

import com.alibaba.fastjson.JSONObject;
import com.cfq.connection.ISession;
import com.cfq.connection.ManagerSession;
import com.cfq.connection.NettySession;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.ydqp.common.constant.UpLoadConstant;
import com.ydqp.common.dao.PaySuccessDealDao;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.entity.PaySuccessDeal;
import com.ydqp.common.entity.Player;
import com.ydqp.common.sendProtoMsg.CoinPointSuccess;
import com.ydqp.common.task.StatisticsUploadTask;
import com.ydqp.lobby.ThreadManager;
import com.ydqp.lobby.cache.PlayerCache;
import com.ydqp.lobby.service.PlayerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PaymentTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(PaymentTask.class);

    @Override
    public void run() {
        try {
            //查询所有未处理支付数据
            List<PaySuccessDeal> allNotDeal = PaySuccessDealDao.getInstance().findAllNotDeal();
            allNotDeal.forEach(paySuccessDeal -> {
                long playerId = paySuccessDeal.getPlayerId();
                double point = paySuccessDeal.getPoint();
                logger.info("paySuccessDeal，playerId:{}, point:{}, payType:{}", playerId, point, paySuccessDeal.getPayType());
                try {
                    int appId = 0;
                    int registerTime = 0;
                    PlayerData playerData = PlayerCache.getInstance().getPlayerByPlayerID(playerId);
                    //不在线
                    if (playerData == null || playerData.getPlayerId() == 0) {
                        Player player = PlayerService.getInstance().queryByCondition(String.valueOf(playerId));
                        if (player == null) return;
                        PlayerService.getInstance().updatePlayerZjPoint(point, playerId);
                        PaySuccessDealDao.getInstance().setDealSuccess(paySuccessDeal.getId());

                        appId = player.getAppId();
                        registerTime = player.getCreateTime();
                    } else {
                        PlayerData playerData1 = PlayerCache.getInstance().getPlayer(playerData.getSessionId());
                        if (playerData1 == null || playerData1.getPlayerId() == 0) {
                            Player player = PlayerService.getInstance().queryByCondition(String.valueOf(playerId));
                            PlayerService.getInstance().updatePlayerZjPoint(point, playerId);
                            PaySuccessDealDao.getInstance().setDealSuccess(paySuccessDeal.getId());

                            appId = player.getAppId();
                            registerTime = player.getCreateTime();
                        } else {
                            long sessionId = playerData1.getSessionId();
                            PlayerService.getInstance().updatePlayerZjPoint(point, playerId);

                            point = point + playerData1.getZjPoint();
                            playerData1.setZjPoint(point);
                            PlayerCache.getInstance().addPlayer(sessionId, playerData1);

                            PaySuccessDealDao.getInstance().setDealSuccess(paySuccessDeal.getId());

                            CoinPointSuccess coinPointSuccess = new CoinPointSuccess();
                            coinPointSuccess.setPlayerId(playerId);
                            coinPointSuccess.setCoinPoint(point);

                            Map<Long, ISession> sessionMap = ManagerSession.getInstance().getSessionMap();
                            NettySession iSession = (NettySession) sessionMap.get(new ArrayList<>(sessionMap.keySet()).get(0));
                            iSession.sendMessageByID(coinPointSuccess, sessionId);

                            appId = playerData1.getAppId();
                            registerTime = playerData1.getRegisterTime();
                        }
                    }

                    if (paySuccessDeal.getPayType() == 0) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("playerId", playerId);
                        jsonObject.put("appId", appId);
                        jsonObject.put("registerTime", registerTime);
                        jsonObject.put("amount", paySuccessDeal.getPoint());
                        jsonObject.put("createTime", new Long(System.currentTimeMillis() / 1000).intValue());
                        ThreadManager.getInstance().getStatUploadExecutor().execute(new StatisticsUploadTask(UpLoadConstant.ORDER, jsonObject));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("payment deal error, playerId: {}", playerId);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("payment deal error, msg: {}", e.getMessage());
        }
    }
}
