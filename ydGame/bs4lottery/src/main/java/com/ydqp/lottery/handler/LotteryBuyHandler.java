package com.ydqp.lottery.handler;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.dao.lottery.LotteryDao;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.entity.*;
import com.ydqp.common.lottery.player.ILottery;
import com.ydqp.common.lottery.player.ManageLottery;
import com.ydqp.common.lottery.player.ManageLotteryRoom;
import com.ydqp.common.lottery.role.LotteryBattleRole;
import com.ydqp.common.receiveProtoMsg.lottery.LotteryBuy;
import com.ydqp.common.sendProtoMsg.CoinPointSuccess;
import com.ydqp.common.sendProtoMsg.lottery.LotteryBuySuc;
import com.ydqp.common.sendProtoMsg.lottery.LotteryQuitRoomSuc;
import com.ydqp.common.sendProtoMsg.lottery.PlayerLotteryInfo;
import com.ydqp.common.service.PlayerService;
import com.ydqp.common.utils.LotteryUtil;
import com.ydqp.lottery.Cache.LotteryCache;
import com.ydqp.lottery.ThreadManager;
import com.ydqp.lottery.task.PlayerPromoteTask;
import com.ydqp.lottery.util.DateUtil;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;

@ServerHandler(module = "lotteryBuy", command = 5000002)
public class LotteryBuyHandler implements IServerHandler {

    private static final Logger logger = LoggerFactory.getLogger(LotteryBuyHandler.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        LotteryBuy lotteryBuy = (LotteryBuy) abstartParaseMessage;

        LotteryBuySuc lotteryBuySuc = new LotteryBuySuc();

        if (ManageLottery.getInstance().getCloseServer() == 1) {
            String maintainTimeStr = LotteryCache.getInstance().getMaintainTime();
            int maintainTime = Integer.parseInt(maintainTimeStr);

            int nowTime = new Long(System.currentTimeMillis() / 1000).intValue();
            if (nowTime > maintainTime) {
                lotteryBuySuc.setSuccess(false);
                lotteryBuySuc.setMessage("System under maintenance");
                iSession.sendMessageByID(lotteryBuySuc, lotteryBuy.getConnId());
                return;
            }
        }

        if (StringUtils.isBlank(lotteryBuy.getPay())) {
            logger.info("下注金额为空, playerId:{}", lotteryBuy.getPlayerId());
            lotteryBuySuc.setSuccess(false);
            lotteryBuySuc.setMessage("The bet amount is empty");
            iSession.sendMessageByID(lotteryBuySuc, lotteryBuy.getConnId());
            return;
        }

        PlayerData playerData = PlayerCache.getInstance().getPlayer(lotteryBuy.getConnId());
        if (playerData == null) {
            logger.error("player is not true");
            lotteryBuySuc.setSuccess(false);
            lotteryBuySuc.setMessage("The player is not true");
            iSession.sendMessageByID(lotteryBuySuc, lotteryBuy.getConnId());
            return;
        }

        Lottery lottery = LotteryDao.getInstance().findById(lotteryBuy.getLotteryId());
        int nowTimestamp = new Long(System.currentTimeMillis() / 1000).intValue();

        if (nowTimestamp - lottery.getCreateTime() >= 150) {
            logger.info("购买时间已截至：lotteryId:{}, nowTimestamp:{}", lottery.getId(), nowTimestamp);
            lotteryBuySuc.setSuccess(false);
            lotteryBuySuc.setMessage("Time is up");
            iSession.sendMessageByID(lotteryBuySuc, lotteryBuy.getConnId());
            return;
        }

        long playerId = playerData.getPlayerId();
        Player player = PlayerService.getInstance().queryByPlayerId(playerId);
        if (player.getZjPoint() < Double.parseDouble(lotteryBuy.getPay())) {
            lotteryBuySuc.setSuccess(false);
            lotteryBuySuc.setMessage("Insufficient balance");
            iSession.sendMessageByID(lotteryBuySuc, lotteryBuy.getConnId());
            logger.info("用户余额不足, playerId:{}", playerId);
            return;
        }

        ILottery iLottery;
        if (player.getRoomId() == 0) {
            Integer roomId = ManageLotteryRoom.getInstance().getRoomId(lotteryBuy.getType());
            iLottery = ManageLottery.getInstance().getLotteryByRoomIdAndType(roomId, lotteryBuy.getType());
            iLottery.enterLottery(playerData, iSession);
        } else {
            iLottery = ManageLottery.getInstance().getLotteryByRoomIdAndType(player.getRoomId(), lotteryBuy.getType());
        }
        LotteryBattleRole lotteryBattleRole = ManageLottery.getLotteryBattleRoleMap().get(playerData.getPlayerId());
        if (lotteryBattleRole == null) {
            LotteryQuitRoomSuc lotteryQuitRoomSuc = new LotteryQuitRoomSuc();
            lotteryQuitRoomSuc.setPlayerId(playerData.getPlayerId());
            iSession.sendMessageByID(lotteryQuitRoomSuc, lotteryBuy.getConnId());
            return;
        }
        if (lotteryBattleRole.getBuying() != null && lotteryBattleRole.getBuying()) {
            lotteryBuySuc.setSuccess(false);
            lotteryBuySuc.setMessage("Frequent operation, please try later");
            iSession.sendMessageByID(lotteryBuySuc, lotteryBuy.getConnId());
            return;
        }
        lotteryBattleRole.setBuying(true);

        try {
            //扣钱
            double zj = Double.parseDouble(lotteryBuy.getPay());
            int row = PlayerService.getInstance().updatePlayerZjPoint(0 - zj, playerId);
            if (row == 0) {
                lotteryBuySuc.setSuccess(false);
                lotteryBuySuc.setMessage("Insufficient balance");
                iSession.sendMessageByID(lotteryBuySuc, lotteryBuy.getConnId());
                logger.error("用户账户异常余额不足，playerId:{}", playerId);
                return;
            }
            logger.info("用户购买彩票扣钱成功,playerId = {},彩票期数 = {}",player.getId(),(DateUtil.timestampToStr(lottery.getCreateTime()) + LotteryUtil.intToPeriod(lottery.getPeriod())));

            playerData.setZjPoint(playerData.getZjPoint() - zj);
            PlayerCache.getInstance().addPlayer(lotteryBuy.getConnId(), playerData);

            PlayerLottery playerLottery = new PlayerLottery();
            playerLottery.setPlayerId(playerId);
            playerLottery.setType(lotteryBuy.getType());
            playerLottery.setLotteryId(lotteryBuy.getLotteryId());
            playerLottery.setSelected(lotteryBuy.getSelect());
            playerLottery.setNumber(lotteryBuy.getNumber());
            playerLottery.setPay(new BigDecimal(lotteryBuy.getPay()));
            playerLottery.setAppId(player.getAppId());
            playerLottery.setKfId(player.getKfId());
            playerLottery.setIsVir(player.getIsVir());
            playerLottery.setRegisterTime(player.getCreateTime());

            playerLottery.setPeriod(DateUtil.timestampToStr(lottery.getCreateTime()) + LotteryUtil.intToPeriod(lottery.getPeriod()));

            PlayerLottery playerLottery1 = iLottery.lotteryBuy(lottery, playerLottery);

            logger.info("彩票购买成功，用户ID:{}, 抽奖前zj:{}, 抽奖后zj:{}, 期数:{}, 颜色:{}, 数字:{}",
                    player.getId(), player.getZjPoint(), player.getZjPoint() - zj, playerLottery.getPeriod(),
                    playerLottery.getSelected(), playerLottery.getNumber());

            //通知客户端
            CoinPointSuccess coinPointSuccess = new CoinPointSuccess();
            coinPointSuccess.setPlayerId(playerId);
            coinPointSuccess.setCoinPoint(player.getZjPoint() - zj);
            iSession.sendMessageByID(coinPointSuccess, lotteryBuy.getConnId());

            lotteryBuySuc.setSuccess(true);
            lotteryBuySuc.setMessage("Successful");
            lotteryBuySuc.setPlayerLotteryInfo(new PlayerLotteryInfo(playerLottery1));
            iSession.sendMessageByID(lotteryBuySuc, lotteryBuy.getConnId());

            //推广
            PlayerPromoteDetail playerPromoteDetail = new PlayerPromoteDetail();
            playerPromoteDetail.setPlayerId(playerId);
            playerPromoteDetail.setNickname(player.getNickname());
            playerPromoteDetail.setBetAmount(playerLottery1.getPay().subtract(playerLottery1.getFee()));
            playerPromoteDetail.setCreateTime(playerLottery1.getCreateTime());
            ThreadManager.getInstance().getExecutor().execute(new PlayerPromoteTask(iSession, playerPromoteDetail, playerLottery1.getFee()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lotteryBattleRole.setBuying(false);
        }
    }
}
