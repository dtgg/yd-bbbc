package com.ydqp.common.data;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.ydqp.common.entity.Product;
import com.ydqp.common.entity.Promotion;
import lombok.Getter;
import lombok.Setter;

public class ProductData {

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 1)
    private int id;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.INT32, order = 2)
    private int type;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 3)
    private String name;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.DOUBLE, order = 4)
    private double amount;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.DOUBLE, order = 5)
    private double point;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.STRING, order = 6)
    private String promotionName;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.DOUBLE, order = 7)
    private double promotionAmount;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.DOUBLE, order = 8)
    private double promotionPoint;

    @Getter
    @Setter
    @Protobuf(fieldType = FieldType.BOOL, order = 9)
    private boolean promotion;

    public ProductData() {}

    public ProductData(Product product, Promotion promotion, boolean isPromotion) {
        this.id = product.getId();
        this.type = product.getType();
        this.name = product.getName();
        this.amount = product.getAmount();
        this.point = product.getPoint();
        this.promotionName = promotion.getPromotionName();
        this.promotionAmount = promotion.getPromotionAmount();
        this.promotionPoint = promotion.getPromotionPoint();
        this.promotion = isPromotion;
    }
}
