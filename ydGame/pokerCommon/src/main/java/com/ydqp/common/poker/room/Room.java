package com.ydqp.common.poker.room;

import com.cfq.connection.ISession;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartCreateMessage;
import com.ydqp.common.poker.ICardPoker;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Room implements IRoom{

    private static final Logger logger = LoggerFactory.getLogger(Room.class);

    @Setter
    @Getter
    private int roomId;

    @Getter
    @Setter
    private int status;

    @Getter
    @Setter
    private int roomType; // 1 免费赛  2 报名赛   3 zj

    @Setter
    @Getter
    private Map<Long, BattleRole> battleRoleMap = new ConcurrentHashMap<>();

    @Setter
    @Getter
    private int curWaitTime;

    @Getter
    @Setter
    private ICardPoker iCardPoker;

    /**
     * 发送 消息给房间的所有用户
     * @param abstartCreateMessage
     */
    public void sendMessageToBattles(AbstartCreateMessage abstartCreateMessage){
        //现发给正在打牌的用户
        for(Map.Entry<Long, BattleRole> entry : getBattleRoleMap().entrySet()) {

            if(entry.getValue().isHaveBet()) {
                ISession iSession = entry.getValue().getISession();
                iSession.sendMessageByID(abstartCreateMessage, entry.getValue().getConnId());
            }

        }

        //发送给非战斗用户
        for(Map.Entry<Long, BattleRole> entry : getBattleRoleMap().entrySet()) {
            if(entry.getValue().isHaveBet()) {
                continue;
            }
            ISession iSession = entry.getValue().getISession();
            iSession.sendMessageByID(abstartCreateMessage, entry.getValue().getConnId());
        }
    }

    /**
     * 发送给房间里的指定用户
     * @param abstartCreateMessage
     * @param playerId
     */
    public void sendMessageToBattle(AbstartCreateMessage abstartCreateMessage, long playerId){

        BattleRole battleRole = getBattleRoleMap().get(playerId);
        if (battleRole == null) {
            return;
        }
        ISession iSession = battleRole.getISession();
        iSession.sendMessageByID(abstartCreateMessage, battleRole.getConnId());
    }

}
