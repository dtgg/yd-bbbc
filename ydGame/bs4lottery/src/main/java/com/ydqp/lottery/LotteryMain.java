package com.ydqp.lottery;

import com.cfq.boostStrap.IServer;
import com.cfq.boostStrap.Server;
import com.cfq.dispatch.DispatchMessage;
import com.cfq.net.websocket.ServerWebSocketChannelInitializer;
import com.ydqp.common.lottery.player.ManageLottery;
import com.ydqp.lottery.timer.LotteryDrawTimer;
import com.ydqp.lottery.timer.LotteryGenerateTimer;
import com.ydqp.lottery.timer.LotteryMaintainTimer;
import com.ydqp.lottery.timer.LotteryPayTimer;

import java.util.Timer;

public class LotteryMain {

    public static void main(String[] args) {
        //彩票生成任务
        new Timer().schedule(new LotteryGenerateTimer() , 1000, 24 * 60 * 60 * 1000);
        //开奖任务
        new Timer().schedule(new LotteryDrawTimer(), 1000, 1000);
        //派奖任务
        new Timer().schedule(new LotteryPayTimer(), 1000, 1000);
        //关闭服务器任务
        new Timer().schedule(new LotteryMaintainTimer(), 1000, 1000);
        //初始化
        ManageLottery.getInstance();
        //start server for app
        DispatchMessage serverDispatchMessage = new DispatchMessage();
        IServer iServer = new Server();
        ServerWebSocketChannelInitializer serverWebSocketChannelInitializer = new ServerWebSocketChannelInitializer(serverDispatchMessage);
        iServer.start(serverWebSocketChannelInitializer);
    }
}
