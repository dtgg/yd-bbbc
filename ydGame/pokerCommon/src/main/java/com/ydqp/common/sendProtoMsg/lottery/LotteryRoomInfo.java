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

@SendCommandAnnotation(command = 5001001)
public class LotteryRoomInfo extends AbstartCreateMessage {

    @Setter
    @Getter
    @Protobuf(fieldType = FieldType.INT32, order = 1, description = "房间id")
    private int roomId;
    @Setter
    @Getter
    @Protobuf(fieldType = FieldType.OBJECT, order = 2, description = "彩种信息")
    private List<LotteryTypeInfo> lotteryTypeInfos;
    @Setter
    @Getter
    @Protobuf(fieldType = FieldType.OBJECT, order = 3, description = "用户信息")
    private LotteryBattleRoleInfo lotteryBattleRoleInfo;
//    @Setter
//    @Getter
//    @Protobuf(fieldType = FieldType.OBJECT, order = 4)
//    private List<LotteryInfo> lotteryInfos;
//    @Setter
//    @Getter
//    @Protobuf(fieldType = FieldType.OBJECT, order = 5)
//    private List<PlayerLotteryInfo> playerLotteryInfos;

    @Override
    public NetProtoMessage encodeSendMessage() {
        NetProtoMessage netProtoMessage = new NetProtoMessage();
        netProtoMessage.getNetProtoMessageHead().setCmd(this.getCommand());
        try {
            Codec<LotteryRoomInfo> lotteryRoomInfoCodec = ProtobufProxy.create(LotteryRoomInfo.class);
            byte[] bytes = lotteryRoomInfoCodec.encode(this);
            netProtoMessage.getNetProtoMessageBody().setBody(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return netProtoMessage;
    }
}
