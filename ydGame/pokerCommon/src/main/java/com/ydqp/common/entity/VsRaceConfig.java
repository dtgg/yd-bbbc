package com.ydqp.common.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cfq
 */
public class VsRaceConfig implements Serializable {

    public static final long serialVersionUID = 1616661867772l;
    private Map<String, Object> entityMap = new HashMap<String, Object>(16, 1);
    public static final String ID_NAME = "id";
    public static final String BASEPOINT_NAME = "basePoint";
    public static final String FREQUENCY_NAME = "frequency";
    public static final String ENABLED_NAME = "enabled";
    public static final String RACETYP_NAME = "raceType";
    public static final String KILLAREA_NAME = "killArea";


    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        entityMap.put(ID_NAME, id);
    }

    private int basePoint;

    public int getBasePoint() {
        return basePoint;
    }

    public void setBasePoint(int basePoint) {
        this.basePoint = basePoint;
        entityMap.put(BASEPOINT_NAME, basePoint);
    }

    private int frequency;

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
        entityMap.put(FREQUENCY_NAME, frequency);
    }

    private int enabled;

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
        entityMap.put(ENABLED_NAME, enabled);
    }

    private int raceType;

    public int getRaceType() {
        return raceType;
    }

    public void setRaceType(int raceType) {
        this.raceType = raceType;
        entityMap.put(RACETYP_NAME, raceType);
    }

    private int killArea;

    public int getKillArea() {
        return killArea;
    }

    public void setKillArea(int killArea) {
        this.killArea = killArea;
        entityMap.put(KILLAREA_NAME, killArea);
    }

    public Object getValueByFieldName(String fieldName) {
        return this.entityMap.get(fieldName);
    }

    public Map<String, Object> getParameterMap() {
        return this.entityMap;
    }

    @Override
    public String toString() {
        return this.entityMap.toString();
    }
}
