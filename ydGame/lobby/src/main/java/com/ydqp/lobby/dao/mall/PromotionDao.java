package com.ydqp.lobby.dao.mall;

import com.cfq.jdbc.JdbcOrm;
import com.ydqp.common.entity.Product;
import com.ydqp.common.entity.Promotion;

import java.util.List;

public class PromotionDao {

    private PromotionDao() {
    }

    private static PromotionDao instance = new PromotionDao();

    public static PromotionDao getInstance() {
        if (instance == null) instance = new PromotionDao();
        return instance;
    }

    private static final String FIND_ALL_BY_TYPE = "select * from promotion where enabled = 1;";

    public List<Promotion> findAllPromotion() {
        return JdbcOrm.getInstance().getListBean(FIND_ALL_BY_TYPE, Promotion.class);
    }

    private static final String FIND_BY_PRODUCT_ID = "select * from promotion where enabled = 1 and productId = ";

    public Promotion findByProductId(int productId) {
        String sql = FIND_BY_PRODUCT_ID + productId + ";";
        return (Promotion) JdbcOrm.getInstance().getBean(sql, Promotion.class);
    }
}
