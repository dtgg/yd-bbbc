package com.ydqp.vspoker.room;

import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;

public class VsPokerBeginHandler implements IRoomStatusHandler{

    private static final Logger logger = LoggerFactory.getLogger(VsPokerFaPaiHandler.class);
    @Override
    public void doHandler(VsPokerRoom vsPokerRoom) {
        if(vsPokerRoom.getCurWaitTime() <= 0) {
            //下注60秒时间到
            vsPokerRoom.setStatus(1);
        } else {
            vsPokerRoom.setCurWaitTime(vsPokerRoom.getCurWaitTime() - 1);
        }
    }
}
