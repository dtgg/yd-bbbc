package com.ydqp.common.receiveProtoMsg.mall;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.cfq.annotation.ReceiveCommandAnnotation;
import com.cfq.message.AbstartParaseMessage;
import com.cfq.message.NetProtoMessage;
import lombok.Getter;
import lombok.Setter;

@ReceiveCommandAnnotation(command = 1004003)
public class PlayerAccountUpdate extends AbstartParaseMessage {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT64, order = 1)
    private long playerId;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 2)
    private String name;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 3)
    private String accNo;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 4)
    private String ifsc;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 5)
    private String mobile;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 6)
    private String bankCode;

    @Override
    public AbstartParaseMessage paraseMessage(NetProtoMessage netProtoMessage) throws Exception {
        byte[] body = netProtoMessage.getNetProtoMessageBody().getBody();

        Codec<PlayerAccountUpdate> accountUpdateCodec = ProtobufProxy.create(PlayerAccountUpdate.class);

        PlayerAccountUpdate accountUpdate = accountUpdateCodec.decode(body);
//        setPlayerId(accountUpdate.getPlayerId());
//        setName(accountUpdate.getName());
//        setAccNo(accountUpdate.getAccNo());
//        setIfsc(accountUpdate.getIfsc());
//        setMobile(accountUpdate.getMobile());
        return accountUpdate;
    }
}
