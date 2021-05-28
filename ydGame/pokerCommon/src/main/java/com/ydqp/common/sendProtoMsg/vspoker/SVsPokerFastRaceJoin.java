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

@SendCommandAnnotation(command = 7001026)
@GenProto(modulePro = "vsPoker")
public class SVsPokerFastRaceJoin extends AbstartCreateMessage {
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.BOOL , order = 1, description = "是否需要报名")
    private boolean enterRoomSuccess;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING , order = 2, description = "提示信息")
    private String message;

    @Override
    public NetProtoMessage encodeSendMessage() {
        NetProtoMessage netProtoMessage = new NetProtoMessage();
        netProtoMessage.getNetProtoMessageHead().setCmd(this.getCommand());
        try {
            Codec<SVsPokerFastRaceJoin> sVsPokerCheckPlayerJoinCodec = ProtobufProxy.create(SVsPokerFastRaceJoin.class);
            byte[] bytes = sVsPokerCheckPlayerJoinCodec.encode(this);
            netProtoMessage.getNetProtoMessageBody().setBody(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return netProtoMessage;
    }
}
