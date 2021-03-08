package com.ydqp.common.sendProtoMsg.lottery;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.cfq.annotation.GenProto;
import com.cfq.annotation.SendCommandAnnotation;
import com.cfq.message.AbstartCreateMessage;
import com.cfq.message.NetProtoMessage;
import com.ydqp.common.data.TaskConfigData;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@SendCommandAnnotation(command = 5001014)
@GenProto(modulePro = "lottery")
public class ParityRecommendTaskSuc extends AbstartCreateMessage {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 1)
    private int effectiveNum;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.OBJECT, order = 2)
    private List<TaskConfigData> taskConfigDataList;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 3, description = "已领取奖励的任务ID")
    private String rewardTaskIds;


    @Override
    public NetProtoMessage encodeSendMessage() {
        NetProtoMessage netProtoMessage = new NetProtoMessage();
        netProtoMessage.getNetProtoMessageHead().setCmd(this.getCommand());
        try {
            Codec<ParityRecommendTaskSuc> recommendTaskSucCodec = ProtobufProxy.create(ParityRecommendTaskSuc.class);
            byte[] bytes = recommendTaskSucCodec.encode(this);
            netProtoMessage.getNetProtoMessageBody().setBody(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return netProtoMessage;
    }
}
