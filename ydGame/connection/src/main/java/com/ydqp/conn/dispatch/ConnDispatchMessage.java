package com.ydqp.conn.dispatch;

import com.cfq.connection.ManagerSession;
import com.cfq.connection.NettySession;
import com.cfq.dispatch.IClientDispatchMessage;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.NetProtoMessage;
import com.cfq.message.websocket.ProtoEncode;
import com.cfq.net.NettyClient;
import com.cfq.util.StackTraceUtil;
import com.ydqp.common.receiveProtoMsg.addMoney.PlayerAddMoney;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

/**
 * @author cfq
 * date：2020/4/16.
 */
public class ConnDispatchMessage implements IClientDispatchMessage {

    private static Logger logger = LoggerFactory.getLogger(ConnDispatchMessage.class);
    @Override
    public void clientDispatchAction(ChannelHandlerContext ctx, NetProtoMessage netProtoMessage) {

        try {
            //这边通过connId来判断，！= 0 为转发远程处理，否则本地handler处理
            NettySession nettySession;
            int cmd = netProtoMessage.getNetProtoMessageHead().getCmd();
            //这边由服务端返回的数据，找到要发送的客户端具柄，直接发送
            long connId = netProtoMessage.getNetProtoMessageHead().getConnId();
            if(connId == 0 || cmd == 2010000 ||cmd == 3010000) {
                if (cmd == 2010000) {
                    //发送给所有业务节点
                    NettyClient nettyClient = RouteToBs.selectBs(cmd);
                    ProtoEncode encode = new ProtoEncode();
                    BinaryWebSocketFrame byteBuf = encode.encode(netProtoMessage);
                    nettyClient.getChannel().writeAndFlush(byteBuf);
                } else if (cmd == 3010000) {
                    //发送给所有业务节点
                    logger.info("玩家登陆成功，cmdId= {}    ConnId= {} ",cmd, connId);
                    NettyClient nettyClient = RouteToBs.selectBs(cmd);
                    ProtoEncode encode = new ProtoEncode();
                    BinaryWebSocketFrame byteBuf = encode.encode(netProtoMessage);
                    nettyClient.getChannel().writeAndFlush(byteBuf);
                }
//                nettySession = new NettySession();
//                nettySession.setChannel(ctx.channel());
//
//                //TODO loalhandler
//                IDispatchMessage iDispatchMessage = new DispatchMessage();
//                iDispatchMessage.dispatchAction(nettySession, netProtoMessage);
            } else if (cmd == 1000050) {
                //addMOney
                int roomId = loadBsServerByAddMoneyRoomId (netProtoMessage);
                if ((roomId / 100000) == 20) {
                    netProtoMessage.getNetProtoMessageHead().setCmd(2000050);
                } else if ((roomId / 100000) == 30) {
                    netProtoMessage.getNetProtoMessageHead().setCmd(3000050);
                }
                netProtoMessage.getNetProtoMessageHead().setConnId(connId);
                NettyClient nettyClient = RouteToBs.selectBs(roomId);
                ProtoEncode encode = new ProtoEncode();
                BinaryWebSocketFrame byteBuf = encode.encode(netProtoMessage);
                nettyClient.getChannel().writeAndFlush(byteBuf);
            } else {
                logger.info("receive connID = {} " , connId);
                nettySession = (NettySession) ManagerSession.getInstance().getISession(connId);
                if (nettySession == null ){
                    logger.error("get nettySession is null, connID {}" ,connId);
                    return;
                }

                nettySession.sendMessageToClient(netProtoMessage);
            }

        } catch (Exception e) {
            logger.error("clientDispatchAction error {}", e.getMessage());
        }

    }

    private int loadBsServerByAddMoneyRoomId (NetProtoMessage netProtoMessage) {
        PlayerAddMoney playerAddMoney = new PlayerAddMoney();
        try {
            playerAddMoney.paraseMessage(netProtoMessage);
            logger.info("用户加錢房间，roomId = {} playeId= {}", playerAddMoney.getRoomId(), playerAddMoney.getPlayerId());
        } catch (Exception e) {
            System.out.println(StackTraceUtil.getStackTrace(e));
        }
        return playerAddMoney.getRoomId();
    }
}
