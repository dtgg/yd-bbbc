package com.ydqp.common.lottery.role;

import com.cfq.connection.ISession;
import com.ydqp.common.data.PlayerData;
import lombok.Getter;
import lombok.Setter;

public class LotteryBattleRole {

    @Getter
    @Setter
    private ISession iSession;
    @Getter
    @Setter
    private long connId;
    @Getter
    @Setter
    private long playerId;
    @Getter
    @Setter
    private double playerZJ;
    @Getter
    @Setter
    private int roomId;
    @Getter
    @Setter
    private Boolean buying;

    public LotteryBattleRole() {

    }

    public LotteryBattleRole(PlayerData playerData, ISession iSession) {
        this.iSession = iSession;
        this.connId = playerData.getSessionId();
        this.playerId = playerData.getPlayerId();
        this.playerZJ = playerData.getZjPoint();
    }
}
