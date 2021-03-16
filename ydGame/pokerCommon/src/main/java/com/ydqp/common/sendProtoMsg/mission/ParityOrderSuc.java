package com.ydqp.common.sendProtoMsg.mission;

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

@SendCommandAnnotation(command = 1001070)
@GenProto(modulePro = "lottery")
public class ParityOrderSuc extends AbstartCreateMessage {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT64, order = 1)
    private long playerId;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.DOUBLE, order = 2)
    private double playerZJ;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 3, description = "有效用户数")
    private int effectiveNum;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 4, description = "LV1有效用户数")
    private int effectiveNumLv1;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.DOUBLE, order = 5, description = "LV1获得的金币")
    private double bonusLv1;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 6, description = "LV2有效用户数")
    private int effectiveNumLv2;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.DOUBLE, order = 7, description = "LV2获得的金币")
    private double bonusLv2;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 8, description = "推广码")
    private String referralCode;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 9, description = "推广链接")
    private String referralLink;

    @Override
    public NetProtoMessage encodeSendMessage() {
        NetProtoMessage netProtoMessage = new NetProtoMessage();
        netProtoMessage.getNetProtoMessageHead().setCmd(this.getCommand());
        try {
            Codec<ParityOrderSuc> parityOrderSucCodec = ProtobufProxy.create(ParityOrderSuc.class);
            byte[] bytes = parityOrderSucCodec.encode(this);
            netProtoMessage.getNetProtoMessageBody().setBody(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return netProtoMessage;
    }
}
