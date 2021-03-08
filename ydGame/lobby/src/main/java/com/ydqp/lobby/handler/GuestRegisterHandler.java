package com.ydqp.lobby.handler;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.receiveProtoMsg.player.GuestRegister;
import com.ydqp.lobby.service.PlayerService;

/**
 * 游客注册
 */
@ServerHandler(module = "playerLogin", command = 1000011)
public class GuestRegisterHandler implements IServerHandler {
    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        GuestRegister guestRegister = (GuestRegister) abstartParaseMessage;
        PlayerService.getInstance().guestRegister(iSession, guestRegister);
    }
}
