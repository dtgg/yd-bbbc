package com.ydqp.lottery.test;

import com.alibaba.fastjson.JSON;
import com.cfq.connection.NettySession;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.receiveProtoMsg.lottery.*;
import com.ydqp.common.receiveProtoMsg.mission.*;
import com.ydqp.lottery.handler.*;
import com.ydqp.lottery.task.LotteryDrawTask;
import com.ydqp.lottery.task.LotteryGenerateTask;
import com.ydqp.lottery.task.LotteryPayTask;

import java.util.*;

public class LotteryTest {

    private static final NettySession iSession = new NettySession();

    static {
        iSession.setSessionId(1);
    }

    private static void lotteryEnterRoomTest() {
        LotteryEnterRoom lotteryEnterRoom = new LotteryEnterRoom();
        lotteryEnterRoom.setPlayerId(1069106);
        lotteryEnterRoom.setRoomId(5000001);

        new LotteryEnterRoomHandler().process(iSession, lotteryEnterRoom);
    }

    private static void lotteryBuyTest() {
        try {
            for (int i = 0; i < 10; i++) {
                LotteryBuy lotteryBuy = new LotteryBuy();
                lotteryBuy.setPlayerId(1069106);
                lotteryBuy.setLotteryId(3197);
                lotteryBuy.setType(1);
                lotteryBuy.setNumber(String.valueOf(i + 1));
                lotteryBuy.setPay(String.valueOf(i + 1));

                new LotteryBuyHandler().process(iSession, lotteryBuy);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void lotteryQuitRoom() {
        LotteryQuitRoom lotteryQuitRoom = new LotteryQuitRoom();
        lotteryQuitRoom.setPlayerId(1069106);

        new LotteryQuitRoomHandler().process(iSession, lotteryQuitRoom);
    }

    private static void lotteryComeBackTest() {
        LotteryComebackRoom lotteryComebackRoom = new LotteryComebackRoom();
        lotteryComebackRoom.setPlayerId(1069106);
        lotteryComebackRoom.setRoomId(5000001);

        new LotteryComebackHandler().process(iSession, lotteryComebackRoom);
    }

    private static void lotteryListTest() {
        LotteryList lotteryList = new LotteryList();
        lotteryList.setType(1);
        lotteryList.setPage(1);
        lotteryList.setSize(10);
        new LotteryListHandler().process(iSession, lotteryList);
    }

    private static void playerLotteryListTest() {
        PlayerLotteryList lotteryList = new PlayerLotteryList();
        lotteryList.setType(1);
        lotteryList.setPage(1);
        lotteryList.setSize(10);
        lotteryList.setPlayerId(1069109);
        new PlayerLotteryListHandler().process(iSession, lotteryList);
    }

    private static void lotteryGenerateTest() {
        new LotteryGenerateTask().run();
    }

    private static void lotteryDrawTest() {
        LotteryDrawTask lotteryDrawTask = new LotteryDrawTask();
        lotteryDrawTask.run();
    }

    private static void lotteryPayTest() {
        new LotteryPayTask().run();
    }

    private static void lotteryNextTest() {
        LotteryNext lotteryNext = new LotteryNext();
//        lotteryNext.setNext(true);
        new LotteryNextHandler().process(iSession, lotteryNext);
    }

    private static void playerCacheTest() {
        Map<Long, PlayerData> dataMap = new HashMap<>();
        List<Long> keys = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            PlayerData playerData = new PlayerData();
            playerData.setPlayerId(i+1);
            dataMap.put((long) (i+1), playerData);
            keys.add((long) (i+1));
        }
        PlayerCache.getInstance().addPlayers(dataMap);
        List<PlayerData> players = PlayerCache.getInstance().getPlayers(keys);
        System.out.println(JSON.toJSONString(players));
    }

    private static void parityOrderTest() {
        ParityOrder parityOrder = new ParityOrder();
        parityOrder.setPlayerId(1069108);
//        new ParityOrderHandler().process(iSession, parityOrder);
    }

    private static void parityOrderDetailTest() {
        ParityOrderDetail detail = new ParityOrderDetail();
        detail.setPlayerId(1069108);
//        new ParityOrderDetailHandler().process(iSession, detail);
    }

    private static void parityOrderLvTest() {
        ParityOrderLv lv = new ParityOrderLv();
        lv.setPlayerId(1069108);
        lv.setLv(2);
        lv.setPage(1);
        lv.setSize(5);
//        new ParityOrderLvHandler().process(iSession, lv);
    }

    private static void recommendTask() {
        ParityRecommendTask recommendTask = new ParityRecommendTask();
        recommendTask.setPlayerId(1069108);
//        new ParityRecommendTaskHandler().process(iSession, recommendTask);
    }

    private static void parityReceive() {
        ParityReceive parityReceive = new ParityReceive();
        parityReceive.setPlayerId(1069108);
        parityReceive.setTaskId(16);
//        new ParityReceiveHandler().process(iSession, parityReceive);
    }

    public static void main(String[] args) {
//        Player p = new Player();
//        p.setId(1069106);
//        p.setZjPoint(1000);
//        p.setRoomId(5000001);
//        playerData = new PlayerData(p);
//        playerData.setSessionId(iSession.getSessionId());
//        lotteryDrawTest();
    }
}
