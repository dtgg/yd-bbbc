package com.ydqp.common.sendProtoMsg.vspoker;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.cfq.annotation.GenProto;
import com.cfq.annotation.SendCommandAnnotation;
import com.cfq.message.AbstartCreateMessage;
import com.cfq.message.NetProtoMessage;
import lombok.Getter;
import lombok.Setter;

@SendCommandAnnotation(command = 7001025)
@GenProto(modulePro = "vsPoker")
public class SVsPokerCheckPlayerJoin extends AbstartCreateMessage {
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.BOOL , order = 1, description = "是否需要报名")
    private boolean joinEnabled;

    @Override
    public NetProtoMessage encodeSendMessage() {
        NetProtoMessage netProtoMessage = new NetProtoMessage();
        netProtoMessage.getNetProtoMessageHead().setCmd(this.getCommand());
        try {
            Codec<SVsPokerCheckPlayerJoin> sVsPokerCheckPlayerJoinCodec = ProtobufProxy.create(SVsPokerCheckPlayerJoin.class);
            byte[] bytes = sVsPokerCheckPlayerJoinCodec.encode(this);
            netProtoMessage.getNetProtoMessageBody().setBody(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return netProtoMessage;
    }
}
