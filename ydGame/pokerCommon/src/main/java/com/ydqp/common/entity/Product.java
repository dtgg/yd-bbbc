package com.ydqp.common.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author cfq
 */
public class Product implements Serializable {
  
       public static final long serialVersionUID = 1594747043991l;
       private Map<String,Object> entityMap = new HashMap<String,Object>(16,1);
	   	public static final String ID_NAME = "id";
	public static final String TYPE_NAME = "type";
	public static final String NAME_NAME = "name";
	public static final String AMOUNT_NAME = "amount";
	public static final String POINT_NAME = "point";
	public static final String ENABLED_NAME = "enabled";


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
	private String name;
	public String getName() {
	return name;
	}
 	 public void setName(String name){
	 this.name = name;
	 entityMap.put(NAME_NAME, name);
	}
	private double amount;
	public double getAmount() {
	return amount;
	}
 	 public void setAmount(double amount){
	 this.amount = amount;
	 entityMap.put(AMOUNT_NAME, amount);
	}
	private double point;
	public double getPoint() {
	return point;
	}
 	 public void setPoint(double point){
	 this.point = point;
	 entityMap.put(POINT_NAME, point);
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
