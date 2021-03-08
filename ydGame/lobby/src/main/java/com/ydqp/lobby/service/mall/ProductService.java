package com.ydqp.lobby.service.mall;

import com.ydqp.common.entity.Product;
import com.ydqp.common.entity.ProductBuyHistory;
import com.ydqp.lobby.dao.mall.ProductDao;

import java.util.List;
import java.util.Map;

public class ProductService {

    private ProductService() {}

    private static ProductService instance = new ProductService();

    public static ProductService getInstance() {
        if (instance == null) instance = new ProductService();
        return instance;
    }

    public List<Product> findAllByProductType(int productType) {
        return ProductDao.getInstance().findAllByType(productType);
    }

    public Product findById(int productId) {
        return ProductDao.getInstance().findById(productId);
    }

    public List<ProductBuyHistory> findHistoriesByPlayerId(long playerId) {
        return ProductDao.getInstance().findHistoriesByPlayerId(playerId);
    }

    public ProductBuyHistory findHistoryByPlayerIddAndProductId(long playerId, int productId) {
        return ProductDao.getInstance().findHistoryByPlayerIddAndProductId(playerId, productId);
    }

    public ProductBuyHistory findHistoryByPlayerIdAndPromotionId(long playerId, int promotionId) {
        return ProductDao.getInstance().findHistoryByPlayerIdAndPromotionId(playerId, promotionId);
    }

    public void saveProductBuyHistory(Map<String, Object> params) {
        ProductDao.getInstance().saveProductBuyHistory(params);
    }

    public void updateProductBuyHistory(int i, long id) {
        ProductDao.getInstance().updateProductBuyHistory(i, id);
    }
}
