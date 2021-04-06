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

@SendCommandAnnotation(command = 7001014)
@GenProto(modulePro = "vsPoker")
public class SVsPokerRaceJoin extends AbstartCreateMessage {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.BOOL, order = 1)
    private boolean success;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 2)
    private String message;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.OBJECT, order = 3)
    private SVsPlayerRace sVsPlayerRace;

    @Override
    public NetProtoMessage encodeSendMessage() {
        NetProtoMessage netProtoMessage = new NetProtoMessage();
        netProtoMessage.getNetProtoMessageHead().setCmd(this.getCommand());
        try {
            Codec<SVsPokerRaceJoin> sVsPokerRaceJoinCodec = ProtobufProxy.create(SVsPokerRaceJoin.class);
            byte[] bytes = sVsPokerRaceJoinCodec.encode(this);
            netProtoMessage.getNetProtoMessageBody().setBody(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return netProtoMessage;
    }
}
