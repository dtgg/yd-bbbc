package com.ydqp.vspoker.room;

import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;

public class VsPokerBetHandler implements IRoomStatusHandler{

    private static final Logger logger = LoggerFactory.getLogger(VsPokerBetHandler.class);
    @Override
    public void doHandler(VsPokerRoom vsPokerRoom) {
        if(vsPokerRoom.getCurWaitTime() <= 0) {
            //下注15秒时间到
            vsPokerRoom.setStatus(2);
        } else {
            vsPokerRoom.setCurWaitTime(vsPokerRoom.getCurWaitTime() - 1);
        }
    }
}
