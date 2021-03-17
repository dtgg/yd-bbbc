package com.ydqp.lobby.handler.mall;

import com.alibaba.fastjson.JSONObject;
import com.cfq.annotation.ServerHandler;
import com.cfq.connection.ISession;
import com.cfq.handler.IServerHandler;
import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.message.AbstartParaseMessage;
import com.ydqp.common.data.ProductData;
import com.ydqp.common.entity.Product;
import com.ydqp.common.entity.ProductBuyHistory;
import com.ydqp.common.entity.Promotion;
import com.ydqp.common.receiveProtoMsg.mall.ProductList;
import com.ydqp.common.sendProtoMsg.mall.ProductListSuccess;
import com.ydqp.lobby.service.mall.ProductService;
import com.ydqp.lobby.service.mall.PromotionService;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@ServerHandler(command = 1004001, module = "mall")
public class ProductListHandler implements IServerHandler {
    private static Logger logger = LoggerFactory.getLogger(ProductList.class);

    @Override
    public void process(ISession iSession, AbstartParaseMessage abstartParaseMessage) {
        logger.info("Product list request: {}", JSONObject.toJSONString(abstartParaseMessage));
        ProductList productList = (ProductList) abstartParaseMessage;

        List<Product> products = ProductService.getInstance().findAllProduct();
        if (CollectionUtils.isEmpty(products)) {
            iSession.sendMessageByID(new ProductListSuccess(), productList.getConnId());
            return;
        }

        List<ProductData> productDataList = new ArrayList<>();

        List<Promotion> promotions = PromotionService.getInstance().findAllPromotion();
        if (CollectionUtils.isNotEmpty(promotions)) {
            logger.info("商品促销中");
            Map<Integer, Product> productMap = products.stream().collect(Collectors.toMap(Product::getId, Function.identity()));

            List<ProductBuyHistory> histories = ProductService.getInstance().findHistoriesByPlayerId(productList.getPlayerId());
            Map<Integer, ProductBuyHistory> historyMap = histories.stream()
                    .collect(Collectors.toMap(ProductBuyHistory::getPromotionId, Function.identity()));

            for (Promotion promotion : promotions) {
                ProductData productData;
                if (historyMap.get(promotion.getId()) == null || historyMap.get(promotion.getId()).getBuyNum() < promotion.getLimit()) {
                    productData = new ProductData(productMap.get(promotion.getProductId()), promotion, true);
                    logger.info("玩家未购买过该商品；playerId:{},,productId:{}",
                            productList.getPlayerId(), promotion.getProductId());
                } else {
                    productData = new ProductData(productMap.get(promotion.getProductId()), new Promotion(), false);
                    logger.info("玩家已购买过该商品；playerId:{},productId:{}",
                            productList.getPlayerId(), promotion.getProductId());
                }
                productDataList.add(productData);
            }
        } else {
            productDataList = products.stream().map(product ->
                    new ProductData(product, new Promotion(), false)).collect(Collectors.toList());
        }

        ProductListSuccess productListSuccess = new ProductListSuccess();
        productListSuccess.setProductData(productDataList);
        iSession.sendMessageByID(productListSuccess, productList.getConnId());
    }
}
