package com.ydqp.lobby.handler.mall;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.data.WithdrawalData;
import com.ydqp.common.entity.PlayerWithdrawal;
import com.ydqp.common.receiveProtoMsg.mall.WithdrawalList;
import com.ydqp.common.sendProtoMsg.mall.WithdrawalListSuccess;
import com.ydqp.lobby.service.mall.PlayerWithdrawalService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ServerHandler(command = 1004008, module = "mall")
public class WithdrawalListHandler implements IServerHandler {
    private static final Logger logger = LoggerFactory.getLogger(WithdrawalListHandler.class);

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        WithdrawalList withdrawalList = (WithdrawalList) abstartParaseMessage;
        List<PlayerWithdrawal> list = PlayerWithdrawalService.getInstance().page(
                withdrawalList.getPlayerId(), withdrawalList.getPage(), withdrawalList.getLimit());

        logger.info("玩家{}提现记录有{}条", withdrawalList.getPlayerId(), list.size());

        List<WithdrawalData> data = list.stream().map(withdrawal -> {
            String time = sdf.format(new Date(withdrawal.getCreateTime() * 1000L));
            return new WithdrawalData(withdrawal, time);
        }).collect(Collectors.toList());

        WithdrawalListSuccess success = new WithdrawalListSuccess();
        success.setWithdrawalData(data);
        success.setSuccess(true);
        iSession.sendMessageByID(success, withdrawalList.getConnId());
    }
}
