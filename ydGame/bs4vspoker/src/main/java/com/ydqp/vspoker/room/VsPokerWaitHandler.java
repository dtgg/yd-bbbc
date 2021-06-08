package com.ydqp.vspoker.room;

import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;

public class VsPokerWaitHandler implements IRoomStatusHandler{

    private static final Logger logger = LoggerFactory.getLogger(VsPokerWaitHandler.class);
    @Override
    public void doHandler(VsPokerRoom vsPokerRoom) {
        if(vsPokerRoom.getCurWaitTime() <= 0) {
            //下注15秒时间到
            vsPokerRoom.setStatus(5);
        } else {
            vsPokerRoom.setCurWaitTime(vsPokerRoom.getCurWaitTime() - 1);
        }
    }
}
