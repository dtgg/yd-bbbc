package com.ydqp.vspoker;

import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.util.StackTraceUtil;
import com.ydqp.common.poker.room.Room;
import com.ydqp.vspoker.room.RoomManager;

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
}
