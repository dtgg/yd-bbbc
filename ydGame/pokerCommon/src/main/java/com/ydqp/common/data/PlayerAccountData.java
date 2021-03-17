package com.ydqp.common.data;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.ydqp.common.entity.PlayerAccount;
import lombok.Getter;
import lombok.Setter;

public class PlayerAccountData {

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
    private String mobile;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 4)
    private String accNo;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 5)
    private String ifsc;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 6)
    private String bankName;

//    @Getter
//    @Setter
//    @Protobuf(fieldType = FieldType.INT32, order = 6)
//    private int withdrawalCount;

    public PlayerAccountData() {
    }

    //, int withdrawalCount
    public PlayerAccountData(PlayerAccount playerAccount) {
        this.playerId = playerAccount.getPlayerId();
        this.name = playerAccount.getName();
        this.accNo = playerAccount.getAccNo();
        this.ifsc = playerAccount.getIfsc();
        this.mobile = playerAccount.getMobile();
        this.bankName = playerAccount.getBankName();
//        this.withdrawalCount = withdrawalCount;
    }
}
