package com.ydqp.common.data;

import com.ydqp.common.entity.Player;
import lombok.Getter;
import lombok.Setter;

public class PlayerData {

    @Setter
    @Getter
    private long playerId;
    @Setter
    @Getter
    private long sessionId;
    @Setter
    @Getter
    private String playerName;
    @Setter
    @Getter
    private String nickName;
    @Setter
    @Getter
    private String headUrl;
    @Setter
    @Getter
    private int roomId;
    @Setter
    @Getter
    private Double zjPoint;
//    @Setter
//    @Getter
//    private ISession session;
    @Setter
    @Getter
    private int appId;
    @Setter
    @Getter
    private long kfId;
    @Setter
    @Getter
    private int registerTime;

    public PlayerData(){}

    public PlayerData (Player player) {
        this.playerId = player.getId();
        this.playerName = player.getPlayerName() == null ? "" : player.getPlayerName();
        this.nickName = player.getNickname();
        this.headUrl = player.getHeadUrl();
        this.roomId = player.getRoomId();
        this.zjPoint = player.getZjPoint();
        this.appId = player.getAppId();
        this.registerTime = player.getCreateTime();
        this.kfId = player.getKfId();
    }

}
