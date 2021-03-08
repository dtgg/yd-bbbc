package com.ydqp.common.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author cfq
 */
public class Lottery implements Serializable {
  
       public static final long serialVersionUID = 1598489719097l;
       private Map<String,Object> entityMap = new HashMap<String,Object>(16,1);
	   	public static final String ID_NAME = "id";
	public static final String TYPE_NAME = "type";
	public static final String PERIOD_NAME = "period";
	public static final String PRICE_NAME = "price";
	public static final String NUMBER_NAME = "number";
	public static final String STATUS_NAME = "status";
	public static final String CREATETIME_NAME = "createTime";
	public static final String OPENTIME_NAME = "openTime";
	public static final String TOTALPAY_NAME = "totalPay";
	public static final String TOTALAWARD_NAME = "totalAward";
	public static final String TOTALPROFIT_NAME = "totalProfit";
	public static final String TOTALFEE_NAME = "totalFee";


    	private int id;
	public int getId() {
	return id;
	}
 	 public void setId(int id){
	 this.id = id;
	 entityMap.put(ID_NAME, id);
	}
	private int type;
	public int getType() {
	return type;
	}
 	 public void setType(int type){
	 this.type = type;
	 entityMap.put(TYPE_NAME, type);
	}
	private int period;
	public int getPeriod() {
	return period;
	}
 	 public void setPeriod(int period){
	 this.period = period;
	 entityMap.put(PERIOD_NAME, period);
	}
	private String price;
	public String getPrice() {
	return price;
	}
 	 public void setPrice(String price){
	 this.price = price;
	 entityMap.put(PRICE_NAME, price);
	}
	private String number;
	public String getNumber() {
	return number;
	}
 	 public void setNumber(String number){
	 this.number = number;
	 entityMap.put(NUMBER_NAME, number);
	}
	private int status;
	public int getStatus() {
	return status;
	}
 	 public void setStatus(int status){
	 this.status = status;
	 entityMap.put(STATUS_NAME, status);
	}
	private int createTime;
	public int getCreateTime() {
	return createTime;
	}
 	 public void setCreateTime(int createTime){
	 this.createTime = createTime;
	 entityMap.put(CREATETIME_NAME, createTime);
	}
	private int openTime;
	public int getOpenTime() {
	return openTime;
	}
 	 public void setOpenTime(int openTime){
	 this.openTime = openTime;
	 entityMap.put(OPENTIME_NAME, openTime);
	}
	private BigDecimal totalPay;
	public BigDecimal getTotalPay() {
		return totalPay;
	}
	public void setTotalPay(BigDecimal totalPay) {
		this.totalPay = totalPay;
		entityMap.put(TOTALPAY_NAME, totalPay);
	}
	private BigDecimal totalAward;
	public BigDecimal getTotalAward() {
		return totalAward;
	}
	public void setTotalAward(BigDecimal totalAward) {
		this.totalAward = totalAward;
		entityMap.put(TOTALAWARD_NAME, totalAward);
	}
	private BigDecimal totalProfit;
	public BigDecimal getTotalProfit() {
		return totalProfit;
	}
	public void setTotalProfit(BigDecimal totalProfit) {
		this.totalProfit = totalProfit;
		entityMap.put(TOTALPROFIT_NAME, totalProfit);
	}
	private BigDecimal totalFee;
	public BigDecimal getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(BigDecimal totalFee) {
		this.totalFee = totalFee;
		entityMap.put(TOTALFEE_NAME, totalFee);
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
