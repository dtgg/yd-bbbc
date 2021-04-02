package com.ydqp.common.sendProtoMsg.vspoker;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.cfq.annotation.GenProto;
import com.cfq.annotation.SendCommandAnnotation;
import com.cfq.message.AbstartCreateMessage;
import com.cfq.message.NetProtoMessage;
import com.ydqp.common.poker.Poker;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@SendCommandAnnotation(command = 7000002)
@GenProto(modulePro = "vsPoker")
public class SVsCompareResult extends AbstartCreateMessage {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32 , order = 1, description = "roomId")
    private int roomId;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32 , order = 2, description = "4个下注区域 1 = A")
    private Map<Integer, SPlayerInfo> sPlayerInfoMap = new HashMap<>(4);

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.OBJECT , order = 3, description = "庄家的牌")
    private Poker bankPoker;

    @Override
    public NetProtoMessage encodeSendMessage() {
        NetProtoMessage netProtoMessage = new NetProtoMessage();
        netProtoMessage.getNetProtoMessageHead().setCmd(this.getCommand());
        try {
            Codec<SVsCompareResult> sVsCompareResultCodec = ProtobufProxy.create(SVsCompareResult.class);
            byte[] bytes = sVsCompareResultCodec.encode(this);
            netProtoMessage.getNetProtoMessageBody().setBody(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return netProtoMessage;
    }
}
