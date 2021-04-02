package com.ydqp.vspoker.room;

import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.ydqp.common.poker.room.BattleRole;
import com.ydqp.common.sendProtoMsg.vspoker.SVsPlayerWin;

import java.util.Map;

public class VsPokerSettlementHandler implements IRoomStatusHandler{

    private static final Logger logger = LoggerFactory.getLogger(VsPokerSettlementHandler.class);
    @Override
    public void doHandler(VsPokerRoom vsPokerRoom) {
        paiFu(vsPokerRoom);
        //重置数据，房间状态
        vsPokerRoom.setRound(vsPokerRoom.getRound() + 1);

        for (int i = 1; i <= 4; i++) {
            PlayerObject playerObject = vsPokerRoom.getPlayerObjectMap().get(i);
            playerObject.setWin(1);
            playerObject.getBetBattleRoleId().clear();
            playerObject.setBetPool(0);
        }
        vsPokerRoom.getPokerMap().clear();

        vsPokerRoom.setStatus(0);
        logger.info("VsPokerSettlementHandler end");
    }

    /**
     * 对赢的玩家进行赔付
     * @param vsPokerRoom
     */
    private void paiFu (VsPokerRoom vsPokerRoom) {

        for (int i = 1; i <= 4; i++){
            PlayerObject playerObject = vsPokerRoom.getPlayerObjectMap().get(i);
            if (playerObject.getWin() == 1) {
                sendToPlayer(vsPokerRoom, playerObject.getBetBattleRoleId());
            }
        }
    }

    private void sendToPlayer (VsPokerRoom vsPokerRoom, Map<Long, Double> betBattleRoleId) {
        for (Map.Entry<Long, Double> entry : betBattleRoleId.entrySet()) {
            BattleRole battleRole = vsPokerRoom.getBattleRoleMap().get(entry.getKey());
            if (battleRole != null) {
                //赔付
                SVsPlayerWin sVsPlayerWin = new SVsPlayerWin();
                double peiM = entry.getValue() * 2;
                battleRole.setPlayerZJ(battleRole.getPlayerZJ() + peiM);

                sVsPlayerWin.setRoomId(vsPokerRoom.getRoomId());
                sVsPlayerWin.setPlayerId(battleRole.getPlayerId());
                sVsPlayerWin.setTotalMoney(battleRole.getPlayerZJ());
                sVsPlayerWin.setWinMoney(peiM);
                vsPokerRoom.sendMessageToBattle(sVsPlayerWin, battleRole.getPlayerId());
                //@TODO 数据库更新

            }
        }
    }
}
