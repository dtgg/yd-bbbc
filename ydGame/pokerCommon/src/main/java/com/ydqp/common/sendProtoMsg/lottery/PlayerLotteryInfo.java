package com.ydqp.common.sendProtoMsg.lottery;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.ydqp.common.entity.PlayerLottery;
import lombok.Getter;
import lombok.Setter;

public class PlayerLotteryInfo {
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT64, order = 1)
    private long id;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT64, order = 2)
    private long playerId;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 3)
    private int type;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 4)
    private int lotteryId;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 5)
    private Integer select;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 6)
    private String number;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.DOUBLE, order = 7)
    private Double pay;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 8)
    private int status;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.DOUBLE, order = 9)
    private Double award;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 10)
    private int createTime;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 11)
    private int openTime;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 12)
    private String period;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.DOUBLE, order = 13)
    private Double fee;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 14)
    private String result;

    public PlayerLotteryInfo() {
    }

    public PlayerLotteryInfo(PlayerLottery playerLottery) {
        this.id = playerLottery.getId();
        this.playerId = playerLottery.getPlayerId();
        this.type = playerLottery.getType();
        this.lotteryId = playerLottery.getLotteryId();
        this.select = playerLottery.getSelected();
        this.number = playerLottery.getNumber();
        this.pay = playerLottery.getPay().doubleValue();
        this.status = playerLottery.getStatus();
        this.award = playerLottery.getAward() == null ? null : playerLottery.getAward().doubleValue();
        this.createTime = playerLottery.getCreateTime();
        this.openTime = playerLottery.getOpenTime();
        this.period = playerLottery.getPeriod();
        this.fee = playerLottery.getFee().doubleValue();
        this.result = playerLottery.getResult();
    }
}
