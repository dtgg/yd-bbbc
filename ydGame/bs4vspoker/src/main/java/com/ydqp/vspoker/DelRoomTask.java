package com.ydqp.vspoker;

import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.util.StackTraceUtil;
import com.ydqp.common.poker.room.BattleRole;
import com.ydqp.common.poker.room.Room;
import com.ydqp.common.sendProtoMsg.vspoker.SVsBonusRank;
import com.ydqp.common.sendProtoMsg.vspoker.SVsRaceEnd;
import com.ydqp.vspoker.dao.VsPokerDao;
import com.ydqp.vspoker.room.RoomManager;
import com.ydqp.vspoker.room.VsPokerRoom;

import java.util.HashMap;
import java.util.Map;

public class DelRoomTask implements Runnable{

    private static final Logger logger = LoggerFactory.getLogger(DelRoomTask.class);


    public DelRoomTask() {
    }

    @Override
    public void run() {
        //删除房间
        try {
            for(Integer roomId : RoomManager.getInstance().getDelRoomId()) {
                Room room = RoomManager.getInstance().getRoom(roomId);
                if (room != null) {
                    logger.info("删除房间开始，roomId = {}", roomId);

                    if (room.getRoomType() == 1 ||room.getRoomType() == 2) {
                        //更新状态
                        VsPokerDao.getInstance().updateRaceStatus(room.getRaceId(), 2);
                        //@TODO 通知用户比赛结束
                        notifyRaceEnd(room);
                    }

                    RoomManager.getInstance().getVsPokerRoomMapMap().remove(roomId);
                    NumberPool.getInstance().push(roomId);

                    logger.info("删除房间结束，roomId = {}", roomId);
                }
            }

            RoomManager.getInstance().getDelRoomId().clear();
        } catch (Exception e) {
            logger.error("关闭房间失败异常, e = {}" , StackTraceUtil.getStackTrace(e));
        }

    }

    /**
     * 通知比赛结束
     * @param vsPokerRoom
     */
    private void notifyRaceEnd (Room vsPokerRoom) {

        for (Map.Entry<Long, BattleRole> entry : vsPokerRoom.getBattleRoleMap().entrySet()) {
            SVsRaceEnd sVsRaceEnd = new SVsRaceEnd();
            sVsRaceEnd.setRoomId(vsPokerRoom.getRoomId());
            Map<Integer, SVsBonusRank> sVsBonusRankMap = new HashMap<>();
            SVsBonusRank  sVsBonusRank = new SVsBonusRank();
            sVsBonusRank.setBonus(1000);
            sVsBonusRank.setPoints(1000);
            sVsBonusRank.setPlayerName("4565");
            SVsBonusRank  sVsBonusRank2 = new SVsBonusRank();
            sVsBonusRank2.setBonus(1000);
            sVsBonusRank2.setPoints(1000);
            sVsBonusRank2.setPlayerName("123");
            SVsBonusRank  sVsBonusRank3 = new SVsBonusRank();
            sVsBonusRank3.setBonus(1000);
            sVsBonusRank3.setPoints(1000);
            sVsBonusRank3.setPlayerName("658");
            sVsBonusRankMap.put(1, sVsBonusRank);
            sVsBonusRankMap.put(2, sVsBonusRank2);
            sVsBonusRankMap.put(3, sVsBonusRank3);
            sVsRaceEnd.setBonusRankMap(sVsBonusRankMap);
            sVsRaceEnd.setPlayerId(entry.getKey());
            sVsRaceEnd.setBonus(0);
            sVsRaceEnd.setRank(10);

            vsPokerRoom.sendMessageToBattle(sVsRaceEnd, entry.getKey());
        }

    }
}

