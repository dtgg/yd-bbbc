package com.ydqp.common.poker.room;

import com.cfq.connection.ISession;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import lombok.Getter;
import lombok.Setter;


//战斗角色，主要功能就是打牌
public class BattleRole {

    private static final Logger logger = LoggerFactory.getLogger(BattleRole.class);
    @Setter
    @Getter
    private long connId;

    @Getter
    @Setter
    private long playerId;
    @Getter
    @Setter
    private String playerName;
    @Getter
    @Setter
    private String playerUrl;

    @Setter
    @Getter
    private Double playerZJ;

    @Getter
    @Setter
    private ISession iSession;

    @Getter
    @Setter
    private boolean haveBet;

    @Getter
    @Setter
    private int rank;

}
