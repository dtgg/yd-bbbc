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

@SendCommandAnnotation(command = 7000003)
@GenProto(modulePro = "vsPoker")
public class SVsPlayerWin extends AbstartCreateMessage {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32 , order = 1, description = "roomId")
    private int roomId;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT64 , order = 2, description = "用户id")
    private long playerId;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.DOUBLE , order = 3, description = "总的钱")
    private double totalMoney;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.DOUBLE , order = 4, description = "当局赢的钱")
    private double winMoney;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32 , order = 5, description = "当局赢的区域")
    private List<Integer> winTypes;

    @Override
    public NetProtoMessage encodeSendMessage() {
        NetProtoMessage netProtoMessage = new NetProtoMessage();
        netProtoMessage.getNetProtoMessageHead().setCmd(this.getCommand());
        try {
            Codec<SVsPlayerWin> sVsPlayerWinCodec = ProtobufProxy.create(SVsPlayerWin.class);
            byte[] bytes = sVsPlayerWinCodec.encode(this);
            netProtoMessage.getNetProtoMessageBody().setBody(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return netProtoMessage;
    }
}
