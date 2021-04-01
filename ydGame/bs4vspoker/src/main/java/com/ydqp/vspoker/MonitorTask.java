package com.ydqp.vspoker;

import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.util.StackTraceUtil;
import com.ydqp.common.poker.room.Room;

public class MonitorTask implements Runnable{

    private static final Logger logger = LoggerFactory.getLogger(MonitorTask.class);

    private final Room room;

    public MonitorTask(Room room) {
        this.room = room;
    }

    /**
     * 多线程处理monitor
     */
    public void run() {
        try {
            if (null != this.room) {
                this.room.monitor();
            }
        } catch (Exception e) {
            logger.error(StackTraceUtil.getStackTrace(e));
        } finally {
            if (null != this.room) {
            }
        }
    }
}
