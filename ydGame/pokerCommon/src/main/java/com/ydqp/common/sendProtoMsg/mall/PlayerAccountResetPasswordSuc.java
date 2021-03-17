package com.ydqp.common.sendProtoMsg.mall;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.cfq.annotation.SendCommandAnnotation;
import com.cfq.message.AbstartCreateMessage;
import com.cfq.message.NetProtoMessage;
import com.ydqp.common.sendProtoMsg.player.PlayerResetPasswordSuc;
import lombok.Getter;
import lombok.Setter;

@SendCommandAnnotation(command = 1004108)
public class PlayerAccountResetPasswordSuc extends AbstartCreateMessage {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.BOOL, order = 1)
    private boolean success;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 2)
    private String message;

    @Override
    public NetProtoMessage encodeSendMessage() {
        NetProtoMessage netProtoMessage = new NetProtoMessage();
        netProtoMessage.getNetProtoMessageHead().setCmd(this.getCommand());
        try {
            Codec<PlayerAccountResetPasswordSuc> codec = ProtobufProxy.create(PlayerAccountResetPasswordSuc.class);
            byte[] bytes = codec.encode(this);
            netProtoMessage.getNetProtoMessageBody().setBody(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return netProtoMessage;
    }
}
