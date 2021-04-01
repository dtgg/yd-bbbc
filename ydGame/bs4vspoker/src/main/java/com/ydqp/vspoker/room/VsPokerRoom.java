package com.ydqp.vspoker.room;

import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.util.StackTraceUtil;
import com.ydqp.common.poker.Poker;
import com.ydqp.common.poker.room.Room;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class VsPokerRoom extends Room {

    private static final Logger logger = LoggerFactory.getLogger(VsPokerRoom.class);

    // 0 发牌、 1 下注 15s, 2 发送笔牌结果 3 等待客户端播完动画效果 4 发送结算信息
    private final IRoomStatusHandler[] roomStatusHandlers = new IRoomStatusHandler[]{new VsPokerFaPaiHandler(),
    new VsPokerBetHandler(), new VsPokerCompareHandler(), new VsPokerWaitHandler(), new VsPokerSettlementHandler()};

    @Setter
    @Getter
    private Map<Integer, Poker> pokerMap = new ConcurrentHashMap<>(5);

    @Setter
    @Getter
    private int round = 0; //当前局数

    @Setter
    @Getter
    private double aBetPool;
    @Setter
    @Getter
    private double bBetPool;
    @Setter
    @Getter
    private double cBetPool;
    @Setter
    @Getter
    private double dBetPool;


    @Override
    public void monitor() {
        try {
            roomStatusHandlers[this.getStatus()].doHandler(this);
        } catch (Exception e) {
            e.printStackTrace();

            logger.error(StackTraceUtil.getStackTrace(e));
        }
    }
}
