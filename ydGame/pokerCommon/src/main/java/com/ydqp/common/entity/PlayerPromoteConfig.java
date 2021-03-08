package com.ydqp.common.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author cfq
 */
public class PlayerPromoteConfig implements Serializable {
  
       public static final long serialVersionUID = 1599023301830l;
       private Map<String,Object> entityMap = new HashMap<String,Object>(16,1);
	   	public static final String ID_NAME = "id";
	public static final String SUPERIORRATE_NAME = "superiorRate";
	public static final String GRANDRATE_NAME = "grandRate";
	public static final String REFERRALLINK_NAME = "referralLink";


    	private int id;
	public int getId() {
	return id;
	}
 	 public void setId(int id){
	 this.id = id;
	 entityMap.put(ID_NAME, id);
	}
	private String superiorRate;
	public String getSuperiorRate() {
	return superiorRate;
	}
 	 public void setSuperiorRate(String superiorRate){
	 this.superiorRate = superiorRate;
	 entityMap.put(SUPERIORRATE_NAME, superiorRate);
	}
	private String grandRate;
	public String getGrandRate() {
	return grandRate;
	}
 	 public void setGrandRate(String grandRate){
	 this.grandRate = grandRate;
	 entityMap.put(GRANDRATE_NAME, grandRate);
	}
	private String referralLink;
	public String getReferralLink() {
		return referralLink;
	}
	public void setReferralLink(String referralLink) {
		this.referralLink = referralLink;
		entityMap.put(REFERRALLINK_NAME, referralLink);
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
