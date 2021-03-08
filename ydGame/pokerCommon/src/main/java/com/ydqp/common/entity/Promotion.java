package com.ydqp.common.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author cfq
 */
public class Promotion implements Serializable {
  
       public static final long serialVersionUID = 1594739678052l;
       private Map<String,Object> entityMap = new HashMap<String,Object>(16,1);
	   	public static final String ID_NAME = "id";
	public static final String PRODUCTID_NAME = "productId";
	public static final String PRODUCTTYPE_NAME = "productType";
	public static final String PROMOTIONNAME_NAME = "promotionName";
	public static final String PROMOTIONAMOUNT_NAME = "promotionAmount";
	public static final String PROMOTIONPOINT_NAME = "promotionPoint";
	public static final String LIMIT_NAME = "limit";
	public static final String ENABLED_NAME = "enabled";


    	private int id;
	public int getId() {
	return id;
	}
 	 public void setId(int id){
	 this.id = id;
	 entityMap.put(ID_NAME, id);
	}
	private int productId;
	public int getProductId() {
	return productId;
	}
 	 public void setProductId(int productId){
	 this.productId = productId;
	 entityMap.put(PRODUCTID_NAME, productId);
	}
	private int productType;
	public int getProductType() {
	return productType;
	}
 	 public void setProductType(int productType){
	 this.productType = productType;
	 entityMap.put(PRODUCTTYPE_NAME, productType);
	}
	private String promotionName;
	public String getPromotionName() {
	return promotionName;
	}
 	 public void setPromotionName(String promotionName){
	 this.promotionName = promotionName;
	 entityMap.put(PROMOTIONNAME_NAME, promotionName);
	}
	private double promotionAmount;
	public double getPromotionAmount() {
	return promotionAmount;
	}
 	 public void setPromotionAmount(double promotionAmount){
	 this.promotionAmount = promotionAmount;
	 entityMap.put(PROMOTIONAMOUNT_NAME, promotionAmount);
	}
	private double promotionPoint;
	public double getPromotionPoint() {
	return promotionPoint;
	}
 	 public void setPromotionPoint(double promotionPoint){
	 this.promotionPoint = promotionPoint;
	 entityMap.put(PROMOTIONPOINT_NAME, promotionPoint);
	}
	private int limit;
	public int getLimit() {
	return limit;
	}
 	 public void setLimit(int limit){
	 this.limit = limit;
	 entityMap.put(LIMIT_NAME, limit);
	}
	private int enabled;
	public int getEnabled() {
	return enabled;
	}
 	 public void setEnabled(int enabled){
	 this.enabled = enabled;
	 entityMap.put(ENABLED_NAME, enabled);
	}



    public Object getValueByFieldName(String fieldName) {
        return this.entityMap.get(fieldName);
    }

    public Map<String, Object> getParameterMap() {
        return this.entityMap;
    }
	
	@Override
    public String toString(){
        return this.entityMap.toString();
    }
}
