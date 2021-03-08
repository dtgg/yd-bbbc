//package com.ydqp.lobby.handler.task;
//
//import com.cfq.annotation.ServerHandler;
//import com.cfq.connection.ISession;
//import com.cfq.handler.IServerHandler;
//import com.cfq.message.AbstartParaseMessage;
//import com.ydqp.common.data.PlayerData;
//import com.ydqp.common.entity.Player;
//import com.ydqp.common.receiveProtoMsg.task.TaskUpgrade;
//import com.ydqp.common.sendProtoMsg.CoinPointSuccess;
//import com.ydqp.common.sendProtoMsg.rank.UpgradeSuccess;
//import com.ydqp.lobby.cache.PlayerCache;
//import com.ydqp.lobby.service.PlayerService;
//
//@ServerHandler(command = 1002006, module = "task")
//public class TaskUpgradeHandler implements IServerHandler {
//    @Override
//    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
//        TaskUpgrade taskUpgrade = (TaskUpgrade) abstartParaseMessage;
//        long playerId = taskUpgrade.getPlayerId();
//        int originGrade = taskUpgrade.getOriginGrade();
//        int grade = taskUpgrade.getGrade();
//
//        if (originGrade >= 1000) return;
//
//        double reward = 0;
//        for (int i = originGrade + 1; i <= grade; i++) {
//            reward += 1000 + i * 100;
//        }
//
//        Player player = PlayerService.getInstance().queryByCondition(String.valueOf(playerId));
//        PlayerService.getInstance().updatePlayerCoinPoint(reward, playerId);
//
//        PlayerData playerData = PlayerCache.getInstance().getPlayer(taskUpgrade.getConnId());
//        if (playerData != null && playerData.getSessionId() != 0) {
//            playerData.setCoinPoint(playerData.getCoinPoint() + reward);
//            PlayerCache.getInstance().addPlayer(taskUpgrade.getConnId(), playerData);
//        }
//
//        UpgradeSuccess upgradeSuccess = new UpgradeSuccess();
//        upgradeSuccess.setPlayerId(playerId);
//        upgradeSuccess.setGrade(grade);
//        iSession.sendMessageByID(upgradeSuccess, taskUpgrade.getConnId());
//
//        CoinPointSuccess coinPointSuccess = new CoinPointSuccess();
//        coinPointSuccess.setPlayerId(playerId);
//        coinPointSuccess.setCoinType(1);
//        coinPointSuccess.setCoinPoint(player.getCoinPoint() + reward);
//        iSession.sendMessageByID(coinPointSuccess, taskUpgrade.getConnId());
//    }
//}
