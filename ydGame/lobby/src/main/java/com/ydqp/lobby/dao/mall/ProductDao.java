package com.ydqp.lobby.dao.mall;

import com.cfq.jdbc.JdbcOrm;
import com.ydqp.common.entity.Product;
import com.ydqp.common.entity.ProductBuyHistory;

import java.util.List;
import java.util.Map;

public class ProductDao {

    private ProductDao() {}

    private static ProductDao instance = new ProductDao();

    public static ProductDao getInstance() {
        if (instance == null) instance = new ProductDao();
        return instance;
    }

    private static final String FIND_ALL_BY_TYPE = "select * from product where enabled = 1;";
    public List<Product> findAllProduct() {
        return JdbcOrm.getInstance().getListBean(FIND_ALL_BY_TYPE, Product.class);
    }

    private static final String FIND_BY_ID = "select * from product where enabled = 1 and id = ";
    public Product findById(int id) {
        String sql = FIND_BY_ID + id + ";";
        return (Product) JdbcOrm.getInstance().getBean(sql, Product.class);
    }


    private static final String FIND_HISTORY_BY_PLAYER_ID = "select * from product_buy_history where playerId = ";
    public List<ProductBuyHistory> findHistoriesByPlayerId(long playerId) {
        String sql = FIND_HISTORY_BY_PLAYER_ID + playerId + ";";
        return JdbcOrm.getInstance().getListBean(sql, ProductBuyHistory.class);
    }

    public ProductBuyHistory findHistoryByPlayerIddAndProductId(long playerId, int productId) {
        String sql = "select * from product_buy_history where playerId = " + playerId + " and productId = " + productId + ";";
        return (ProductBuyHistory) JdbcOrm.getInstance().getBean(sql, ProductBuyHistory.class);
    }

    public ProductBuyHistory findHistoryByPlayerIdAndPromotionId(long playerId, int promotionId) {
        String sql = "select * from product_buy_history where playerId = " + playerId + " and promotionId = " + promotionId + ";";
        return (ProductBuyHistory) JdbcOrm.getInstance().getBean(sql, ProductBuyHistory.class);
    }

    public void saveProductBuyHistory(Map<String, Object> params) {
        JdbcOrm.getInstance().insert("product_buy_history", params);
    }

    public void updateProductBuyHistory(int i, long id) {
        String sql = "update product_buy_history set buyNum = buyNum + " + i + " where id = " + id + ";";
        JdbcOrm.getInstance().update(sql);
    }
}
