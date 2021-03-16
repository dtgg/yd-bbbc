package com.ydqp.common.lottery.player;

import com.cfq.connection.ISession;
import com.cfq.message.AbstartCreateMessage;
import com.ydqp.common.data.LotteryDrawData;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.entity.Lottery;
import com.ydqp.common.entity.PlayerLottery;
import com.ydqp.common.lottery.role.LotteryBattleRole;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ILottery {

    PlayerLottery lotteryBuy(Lottery lottery, PlayerLottery playerLottery);

    LotteryDrawData lotteryDraw(Lottery lottery, List<PlayerLottery> playerLotteries);

    Map<Integer, BigDecimal> settleAwardPrice(List<PlayerLottery> playerLotteries);

    PlayerLottery playerLotteryCheck(Lottery lottery, PlayerLottery playerLottery, Integer calculateTime);

    void enterLottery(PlayerData playerData, ISession session);

    boolean comebackLottery(PlayerData playerData, ISession iSession);

//    LotteryBattleRole getLotteryBattleRole(long playerId);
//
//    Map<Long, LotteryBattleRole> getLotteryBattleRoleMap();
//
//    void removeLotteryBattleRole(long playerId);

    void sendMessageToBattle(AbstartCreateMessage abstartCreateMessage, LotteryBattleRole lotteryBattleRole);
}
