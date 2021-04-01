package com.ydqp.vspoker.room;

import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.ydqp.common.poker.Poker;
import com.ydqp.common.sendProtoMsg.vspoker.SVsFaPai;

import java.util.List;

public class VsPokerFaPaiHandler implements IRoomStatusHandler{

    private static final Logger logger = LoggerFactory.getLogger(VsPokerFaPaiHandler.class);
    @Override
    public void doHandler(VsPokerRoom vsPokerRoom) {
        SVsFaPai sVsFaPai = new SVsFaPai();
        sVsFaPai.setRoomId(vsPokerRoom.getRoomId());
        vsPokerRoom.sendMessageToBattles(sVsFaPai);

        //给五个位子发牌
        //1、生成牌堆
        //2、取前五张为5个位子的牌
        List<Poker> pokerList = vsPokerRoom.getICardPoker().generatePoker();
        vsPokerRoom.getPokerMap().put(1,pokerList.get(0));
        vsPokerRoom.getPokerMap().put(2,pokerList.get(1));
        vsPokerRoom.getPokerMap().put(3,pokerList.get(2));
        vsPokerRoom.getPokerMap().put(4,pokerList.get(3));
        vsPokerRoom.getPokerMap().put(5,pokerList.get(4));

        vsPokerRoom.setStatus(1);
        vsPokerRoom.setCurWaitTime(15);
    }
}
