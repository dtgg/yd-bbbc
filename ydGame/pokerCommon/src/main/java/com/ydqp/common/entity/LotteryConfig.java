package com.ydqp.common.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author cfq
 */
public class LotteryConfig implements Serializable {
  
       public static final long serialVersionUID = 1599824177058l;
       private Map<String,Object> entityMap = new HashMap<String,Object>(16,1);
	   	public static final String ID_NAME = "id";
	public static final String LOTTERYTYPE_NAME = "lotteryType";
	public static final String FEERATEMIN_NAME = "feeRateMin";
	public static final String FEERATEMAX_NAME = "feeRateMax";
	public static final String DRAWRANGE_NAME = "drawRange";
	public static final String ENABLED_NAME = "enabled";
	public static final String BALANCE_NAME = "balance";
	public static final String FREQUENCY_NAME = "frequency";


    	private int id;
	public int getId() {
	return id;
	}
 	 public void setId(int id){
	 this.id = id;
	 entityMap.put(ID_NAME, id);
	}
	private int lotteryType;
	public int getLotteryType() {
	return lotteryType;
	}
 	 public void setLotteryType(int lotteryType){
	 this.lotteryType = lotteryType;
	 entityMap.put(LOTTERYTYPE_NAME, lotteryType);
	}
	private BigDecimal feeRateMin;
	public BigDecimal getFeeRateMin() {
	return feeRateMin;
	}
 	 public void setFeeRateMin(BigDecimal feeRateMin){
	 this.feeRateMin = feeRateMin;
	 entityMap.put(FEERATEMIN_NAME, feeRateMin);
	}
	private BigDecimal feeRateMax;
	public BigDecimal getFeeRateMax() {
	return feeRateMax;
	}
 	 public void setFeeRateMax(BigDecimal feeRateMax){
	 this.feeRateMax = feeRateMax;
	 entityMap.put(FEERATEMAX_NAME, feeRateMax);
	}
	private String drawRange;
	public String getDrawRange() {
	return drawRange;
	}
 	 public void setDrawRange(String drawRange){
	 this.drawRange = drawRange;
	 entityMap.put(DRAWRANGE_NAME, drawRange);
	}
	private int enabled;
	public int getEnabled() {
	return enabled;
	}
 	 public void setEnabled(int enabled){
	 this.enabled = enabled;
	 entityMap.put(ENABLED_NAME, enabled);
	}
	private int balance;
	public int getBalance() {
	return balance;
	}
 	 public void setBalance(int balance){
	 this.balance = balance;
	 entityMap.put(BALANCE_NAME, balance);
	}
	private int frequency;
	public int getFrequency() {
	return frequency;
	}
 	 public void setFrequency(int frequency){
	 this.frequency = frequency;
	 entityMap.put(FREQUENCY_NAME, frequency);
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
