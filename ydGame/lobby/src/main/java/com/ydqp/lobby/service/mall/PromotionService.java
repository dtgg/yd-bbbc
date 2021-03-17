package com.ydqp.lobby.service.mall;

import com.ydqp.common.entity.Promotion;
import com.ydqp.lobby.dao.mall.PromotionDao;

import java.util.List;

public class PromotionService {

    private PromotionService() {}

    private static PromotionService instance = new PromotionService();

    public static PromotionService getInstance() {
        if (instance == null) instance = new PromotionService();
        return instance;
    }

    public List<Promotion> findAllPromotion() {
        return PromotionDao.getInstance().findAllPromotion();
    }

    public Promotion findByProductId(int productId) {
        return PromotionDao.getInstance().findByProductId(productId);
    }
}
