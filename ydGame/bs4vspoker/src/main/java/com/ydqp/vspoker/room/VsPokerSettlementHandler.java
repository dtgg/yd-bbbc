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
        vsPokerRoom.setAWin(1);
        vsPokerRoom.setBWin(1);
        vsPokerRoom.setCWin(1);
        vsPokerRoom.setDWin(1);

        vsPokerRoom.getPokerMap().clear();
        vsPokerRoom.getABetBattleRoleId().clear();
        vsPokerRoom.getBBetBattleRoleId().clear();
        vsPokerRoom.getCBetBattleRoleId().clear();
        vsPokerRoom.getDBetBattleRoleId().clear();

        vsPokerRoom.setABetPool(0);
        vsPokerRoom.setBBetPool(0);
        vsPokerRoom.setCBetPool(0);
        vsPokerRoom.setDBetPool(0);

        vsPokerRoom.setStatus(0);
        logger.info("VsPokerSettlementHandler end");
    }

    private void paiFu (VsPokerRoom vsPokerRoom) {
        Map<Long, Double> betBattleRoleId;
        if (vsPokerRoom.getAWin() == 1) {
            betBattleRoleId = vsPokerRoom.getABetBattleRoleId();
            sendToPlayer(vsPokerRoom, betBattleRoleId);
        }
        if (vsPokerRoom.getBWin() == 1) {
            betBattleRoleId = vsPokerRoom.getBBetBattleRoleId();
            sendToPlayer(vsPokerRoom, betBattleRoleId);
        }
        if (vsPokerRoom.getCWin() == 1) {
            betBattleRoleId = vsPokerRoom.getCBetBattleRoleId();
            sendToPlayer(vsPokerRoom, betBattleRoleId);
        }
        if (vsPokerRoom.getDWin() == 1) {
            betBattleRoleId = vsPokerRoom.getDBetBattleRoleId();
            sendToPlayer(vsPokerRoom, betBattleRoleId);
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
