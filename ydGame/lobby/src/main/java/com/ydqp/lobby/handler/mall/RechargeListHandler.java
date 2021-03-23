package com.ydqp.lobby.handler.mall;

import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.data.RechargeData;
import com.ydqp.common.data.WithdrawalData;
import com.ydqp.common.entity.PlayerOrder;
import com.ydqp.common.entity.PlayerWithdrawal;
import com.ydqp.common.receiveProtoMsg.mall.RechargeList;
import com.ydqp.common.receiveProtoMsg.mall.WithdrawalList;
import com.ydqp.common.sendProtoMsg.mall.RechargeListSuccess;
import com.ydqp.common.sendProtoMsg.mall.WithdrawalListSuccess;
import com.ydqp.lobby.service.mall.PlayerOrderService;
import com.ydqp.lobby.service.mall.PlayerWithdrawalService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ServerHandler(command = 1004009, module = "mall")
public class RechargeListHandler implements IServerHandler {

    private static final Logger logger = LoggerFactory.getLogger(RechargeListHandler.class);

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        RechargeList rechargeList = (RechargeList) abstartParaseMessage;
        logger.info("查询充值记录：playerId：{}，page：{}，limit：{}",
                rechargeList.getPlayerId(), rechargeList.getPage(), rechargeList.getLimit());

        List<PlayerOrder> list = PlayerOrderService.getInstance().page(
                rechargeList.getPlayerId(), rechargeList.getPage(), rechargeList.getLimit());

        List<RechargeData> data = list.stream().map(order -> {
            String time = sdf.format(new Date(order.getCreateTime() * 1000L));
            return new RechargeData(order, time);
        }).collect(Collectors.toList());

        RechargeListSuccess success = new RechargeListSuccess();
        success.setRechargeData(data);
        success.setSuccess(true);
        iSession.sendMessageByID(success, rechargeList.getConnId());
    }
}
