package com.ydqp.vspoker.room;

import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.ydqp.common.poker.room.BattleRole;
import com.ydqp.common.receiveProtoMsg.vspoker.VsPokerXiazhu;

import java.util.Map;
import java.util.Random;

public class VsPokerBetHandler implements IRoomStatusHandler {

    private static final Logger logger = LoggerFactory.getLogger(VsPokerBetHandler.class);

    @Override
    public void doHandler(VsPokerRoom vsPokerRoom) {
        if (vsPokerRoom.getCurWaitTime() <= 0) {
            //下注15秒时间到
            vsPokerRoom.setStatus(2);
            logger.info("VsPokerBetHandler end");
        } else {
            vsPokerRoom.setCurWaitTime(vsPokerRoom.getCurWaitTime() - 1);

            for (Map.Entry<Long, BattleRole> entry : vsPokerRoom.getBattleRoleMap().entrySet()) {
                if (entry.getValue().getIsVir() == 0) continue;
                if (getDivisor() == 0) continue;

                VsPokerXiazhu vsPokerXiazhu = new VsPokerXiazhu();
                vsPokerXiazhu.setRoomId(vsPokerRoom.getRoomId());
                vsPokerXiazhu.setPlayType(getPlayType());
                vsPokerXiazhu.setPlayerId(entry.getKey());
                vsPokerXiazhu.setMoney(getPoint());
                vsPokerRoom.playerXiazhu(null, entry.getValue(), vsPokerXiazhu);
            }
        }
    }

    private int getDivisor() {
        return new Random().nextInt(2);
    }

    private int getPlayType() {
        int[] playType = new int[]{1,2,3,4};
        int index = new Random().nextInt(4);
        return playType[index];
    }

    private int getPoint() {
        int[] points = new int[]{10, 50, 200, 1000};
        int index = new Random().nextInt(4);
        return points[index];
    }
}
