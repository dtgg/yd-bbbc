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

@SendCommandAnnotation(command = 7001030)
@GenProto(modulePro = "vsPoker")
public class SVsPokerFastRaceHistory extends AbstartCreateMessage {
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT64 , order = 1, description = "用户ID")
    private long playerId;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.OBJECT , order = 2, description = "快速赛历史记录")
    private List<SVsPokerFastRaceInfo> fastRaceInfoList;

    @Override
    public NetProtoMessage encodeSendMessage() {
        NetProtoMessage netProtoMessage = new NetProtoMessage();
        netProtoMessage.getNetProtoMessageHead().setCmd(this.getCommand());
        try {
            Codec<SVsPokerFastRaceHistory> sVsPokerFastRaceHistoryCodec = ProtobufProxy.create(SVsPokerFastRaceHistory.class);
            byte[] bytes = sVsPokerFastRaceHistoryCodec.encode(this);
            netProtoMessage.getNetProtoMessageBody().setBody(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return netProtoMessage;
    }
}
