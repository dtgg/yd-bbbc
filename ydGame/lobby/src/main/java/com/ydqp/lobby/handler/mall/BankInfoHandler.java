package com.ydqp.lobby.handler.mall;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.data.YaarPayBankInfo;
import com.ydqp.common.sendProtoMsg.mall.BankInfoSuccess;
import com.ydqp.lobby.pay.yaarpay.YaarConstant;

import java.util.ArrayList;
import java.util.List;

@ServerHandler(command = 1004010, module = "mall")
public class BankInfoHandler implements IServerHandler {
    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        List<YaarPayBankInfo> yaarPayBankInfos = new ArrayList<>();
        YaarConstant.BANK_CODE.forEach((key, value) -> {
            YaarPayBankInfo info = new YaarPayBankInfo(key, value);
            yaarPayBankInfos.add(info);
        });

        BankInfoSuccess success = new BankInfoSuccess();
        success.setBankInfos(yaarPayBankInfos);
        iSession.sendMessageByID(success, abstartParaseMessage.getConnId());
    }
}
