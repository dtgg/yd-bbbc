package com.ydqp.conn.dispatch;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.cfq.connection.NettySession;
import com.cfq.dispatch.IDispatchMessage;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.NetProtoMessage;
import com.cfq.message.websocket.ProtoEncode;
import com.cfq.net.NettyClient;
import com.cfq.util.StackTraceUtil;
import com.ydqp.common.cache.PlayerCache;
import com.ydqp.common.data.PlayerData;
import com.ydqp.common.receiveProtoMsg.player.PlayerDownLine;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;


/**
 * @author cfq
 * date：2018-09-06.
 */
public class ServerDispatchMessage implements IDispatchMessage {

    private final static Logger logger = LoggerFactory.getLogger(ServerDispatchMessage.class);

    @Override
    public void dispatchAction(NettySession nettySession, NetProtoMessage netProtoMessage) {

        try {
            //not do everything, just send to bs
            int cmd = netProtoMessage.getNetProtoMessageHead().getCmd();
            long connId = nettySession.getSessionId();

            //玩家掉下
            if(cmd == 1000) {
                PlayerData playerData = PlayerCache.getInstance().getPlayer(connId);
                if (playerData == null) {
                    return;
                }
                int roomId = playerData.getRoomId();
                if (roomId != 0) {
                    if ((roomId / 100000) == 50) {
                        return;
                    }
                } else {
                    cmd = 1000002;
                    netProtoMessage.getNetProtoMessageHead().setCmd(1000002);
                }

                PlayerDownLine playerDownLine = new PlayerDownLine();
                playerDownLine.setPlayerId(playerData.getPlayerId());
                Codec<PlayerDownLine> battleAutoStandUpSuccessCodec = ProtobufProxy.create(PlayerDownLine.class);
                byte[] bytes = battleAutoStandUpSuccessCodec.encode(playerDownLine);
                netProtoMessage.getNetProtoMessageBody().setBody(bytes);

                netProtoMessage.getNetProtoMessageHead().setConnId(connId);
                NettyClient nettyClient = RouteToBs.selectBs(cmd);
                ProtoEncode encode = new ProtoEncode();
                BinaryWebSocketFrame byteBuf = encode.encode(netProtoMessage);
                nettyClient.getChannel().writeAndFlush(byteBuf);
                return;
            }

            netProtoMessage.getNetProtoMessageHead().setConnId(connId);
            NettyClient nettyClient = RouteToBs.selectBs(cmd);
            ProtoEncode encode = new ProtoEncode();
            BinaryWebSocketFrame byteBuf = encode.encode(netProtoMessage);
            nettyClient.getChannel().writeAndFlush(byteBuf);
        } catch (Exception e) {
            logger.error(StackTraceUtil.getStackTrace(e));
        }
    }
}
