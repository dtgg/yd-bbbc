package com.ydqp.common.receiveProtoMsg;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.cfq.annotation.GenProto;
import com.cfq.annotation.ReceiveCommandAnnotation;
import com.cfq.message.AbstartParaseMessage;
import com.cfq.message.NetProtoMessage;
import lombok.Getter;
import lombok.Setter;

@ReceiveCommandAnnotation(command = 1000080)
@GenProto(modulePro = "lobby")
public class GetAnnouncement extends AbstartParaseMessage {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT64, order = 1)
    private long playerId;

    @Override
    public AbstartParaseMessage paraseMessage(NetProtoMessage netProtoMessage) throws Exception {
        byte[] body = netProtoMessage.getNetProtoMessageBody().getBody();

        Codec<GetAnnouncement> getAnnouncementCodec = ProtobufProxy.create(GetAnnouncement.class);
        GetAnnouncement getAnnouncement = getAnnouncementCodec.decode(body);

        //setPlayerId(getGameVersion.getPlayerId());
        return getAnnouncement;
    }
}
