package com.ydqp.common.sendProtoMsg.player;

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

import java.io.IOException;

@SendCommandAnnotation(command = 1000000)
@GenProto(modulePro = "lobby")
public class LobbyError extends AbstartCreateMessage{

    @Setter
    @Getter
    @Protobuf(fieldType = FieldType.INT32, order = 1, description = "错误码")
    private int errorCode;
    @Setter
    @Getter
    @Protobuf(fieldType = FieldType.STRING, order = 2, description = "错误信息")
    private String errorMsg;

    @Override
    public NetProtoMessage encodeSendMessage() {
        NetProtoMessage netProtoMessage = new NetProtoMessage();
        netProtoMessage.getNetProtoMessageHead().setCmd(this.getCommand());

        try {
            Codec<LobbyError> lobbyErrorCodec = ProtobufProxy
                    .create(LobbyError.class);
            //LobbyProto.lobbyError.Builder builder = LobbyProto.lobbyError.newBuilder();
//            setErrorCode(this.getErrorCode());
//            setErrorMsg(this.getErrorMsg());
            byte[] bytes = lobbyErrorCodec.encode(this);
            netProtoMessage.getNetProtoMessageBody().setBody(bytes);

        } catch (IOException e) {
            e.printStackTrace();
        }


        return netProtoMessage;
    }
}
