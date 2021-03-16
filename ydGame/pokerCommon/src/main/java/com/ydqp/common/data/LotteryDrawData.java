package com.ydqp.common.data;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class LotteryDrawData {

    private String drawNum;

    private Map<Integer, BigDecimal> drawNumMap;
}
