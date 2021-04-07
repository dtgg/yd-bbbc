package com.ydqp.common.poker.room;

import com.cfq.connection.ISession;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartCreateMessage;
import com.ydqp.common.data.PlayerData;
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

    @Getter
    @Setter
    private int basePoint;

    @Getter
    @Setter
    private int raceId;

    /**
     * 发送 消息给房间的所有用户
     * @param abstartCreateMessage
     */
    public void sendMessageToBattles(AbstartCreateMessage abstartCreateMessage){
        //现发给正在打牌的用户
        for(Map.Entry<Long, BattleRole> entry : getBattleRoleMap().entrySet()) {

            if(entry.getValue().isHaveBet()) {
                ISession iSession = entry.getValue().getISession();
                if (iSession != null) {
                    iSession.sendMessageByID(abstartCreateMessage, entry.getValue().getConnId());
                }
            }

        }

        //发送给非战斗用户
        for(Map.Entry<Long, BattleRole> entry : getBattleRoleMap().entrySet()) {
            if(entry.getValue().isHaveBet()) {
                continue;
            }
            ISession iSession = entry.getValue().getISession();
            if (iSession != null) {
                iSession.sendMessageByID(abstartCreateMessage, entry.getValue().getConnId());
            }
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
        if (iSession != null) {
            iSession.sendMessageByID(abstartCreateMessage, battleRole.getConnId());
        }
    }

    public void sendMessageToBattlesByFilter(AbstartCreateMessage abstartCreateMessage, long playerId){
        //现发给正在打牌的用户
        for(Map.Entry<Long, BattleRole> entry : getBattleRoleMap().entrySet()) {

            if (entry.getValue().getPlayerId() == playerId) {
                continue;
            }
            if(entry.getValue().isHaveBet()) {
                ISession iSession = entry.getValue().getISession();
                if (iSession != null) {
                    iSession.sendMessageByID(abstartCreateMessage, entry.getValue().getConnId());
                }
            }

        }

        //发送给非战斗用户
        for(Map.Entry<Long, BattleRole> entry : getBattleRoleMap().entrySet()) {
            if (entry.getValue().getPlayerId() == playerId) {
                continue;
            }
            if(entry.getValue().isHaveBet()) {
                continue;
            }
            ISession iSession = entry.getValue().getISession();
            if (iSession != null) {
                iSession.sendMessageByID(abstartCreateMessage, entry.getValue().getConnId());
            }
        }
    }

    public BattleRole enterRoom (PlayerData playerData, ISession iSession){
        logger.info("enterRoom 玩家进入房间，生成battleRole信息，roomId = {}, playerid = {} ", this.getRoomId(), playerData.getPlayerId());
        BattleRole battleRole = getBattleRoleMap().get(playerData.getPlayerId());
        if (battleRole != null){
            //已经在房间了
            battleRole.setConnId(playerData.getSessionId());
            battleRole.setPlayerZJ(playerData.getZjPoint());
            battleRole.setPlayerUrl(playerData.getHeadUrl());
            battleRole.setISession(iSession);
            return battleRole;
        }
        //新玩家进来
        battleRole = new BattleRole();
        battleRole.setConnId(playerData.getSessionId());
        battleRole.setPlayerZJ(playerData.getZjPoint());
        battleRole.setPlayerId(playerData.getPlayerId());
        battleRole.setPlayerName(playerData.getNickName());
        battleRole.setPlayerUrl(playerData.getHeadUrl());
        battleRole.setISession(iSession);

        getBattleRoleMap().put(playerData.getPlayerId(),battleRole);
        logger.info("enterRoom player enter room playerId = {}" , playerData.getPlayerId());
        return battleRole;
    }

    public BattleRole enterRoomByRace (PlayerData playerData, ISession iSession){
        logger.info("enterRoom 玩家进入房间，生成battleRole信息，roomId = {}, playerid = {} ", this.getRoomId(), playerData.getPlayerId());
        BattleRole battleRole = getBattleRoleMap().get(playerData.getPlayerId());
        if (battleRole != null){
            //已经在房间了
            if (battleRole.getIsOut() == 1) {
                return null;
            }
            battleRole.setConnId(playerData.getSessionId());
            battleRole.setPlayerUrl(playerData.getHeadUrl());
            battleRole.setISession(iSession);
            return battleRole;
        }
        //新玩家进来
        battleRole = new BattleRole();
        battleRole.setConnId(playerData.getSessionId());
        battleRole.setPlayerZJ(10000.0);
        battleRole.setPlayerId(playerData.getPlayerId());
        battleRole.setPlayerName(playerData.getNickName());
        battleRole.setPlayerUrl(playerData.getHeadUrl());
        battleRole.setISession(iSession);

        getBattleRoleMap().put(playerData.getPlayerId(),battleRole);
        logger.info("enterRoom player enter room playerId = {}" , playerData.getPlayerId());
        return battleRole;
    }

}
