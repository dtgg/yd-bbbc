package com.ydqp.common.sendProtoMsg.lottery;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.ydqp.common.lottery.role.LotteryBattleRole;
import lombok.Getter;
import lombok.Setter;

public class LotteryBattleRoleInfo {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT64, order = 1)
    private long playerId;
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.DOUBLE, order = 2)
    private double playerZJ;

    public LotteryBattleRoleInfo() {
    }

    public LotteryBattleRoleInfo(LotteryBattleRole lotteryBattleRole) {
        this.playerId = lotteryBattleRole.getPlayerId();
        this.playerZJ = lotteryBattleRole.getPlayerZJ();
    }
}
