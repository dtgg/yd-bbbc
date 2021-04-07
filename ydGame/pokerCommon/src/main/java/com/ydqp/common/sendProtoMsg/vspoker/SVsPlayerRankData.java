package com.ydqp.common.sendProtoMsg.vspoker;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.cfq.annotation.GenProto;
import com.ydqp.common.entity.VsPlayerRace;
import lombok.Getter;
import lombok.Setter;

@GenProto(modulePro = "vsPoker")
public class SVsPlayerRankData {
    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT64 , order = 1, description = "赛事ID")
    private long playerId;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING , order = 2, description = "赛事ID")
    private String playerName;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.DOUBLE , order = 3, description = "赛事ID")
    private double bonus;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.DOUBLE , order = 4, description = "赛事战绩")
    private double point;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32 , order = 5, description = "排序")
    private int rank;

    public SVsPlayerRankData() {}

    public SVsPlayerRankData(VsPlayerRace vsPlayerRace, String playerName) {
        this.playerId = vsPlayerRace.getPlayerId();
        this.playerName = playerName;
        this.bonus = vsPlayerRace.getBonus();
        this.point = vsPlayerRace.getPoint();
        this.rank = vsPlayerRace.getRank();
    }
}
