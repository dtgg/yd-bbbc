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

@SendCommandAnnotation(command = 7000006)
@GenProto(modulePro = "vsPoker")
public class SVsPokerRoomInfo extends AbstartCreateMessage {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32 , order = 1, description = "roomId")
    private int roomId;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32 , order = 2, description = "roomStatus")
    private int roomStatus;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32 , order = 3, description = "curWaitTime")
    private int curWaitTime;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.DOUBLE , order = 4, description = "roomId")
    private double battleRoleMoney;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.MAP ,order = 5, description = "4个下注区域 1 = A")
    private Map<Integer, SPlayerInfo> sPlayerInfoMap = new HashMap<>(4);

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.OBJECT , order = 6, description = "庄家的牌")
    private Poker bankPoker;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32 , order = 7, description = "庄家的牌")
    private int roomType;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32 , order = 8, description = "庄家的牌")
    private int rank;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32 , order = 9, description = "庄家的牌")
    private int round;

    @Override
    public NetProtoMessage encodeSendMessage() {
        NetProtoMessage netProtoMessage = new NetProtoMessage();
        netProtoMessage.getNetProtoMessageHead().setCmd(this.getCommand());
        try {
            Codec<SVsPokerRoomInfo> sVsPokerRoomInfoCodec = ProtobufProxy.create(SVsPokerRoomInfo.class);
            byte[] bytes = sVsPokerRoomInfoCodec.encode(this);
            netProtoMessage.getNetProtoMessageBody().setBody(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return netProtoMessage;
    }
}
