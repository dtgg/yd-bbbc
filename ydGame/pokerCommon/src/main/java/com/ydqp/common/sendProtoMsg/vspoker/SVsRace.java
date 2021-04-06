package com.ydqp.common.sendProtoMsg.vspoker;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.cfq.annotation.GenProto;
import com.ydqp.common.entity.VsRace;
import lombok.Getter;
import lombok.Setter;

@GenProto(modulePro = "vsPoker")
public class SVsRace {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32 , order = 1, description = "赛事ID")
    private int id;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32 , order = 2, description = "赛事类型")
    private int raceType;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32 , order = 3, description = "开始时间")
    private int beginTime;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32 , order = 4, description = "当前人数")
    private int curPlayerNum;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32 , order = 5, description = "最多人数")
    private int maxPlayerNum;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32 , order = 6, description = "赛事状态")
    private int status;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32 , order = 7, description = "报名状态")
    private int joinStatus;

    public SVsRace() {

    }

    public SVsRace(VsRace vsRace) {
        this.id = vsRace.getId();
        this.raceType = vsRace.getRaceType();
        this.beginTime = vsRace.getBeginTime();
        this.curPlayerNum = vsRace.getCurPlayerNum();
        this.maxPlayerNum = vsRace.getMaxPlayerNum();
        this.status = vsRace.getStatus();
    }
}
