package com.ydqp.lobby.handler;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.receiveProtoMsg.player.GuestRegisterByAppId;
import com.ydqp.lobby.service.PlayerService;

@ServerHandler(module = "playerLogin", command = 1000012)
public class PlayerGuestRegisterByAppIdHandler implements IServerHandler {
    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        GuestRegisterByAppId guestRegisterByAppId = (GuestRegisterByAppId) abstartParaseMessage;
        PlayerService.getInstance().guestRegisterByAppId(iSession, guestRegisterByAppId);
    }
}
