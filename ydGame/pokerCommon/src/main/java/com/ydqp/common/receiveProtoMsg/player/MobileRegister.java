package com.ydqp.common.receiveProtoMsg.player;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.cfq.annotation.ReceiveCommandAnnotation;
import com.cfq.message.AbstartParaseMessage;
import com.cfq.message.NetProtoMessage;
import lombok.Getter;
import lombok.Setter;

@ReceiveCommandAnnotation(command = 1000031)
public class MobileRegister extends AbstartParaseMessage {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 1)
    private String mobile;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 2)
    private String password;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 3)
    private String repeatPassword;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 4)
    private String verificationCode;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 5)
    private String referralCode;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 6)
    private int appId;

    @Override
    public AbstartParaseMessage paraseMessage(NetProtoMessage netProtoMessage) throws Exception {
        byte[] body = netProtoMessage.getNetProtoMessageBody().getBody();

        Codec<MobileRegister> mobileRegisterCodec = ProtobufProxy.create(MobileRegister.class);
        MobileRegister mobileRegister = mobileRegisterCodec.decode(body);

//        setMobile(mobileRegister.getMobile());
//        setPassword(mobileRegister.getPassword());
//        setRepeatPassword(mobileRegister.getRepeatPassword());
//        setVerificationCode(mobileRegister.getVerificationCode());
//        setReferralCode(mobileRegister.getReferralCode());
//        setAppId(mobileRegister.getAppId());
        return  mobileRegister;
    }
}
