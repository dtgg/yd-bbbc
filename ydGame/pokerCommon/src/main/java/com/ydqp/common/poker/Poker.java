package com.ydqp.common.poker;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.cfq.annotation.GenProto;
import lombok.Getter;
import lombok.Setter;

@GenProto(modulePro = "battle")
public class Poker {

    @Setter
    @Getter
    @Protobuf(fieldType = FieldType.STRING, order = 1)
    private String tag; //图片扑克花色的表示（代表的是黑桃A,红桃B, 梅花C、方块D）ABCD

    @Setter
    @Getter
    @Protobuf(fieldType = FieldType.INT32, order = 2)
    private Integer num; //表示扑克牌面的大小2-14

    @Setter
    @Getter
    @Protobuf(fieldType = FieldType.BOOL, order = 3)
    private Boolean isLaizigou = false;
    @Setter
    @Getter
    private int orderId;

    @Setter
    @Getter
    private Poker replacePoker;

    public Poker(){}

    public Poker(String tag, int num) {
        this.tag = tag;
        this.num = num;
    }

    public Poker(String tag, int num, int orderId) {
        this.tag = tag;
        this.num = num;
        this.orderId = orderId;
    }

    public Poker pokerClone () {
        Poker pokerClone = new Poker(this.getTag(), this.getNum());
        pokerClone.setIsLaizigou(this.isLaizigou);
        return pokerClone;
    }

}
