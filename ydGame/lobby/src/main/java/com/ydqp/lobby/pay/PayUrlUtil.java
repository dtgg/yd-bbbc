package com.ydqp.lobby.pay;

import com.cfq.log.Logger;
import com.cfq.log.LoggerFactory;
import com.cfq.util.ReadProperties;
import com.cfq.util.StackTraceUtil;

import java.util.Map;

public class PayUrlUtil {

    private static final Logger logger = LoggerFactory.getLogger(PayUrlUtil.class);

    private PayUrlUtil() {
        try {
            propertiesValues = ReadProperties.getPropertiesValues("pay.properties");
        } catch (Exception e) {
            logger.error(StackTraceUtil.getStackTrace(e));
        }
    }

    private static final PayUrlUtil instance = new PayUrlUtil();

    public static PayUrlUtil getInstance() {
        return instance;
    }

    private Map<String, String> propertiesValues;

    public String getUrl(String payChannel){
        return propertiesValues.get(payChannel);
    }
}
