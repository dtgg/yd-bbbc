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

@SendCommandAnnotation(command = 5001004)
public class LotteryDrawNotificationSuc extends AbstartCreateMessage {

    @Setter
    @Getter
    @Protobuf(fieldType = FieldType.INT64, order = 1)
    private long playerId;
    @Setter
    @Getter
    @Protobuf(fieldType = FieldType.BOOL, order = 2)
    private boolean win;
    @Setter
    @Getter
    @Protobuf(fieldType = FieldType.OBJECT, order = 3)
    private List<LotteryTypeListInfo> lotteryTypeListInfos;

    @Override
    public NetProtoMessage encodeSendMessage() {
        NetProtoMessage netProtoMessage = new NetProtoMessage();
        netProtoMessage.getNetProtoMessageHead().setCmd(this.getCommand());
        try {
            Codec<LotteryDrawNotificationSuc> drawNotificationSucCodec = ProtobufProxy.create(LotteryDrawNotificationSuc.class);
            byte[] bytes = drawNotificationSucCodec.encode(this);
            netProtoMessage.getNetProtoMessageBody().setBody(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return netProtoMessage;
    }
}
