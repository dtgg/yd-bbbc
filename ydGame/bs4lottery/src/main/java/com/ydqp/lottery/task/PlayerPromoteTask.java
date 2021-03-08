package com.ydqp.lottery.task;

import com.cfq.connection.ISession;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.dao.PlayerOrderDao;
import com.ydqp.common.dao.lottery.PlayerLotteryDao;
import com.ydqp.common.dao.lottery.PlayerPromoteDao;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.data.Total;
import com.ydqp.common.entity.PlayerPromote;
import com.ydqp.common.entity.PlayerPromoteConfig;
import com.ydqp.common.entity.PlayerPromoteDetail;
import com.ydqp.common.sendProtoMsg.CoinPointSuccess;
import com.ydqp.common.sendProtoMsg.lottery.ParityOrderSuc;
import com.ydqp.common.service.PlayerService;
import com.ydqp.common.utils.ShortCodeKit;
import com.ydqp.lottery.ManagePlayerPromote;
import com.ydqp.lottery.dao.PlayerPromoteDetailDao;

import java.math.BigDecimal;

public class PlayerPromoteTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(PlayerPromoteTask.class);

    private final ISession iSession;

    private final PlayerPromoteDetail playerPromoteDetail;

    public PlayerPromoteTask(ISession iSession, PlayerPromoteDetail playerPromoteDetail) {
        this.iSession = iSession;
        this.playerPromoteDetail = playerPromoteDetail;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        //查询player_promote
        PlayerPromote playerPromote = PlayerPromoteDao.getInstance().findByPlayerId(playerPromoteDetail.getPlayerId());
        if (playerPromote == null) return;

        //player是否是有效用户
        boolean isEffective = false;
        boolean isNew = false;
        if (playerPromote.getIsEffective() == 0) {
            Total total = PlayerOrderDao.getInstance().countByPlayId(playerPromoteDetail.getPlayerId());
            if (total.getTotal() > 0) {
                playerPromote.setIsEffective(1);
                isEffective = true;
                isNew = true;
                logger.info("新的有效用户，playerId:{}", playerPromoteDetail.getPlayerId());
            }
        } else {
            isEffective = true;
        }

        PlayerPromote superior = new PlayerPromote();
        int superiorTotal = 0;
        if (isEffective) {
            //获取上级奖金比例的配置
            PlayerPromoteConfig config = ManagePlayerPromote.getInstance().getConfig();

            //计算上级获得的奖金，保存player_promote_detail
            if (playerPromote.getSuperiorId() != null) {
                playerPromoteDetail.setSuperiorAmount(playerPromoteDetail.getBetAmount().multiply(new BigDecimal(config.getSuperiorRate())));

                superior = PlayerPromoteDao.getInstance().findByPlayerId(playerPromote.getGrandId());
                superiorTotal = PlayerLotteryDao.getInstance().countByPlayerId(playerPromote.getSuperiorId()).getTotal();
            }
            //superior是有效用户
            if (playerPromote.getGrandId() != null && superior.getIsEffective() == 1 && superiorTotal > 0) {
                playerPromoteDetail.setGrandAmount(playerPromoteDetail.getBetAmount().multiply(new BigDecimal(config.getGrandRate())));
            }
        }
        PlayerPromoteDetailDao.getInstance().insert(playerPromoteDetail.getParameterMap());

        if (!isEffective) {
            logger.info("该用户不是有效用户，playerId:{}", playerPromoteDetail.getPlayerId());
            return;
        }

        Object[] param = new Object[]{
                playerPromoteDetail.getSuperiorAmount(),
                playerPromoteDetail.getGrandAmount(),
                playerPromote.getIsEffective(),
                playerPromote.getId()
        };
        PlayerPromoteDao.getInstance().update(param);

        //新增有效用户
        if (isNew) {
            //上级玩家的一级有效用户加一，2级有效用户加上当前玩家的一级有效用户数量
            if (playerPromote.getSuperiorId() != null && playerPromote.getSuperiorId() != 0) {
                PlayerPromoteDao.getInstance().updateSubEffective(playerPromote.getSuperiorId(), playerPromote.getSubNum());
            }
            if (playerPromote.getGrandId() != null && playerPromote.getGrandId() != 0 && superior.getIsEffective() == 1 && superiorTotal > 0) {
                PlayerPromoteDao.getInstance().updateSonEffective(playerPromote.getGrandId());
            }
        }

        //给上级玩家加钱
        if (playerPromote.getSuperiorId() != null && playerPromote.getSuperiorId() != 0) {
            PlayerService.getInstance().updatePlayerZjPoint(playerPromoteDetail.getSuperiorAmount().doubleValue(), playerPromote.getSuperiorId());

            PlayerData data = PlayerCache.getInstance().getPlayerByPlayerID(playerPromote.getSuperiorId());
            if (data != null) {
                PlayerData playerData = PlayerCache.getInstance().getPlayer(data.getSessionId());
                if (playerData != null) {
                    playerData.setZjPoint(playerData.getZjPoint() + playerPromoteDetail.getSuperiorAmount().doubleValue());
                    PlayerCache.getInstance().addPlayer(data.getSessionId(), playerData);

                    sendToSuperiorAndGrand(playerData);
                }
            }
        }
        if (playerPromote.getGrandId() != null && playerPromote.getGrandId() != 0 && superior.getIsEffective() == 1 && superiorTotal > 0) {
            PlayerService.getInstance().updatePlayerZjPoint(playerPromoteDetail.getGrandAmount().doubleValue(), playerPromote.getGrandId());

            PlayerData data = PlayerCache.getInstance().getPlayerByPlayerID(playerPromote.getGrandId());
            if (data != null) {
                PlayerData playerData = PlayerCache.getInstance().getPlayer(data.getSessionId());
                if (playerData != null) {
                    playerData.setZjPoint(playerData.getZjPoint() + playerPromoteDetail.getGrandAmount().doubleValue());
                    PlayerCache.getInstance().addPlayer(data.getSessionId(), playerData);

                    sendToSuperiorAndGrand(playerData);
                }
            }
        }
        long endTime = System.currentTimeMillis();
        if (endTime - startTime > 1000) {
            logger.warn("下注-推广慢日志，执行时间：{}", endTime - startTime);
        }
    }

    /**
     * 通知上级玩家
     * @param playerData
     */
    private void sendToSuperiorAndGrand(PlayerData playerData) {
        PlayerPromote playerPromote = PlayerPromoteDao.getInstance().findByPlayerId(playerData.getPlayerId());
        if (playerPromote == null) {
            logger.error("playerPromote is not true");
            return;
        }

        ParityOrderSuc suc = new ParityOrderSuc();
        suc.setPlayerId(playerData.getPlayerId());
        suc.setEffectiveNum(playerPromote.getSubNum());
        suc.setEffectiveNumLv1(playerPromote.getSubNum());
        suc.setEffectiveNumLv2(playerPromote.getSonNum());
        //referralCode
        String referralCode = ShortCodeKit.convertDecimalToBase62(ShortCodeKit.permutedId(playerData.getPlayerId()), 8);
        suc.setReferralCode(referralCode);
        //referralLink
        PlayerPromoteConfig config = ManagePlayerPromote.getInstance().getConfig();
        suc.setReferralLink(config.getReferralLink() + referralCode);
        //bonus
        Total totalLv1 = PlayerPromoteDao.getInstance().sumBonus(playerData.getPlayerId(), 1);
        Total totalLv2 = PlayerPromoteDao.getInstance().sumBonus(playerData.getPlayerId(), 2);
        suc.setBonusLv1(totalLv1.getSum());
        suc.setBonusLv2(totalLv2.getSum());
        suc.setPlayerZJ(totalLv1.getSum() + totalLv2.getSum());

        iSession.sendMessageByID(suc, playerData.getSessionId());

        CoinPointSuccess coinPointSuccess = new CoinPointSuccess();
        coinPointSuccess.setPlayerId(playerData.getPlayerId());
        coinPointSuccess.setCoinType(2);
        coinPointSuccess.setCoinPoint(playerData.getZjPoint());
        iSession.sendMessageByID(coinPointSuccess, playerData.getSessionId());
    }
}
