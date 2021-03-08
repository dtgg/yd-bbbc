package com.ydqp.lobby.handler;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.receiveProtoMsg.player.PlayerLogin;
import com.ydqp.lobby.service.PlayerService;

@ServerHandler(command = 1000001, module = "playerLogin")
public class PlayerLoginHandler implements IServerHandler {
    /**
     * 这边只做一个登陆动作，坐完登陆后，发送登陆指令到各个业务服务器，业务服务器进行各自的逻辑处理
     * @param iSession
     * @param abstartParaseMessage
     */
    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        PlayerLogin playerLogin = (PlayerLogin) abstartParaseMessage;

        PlayerService.getInstance().login(iSession, playerLogin);
    }
}
