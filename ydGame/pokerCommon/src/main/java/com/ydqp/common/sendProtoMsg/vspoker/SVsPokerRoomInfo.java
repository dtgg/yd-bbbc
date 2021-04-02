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
    @Protobuf(fieldType = FieldType.OBJECT , order = 5, description = "roomId")
    private SPlayerInfo aPlayer;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.OBJECT , order = 6, description = "roomId")
    private SPlayerInfo bPlayer;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.OBJECT , order = 7, description = "roomId")
    private SPlayerInfo cPlayer;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.OBJECT , order = 8, description = "roomId")
    private SPlayerInfo dPlayer;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.OBJECT , order = 9, description = "庄家的牌")
    private Poker bankPoker;


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
