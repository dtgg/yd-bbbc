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

import java.util.List;

@SendCommandAnnotation(command = 7001013)
@GenProto(modulePro = "vsPoker")
public class SVsPokerRaking extends AbstartCreateMessage {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.OBJECT, order = 1)
    private List<SVsPlayerRankData> sPlayerRankData;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.OBJECT, order = 2)
    private SVsPlayerRankData playerRankData;

    @Override
    public NetProtoMessage encodeSendMessage() {
        NetProtoMessage netProtoMessage = new NetProtoMessage();
        netProtoMessage.getNetProtoMessageHead().setCmd(this.getCommand());
        try {
            Codec<SVsPokerRaking> sVsPokerRakingCodec = ProtobufProxy.create(SVsPokerRaking.class);
            byte[] bytes = sVsPokerRakingCodec.encode(this);
            netProtoMessage.getNetProtoMessageBody().setBody(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return netProtoMessage;
    }
}
