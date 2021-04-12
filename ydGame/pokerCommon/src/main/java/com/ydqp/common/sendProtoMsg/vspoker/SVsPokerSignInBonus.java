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

import java.util.Map;

@SendCommandAnnotation(command = 7001019)
@GenProto(modulePro = "vsPoker")
public class SVsPokerSignInBonus extends AbstartCreateMessage {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.MAP , order = 1, description = "bonusMap")
    private Map<Integer, Double> bonusMap;

    @Override
    public NetProtoMessage encodeSendMessage() {
        NetProtoMessage netProtoMessage = new NetProtoMessage();
        netProtoMessage.getNetProtoMessageHead().setCmd(this.getCommand());
        try {
            Codec<SVsPokerSignInBonus> sVsPokerSignInBonusCodec = ProtobufProxy.create(SVsPokerSignInBonus.class);
            byte[] bytes = sVsPokerSignInBonusCodec.encode(this);
            netProtoMessage.getNetProtoMessageBody().setBody(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return netProtoMessage;
    }
}
