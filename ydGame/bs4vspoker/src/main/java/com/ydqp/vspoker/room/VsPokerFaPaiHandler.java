package com.ydqp.vspoker.room;

import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.ydqp.common.sendProtoMsg.vspoker.SVsFaPai;

public class VsPokerFaPaiHandler implements IRoomStatusHandler{

    private static final Logger logger = LoggerFactory.getLogger(VsPokerFaPaiHandler.class);
    @Override
    public void doHandler(VsPokerRoom vsPokerRoom) {
        SVsFaPai sVsFaPai = new SVsFaPai();
        sVsFaPai.setRoomId(vsPokerRoom.getRoomId());
        vsPokerRoom.sendMessageToBattles(sVsFaPai);

        vsPokerRoom.setStatus(1);
        vsPokerRoom.setCurWaitTime(15);
    }
}
