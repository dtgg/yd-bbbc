package com.ydqp.common.sendProtoMsg.lottery;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.cfq.annotation.SendCommandAnnotation;
import com.cfq.message.AbstartCreateMessage;
import com.cfq.message.NetProtoMessage;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@SendCommandAnnotation(command = 5001018)
public class LotteryHotNumSuc extends AbstartCreateMessage {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.MAP, order = 1)
    private Map<String, String> hotNums;

    @Override
    public NetProtoMessage encodeSendMessage() {
        NetProtoMessage netProtoMessage = new NetProtoMessage();
        netProtoMessage.getNetProtoMessageHead().setCmd(this.getCommand());
        try {
            Codec<LotteryHotNumSuc> lotteryHotNumSucCodec = ProtobufProxy.create(LotteryHotNumSuc.class);
            byte[] bytes = lotteryHotNumSucCodec.encode(this);
            netProtoMessage.getNetProtoMessageBody().setBody(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return netProtoMessage;
    }
}
