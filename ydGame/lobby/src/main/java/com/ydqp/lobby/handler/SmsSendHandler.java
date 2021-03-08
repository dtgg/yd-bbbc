package com.ydqp.lobby.handler;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.receiveProtoMsg.player.SmsSend;
import com.ydqp.common.sendProtoMsg.player.SmsSendSuc;
import com.ydqp.lobby.service.sms.SmsService;

@ServerHandler(module = "playerLogin", command = 1000032)
public class SmsSendHandler implements IServerHandler {
    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        SmsSend smsSend = (SmsSend) abstartParaseMessage;

        boolean verifyCode = SmsService.getInstance().sendVerifyCode(smsSend);
        String message = verifyCode ? "Verification code sent successfully" :
                "Failed to send the verification code, please check if the phone number is correct";

        SmsSendSuc smsSendSuc = new SmsSendSuc();
        if (verifyCode) {
            smsSendSuc.setSendTime(new Long(System.currentTimeMillis() / 1000).intValue());
        }
        smsSendSuc.setMessage(message);
        iSession.sendMessageByID(smsSendSuc, smsSend.getConnId());
    }
}
