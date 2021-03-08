package com.ydqp.conn.dispatch;

import com.cfq.connection.ManagerClient;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.NetProtoMessage;
import com.cfq.message.websocket.ProtoEncode;
import com.cfq.net.NettyClient;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

import java.util.Map;

public class RouteToBs {

    private static final Logger logger = LoggerFactory.getLogger(RouteToBs.class);

    public static NettyClient selectBs(int cmd) throws Exception {
        int serverCode = cmd / 100000;
        NettyClient nettyClient = ManagerClient.getInstance().getClientChannel(serverCode);
        if (nettyClient == null) {
            logger.error("select to bs error ,cmd = {} , serverCode = {} " , cmd,  serverCode);
            throw  new Exception("select to bs error");
        }
        return nettyClient;
    }

    public static void sendToAllBs (NetProtoMessage message) {
        Map<Integer, NettyClient > bsMap = ManagerClient.getInstance().getClientChannels();
        bsMap.remove(10);

        message.getNetProtoMessageHead().setCmd(1100000);
        ProtoEncode encode = new ProtoEncode();
        BinaryWebSocketFrame byteBuf = encode.encode(message);
        for (Map.Entry<Integer, NettyClient> entry : bsMap.entrySet()) {
            entry.getValue().getChannel().writeAndFlush(byteBuf);
        }
    }

}
